package statistics.rserve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import statistics.exception.RServeStatsException;

/**
 * @author Morgan
 */
public class RServeStats {

	private RConnection connection;
	private double limit;
	private final String diffSign = "Différence significative";
	private final String diffNonSign = "Différence non significative";

	/**
	 * Constructor. Connects to localhost.
	 *
	 * @throws statistics.exception.RServeStatsException
	 */
	public RServeStats() throws RServeStatsException {
		try {
			connection = new RConnection();
		} catch (RserveException e) {
			String msg = "Error with RServe connection : " + e.getMessage();
			System.err.println(msg);
			throw new RServeStatsException(msg);
		}
		limit = 0.05;
	}

	/**
	 * Constructor. Connects to provided host
	 *
	 * @param host : name of RServe host
	 * @throws statistics.exception.RServeStatsException
	 */
	public RServeStats(String host) throws RServeStatsException {
		try {
			connection = new RConnection(host);
		} catch (Exception e) {
			String msg = "Error with RServe connection : " + e.getMessage();
			System.err.println(msg);
			throw new RServeStatsException(msg);
		}
		limit = 0.05;
	}

	/**
	 * Constructor.
	 *
	 * @param connection :
	 */
	public RServeStats(RConnection connection) {
		this.connection = connection;
		limit = 0.05;
	}

	/**
	 *
	 * @return
	 */
	public double getLimit() {
		return limit;
	}

	/**
	 * Sets the limit for the acceptation of p-value
	 *
	 * @param limit : a double representing the percentage of acceptation (ex:
	 * 5% -> 0.05)
	 */
	public void setLimit(double limit) {
		this.limit = limit;
	}

	/**
	 * Does a "read.table" on the provided file (with headers) & returns the
	 * "summary"
	 *
	 * @param filePath : the complete file path
	 * @param sep : the separator string to use when reading the file
	 * @return A string containing the summary
	 */
	public String readData(String filePath, String sep) {
		String file = filePath.replaceAll(Pattern.quote("\\"), "/");
		try {
			connection.voidEval("tab <- read.table(\"" + file + "\", h=TRUE, sep=\"" + sep + "\")");
			System.out.println("file read");

			String[] names = connection.eval("names(tab)").asStrings();
			String[] summary = connection.eval("summary(tab)").asStrings();
			int len = summary.length / names.length;

			String result = "";
			int i = 0;
			for (String name : names) {
				result += name + " : " + '\n';
				for (int j = 0; j < len; j++, i++) {
					if (summary[i] != null) {
						result += summary[i] + '\n';
					}
				}
				result += '\n';
			}
			return result;
		} catch (RserveException ex) {
			System.err.println("Error RServe (readData) : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error (readData) : " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Does a simple regression-correlation operation on the data that has been
	 * read (data=tab) based on the parameters received.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @return A String with the results
	 */
	public String simRegCorr(String varExpliquee, String varExplicative) {
		try {
			String results = "REGRESSION-CORRELATION simple :" + '\n';
			connection.voidEval("model <- lm(" + varExpliquee + "~" + varExplicative + ", data=tab)");
			connection.voidEval("sum <- summary(model)");
			double corrCoef = connection.eval("sqrt(abs(sum$adj.r.squared))").asDouble();
			results += "Coefficient de corrélation : " + corrCoef + '\n';
			if (corrCoef < 0.4) {
				results += '\t' + "=> Pas de corrélation" + '\n';
			} else {
				if (corrCoef > 0.7) {
					results += '\t' + "=> Haute corrélation" + '\n';
				} else {
					results += '\t' + "=> Basse corrélation" + '\n';
				}
				double pValue = connection.eval("sum$coefficients[2,4]").asDouble();
				results += "P-value de régression : " + pValue + '\n';
				if (pValue >= limit) {
					results += '\t' + "=> Pas de régression" + '\n';
				} else {
					results += '\t' + "=> Présence d'une régression" + '\n';
					results += "Equation : " + '\n';
					double[] coeficients = connection.eval("model$\"coefficients\"").asDoubles();
					results += '\t' + varExpliquee + " = " + coeficients[0] + " + "
						+ coeficients[1] + "*" + varExplicative + '\n';
				}
			}
			return results;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Does a multiple regression-correlation operation on the data that has
	 * been read (data=tab) based on the parameters received.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicatives : the names of the explaining variables
	 * @return A String with the results
	 */
	public String multRegCorr(String varExpliquee, String[] varExplicatives) {
		try {
			String results = "REGRESSION-CORRELATION multiple :" + '\n';
			connection.voidEval("model <- lm(" + varExpliquee + "~., data=tab)");
			//Hyperplan d’ajustement
			double[] coeficients = connection.eval("model$\"coefficients\"").asDoubles();
			results += varExpliquee + " = " + coeficients[0];
			int i = 1;
			for (String varEx : varExplicatives) {
				results += " + " + coeficients[i++] + "*" + varEx;
			}
			results += "\n\n";
			for (double coeficient : coeficients) {
				if (coeficient == Double.NaN) {
					results += "Echantillon trop petit !";
					return results;
				}
			}
			//Reg-corr ?
			connection.voidEval("sum <- summary(model)");
			double corrCoef = connection.eval("sqrt(abs(sum$adj.r.squared))").asDouble();
			results += "Coefficient de corrélation : " + corrCoef + '\n';
			if (corrCoef < 0.4) {
				results += '\t' + "=> Pas de corrélation" + '\n';
			} else {
				if (corrCoef > 0.7) {
					results += '\t' + "=> Haute corrélation" + '\n';
				} else {
					results += '\t' + "=> Basse corrélation" + '\n';
				}
				double pValue = connection.eval("pf(sum$fstatistic[1], sum$fstatistic[2],"
					+ " sum$fstatistic[3], lower.tail=FALSE)").asDouble();
				results += "P-value de régression : " + pValue + '\n';
				if (pValue >= limit) {
					results += '\t' + "=> Pas de régression" + '\n';
				} else {
					results += '\t' + "=> Présence d'une régression" + '\n' + '\n';
					pValue = connection.eval("sum$coefficients[1,4]").asDouble();
					results += "P-value de la variable indépendante : " + pValue + '\n';
					if (pValue >= limit) {
						results += '\t' + "=> la variable indépendante n'est pas significative" + '\n';
						connection.voidEval("model <- lm(" + varExpliquee + "~.-1, data=tab)");
						connection.voidEval("sum <- summary(modelsi)");
						corrCoef = connection.eval("sqrt(abs(sum$adj.r.squared))").asDouble();
						results += "Coefficient de corrélation (sans variable indépendante) : " + corrCoef + '\n';
						if (corrCoef < 0.4) {
							results += '\t' + "=> Pas de corrélation" + '\n';
						} else {
							if (corrCoef > 0.7) {
								results += '\t' + "=> Haute corrélation" + '\n';
							} else {
								results += '\t' + "=> Basse corrélation" + '\n';
							}
							pValue = connection.eval("pf(sum$fstatistic[1],sum$fstatistic[2],"
								+ " sum$fstatistic[3], lower.tail=FALSE)").asDouble();
							results += "P-value de régression (sans variable indépendante) : " + pValue + '\n';
							if (pValue >= limit) {
								results += '\t' + "=> Pas de régression" + '\n';
							} else {
								results += '\t' + "=> Présence d'une régression" + '\n';
							}
							results += nonIndependantTerms(varExpliquee, varExplicatives, true);
						}
					} else {
						results += '\t' + "=> la variable indépendante est significative" + '\n';
						results += nonIndependantTerms(varExpliquee, varExplicatives, false);
					}
				}
			}
			return results;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Deals with the partial models.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicatives : the names of the explaining variables
	 * @param noInd : whether the independent term has to be removed
	 * @return A String with the results
	 * @throws RserveException
	 * @throws REXPMismatchException
	 */
	private String nonIndependantTerms(String varExpliquee, String[] varExplicatives, boolean noInd)
		throws RserveException, REXPMismatchException {
		String results = "\n";
		System.out.println("Debug : " + noInd);
		int i = noInd ? 1 : 2;
		System.out.println("Debug : " + connection.eval("sum$coefficients"));
		for (String varExpl : varExplicatives) {
			System.out.println("Debug : " + i + varExpl);
			double pValue = connection.eval("sum$coefficients[" + ( i++ ) + ",4]").asDouble();
			results += "P-value de " + varExpl + " : " + pValue + '\n';
			results += '\t' + varExpl + ( pValue < limit ? " a une influence sur "
				: " n'a pas d'influence sur " ) + varExpliquee + '\n';
		}
		//TODO : sous-models
		return results;
	}

	/**
	 * Does a Variance Analysis with 1 factor on the data read (data=tab) based
	 * on the parameters received.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @return A String with the results
	 */
	public String anova1(String varExpliquee, String varExplicative) {
		try {
			String results = "ANOVA : " + '\n';
			connection.voidEval("model <- lm(" + varExpliquee + "~" + varExplicative + ", data=tab)");
			//General
			connection.voidEval("an <- anova(model)");
			double fValue = connection.eval("an$\"F value\"[1]").asDouble();
			results += "F value : " + fValue + '\n';
			double pValue = connection.eval("an$\"Pr(>F)\"[1]").asDouble();
			results += "Pr(>F) : " + pValue + '\n';
			results += '\t' + "=> " + ( pValue < limit ? diffSign : diffNonSign ) + '\n';
			//Separate
			results += '\n' + "Summary :" + '\n';
			connection.voidEval("sum <- summary(model)");
			results += "Coefficients :" + '\n';
			double[] coefficients = connection.eval("sum$coefficients[,4]").asDoubles();
			String[] coefNames = connection.eval("names(sum$coefficients[,4])").asStrings();
			for (int i = 0; i < coefficients.length; i++) {
				results += coefNames[i] + " : " + coefficients[i] + '\n';
				results += '\t' + "=> " + ( coefficients[i] < limit ? diffSign : diffNonSign ) + '\n';
			}
			return results;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 * Does a Variance Analysis with 2 factors on the data read (data=tab) based
	 * on the parameters received.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the 1st explaining variable
	 * @param varExplicative2 : the name of the 2nd explaining variable
	 * @return A String with the results
	 */
	public String anova2(String varExpliquee, String varExplicative, String varExplicative2) {
		try {
			String results = "ANOVA (avec interraction) : " + '\n';
			connection.voidEval("model <- lm(" + varExpliquee + "~"
				+ varExplicative + "*" + varExplicative2 + ", data=tab)");
			connection.voidEval("an <- anova(model)");
			double[] fValues = connection.eval("an[1:3,4]").asDoubles();
			double[] pValues = connection.eval("an[1:3,5]").asDoubles();
			results += " " + '\t' + '\t' + "F value" + '\t' + '\t' + "Pr(>F)" + '\n';
			results += varExplicative + '\t' + '\t' + fValues[0] + '\t' + pValues[0] + '\n';
			results += varExplicative2 + '\t' + '\t' + fValues[1] + '\t' + pValues[1] + '\n';
			results += varExplicative + ":" + varExplicative2 + '\t'
				+ fValues[2] + '\t' + pValues[2] + '\n';
			if (pValues[2] < limit) {
				results += '\t' + "=> l'interraction est significative" + '\n';
			} else {
				results += '\t' + "=> l'interraction n'est pas significative" + '\n';
				connection.voidEval("modelsi <- lm(" + varExpliquee + "~"
					+ varExplicative + "+" + varExplicative2 + ", data=tab)");
				connection.voidEval("ansi <- anova(modelsi)");
				results += '\n' + "ANOVA (sans interraction) : " + '\n';
				double pValVar1 = connection.eval("ansi$\"Pr(>F)\"[1]").asDouble();
				double pValVar2 = connection.eval("ansi$\"Pr(>F)\"[2]").asDouble();
				results += varExplicative + " : Pr(>F) : " + pValVar1 + '\n';
				results += '\t' + ( pValVar1 < limit ? diffSign : diffNonSign ) + '\n';
				results += varExplicative2 + " : Pr(>F) : " + pValVar2 + '\n';
				results += '\t' + ( pValVar2 < limit ? diffSign : diffNonSign ) + '\n';
			}
			return results;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @return The JFreeChart created
	 */
	public HashMap<String, List> getDataForBoxAndWiskersChart(
		String varExpliquee, String varExplicative) {
		try {
			String[] categories = connection.eval("sapply(tab, levels)$\"" + varExplicative + "\"").asStrings();
			HashMap<String, List> data = new HashMap<>();
			for (String categorie : categories) {
				double[] mesures = connection.eval("subset(tab, " + varExplicative
					+ "==\"" + categorie + "\")$\"" + varExpliquee + "\"").asDoubles();
				/*
				List l = new ArrayList(mesures.length) {
					{ for (double mesure : mesures) { add(mesure); } }
				};//*/
				ArrayList<Double> l = new ArrayList();
				for (double mesure : mesures) {
					l.add(mesure);
				}
				data.put(categorie, l);
			}
			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the 1st explaining variable
	 * @param varExplicative2 : the name of the 2nd explaining variable
	 * @return The JFreeChart created
	 */
	public HashMap<String, HashMap<String, Double>> getDataForMultipleLinesChart(
		String varExpliquee, String varExplicative, String varExplicative2) {
		try {
			String[] xCategories = connection.eval("sapply(tab, levels)$\"" + varExplicative + "\"").asStrings();
			String[] lineCategories = connection.eval("sapply(tab, levels)$\"" + varExplicative2 + "\"").asStrings();
			HashMap<String, HashMap<String, Double>> data = new HashMap<>();
			for (String lineCategory : lineCategories) {
				connection.voidEval("sub <- subset(tab, " + varExplicative2 + "==\"" + lineCategory + "\")");
				HashMap<String, Double> value = new HashMap<>();
				for (String xCategory : xCategories) {
					double yValue = connection.eval("mean(subset(sub, " + varExplicative
						+ "==\"" + xCategory + "\")$\"" + varExpliquee + "\")").asDouble();
					value.put(xCategory, yValue);
				}
				data.put(lineCategory, value);
			}
			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @return The JFreeChart created
	 */
	public HashMap<String, Object> getDataForScatterPlotChart(
		String varExpliquee, String varExplicative) {
		try {
			//Get data
			double[] xValues = connection.eval("tab$\"" + varExplicative + "\"").asDoubles();
			Number[] x = new Double[xValues.length];
			double[] yValues = connection.eval("tab$\"" + varExpliquee + "\"").asDoubles();
			Number[] y = new Double[yValues.length];
			for (int i = 0; i < xValues.length; i++) {
				x[i] = xValues[i];
				y[i] = yValues[i];
			}
			double min = connection.eval("min(tab$\"" + varExplicative + "\")").asDouble();
			double max = connection.eval("max(tab$\"" + varExplicative + "\")").asDouble();
			//Build map
			HashMap<String, Object> data = new HashMap<>();
			data.put("xValues", x);
			data.put("yValues", y);
			data.put("min", min);
			data.put("max", max);

			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicatives : the names of the explaining variables
	 * @return The JFreeChart created
	 */
	public HashMap<String, Object> getDataForMultipleScatterPlotsChart(
		String varExpliquee, String[] varExplicatives) {
		try {
			//Get data
			double[] yValues = connection.eval("tab$\"" + varExpliquee + "\"").asDoubles();
			HashMap<String, double[]> xValues = new HashMap<>();
			for (String varExplicative : varExplicatives) {
				double[] values = connection.eval("tab$\"" + varExplicative + "\"").asDoubles();
				xValues.put(varExplicative, values);
			}
			//Build map
			HashMap<String, Object> data = new HashMap<>();
			data.put("xValues", xValues);
			data.put("yValues", yValues);

			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee
	 * @param varExplicative
	 * @return
	 */
	public HashMap<String, Double> getDataForPieChart(String varExpliquee, String varExplicative) {
		try {
			String[] categories = connection.eval("sapply(tab, levels)$\"" + varExplicative + "\"").asStrings();
			HashMap<String, Double> data = new HashMap<>();
			for (String categorie : categories) {
				double[] mesures = connection.eval("subset(tab, " + varExplicative
					+ "==\"" + categorie + "\")$\"" + varExpliquee + "\"").asDoubles();
				Double value = 0.0;
				for (double mesure : mesures) {
					value += mesure;
				}
				data.put(categorie, value);
			}
			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param varExpliquee
	 * @param varExplicative
	 * @return
	 */
	public HashMap<Integer, Double> getDataForBarChart(String varExpliquee, String varExplicative) {
		try {
			//Get data
			int[] xValues = connection.eval("tab$\"" + varExplicative + "\"").asIntegers();
			double[] yValues = connection.eval("tab$\"" + varExpliquee + "\"").asDoubles();
			//Build map
			HashMap<Integer, Double> data = new HashMap<>();
			for (int i = 0; i < xValues.length; i++) {
				int x = xValues[i];
				double y = yValues[i];
				Double get = data.get(x);
				if (get == null) {
					data.put(x, y);
				} else {
					data.put(x, get + y);
				}
			}
			return data;
		} catch (RserveException ex) {
			System.err.println("Error RServe : " + ex.getMessage());
		} catch (REXPMismatchException ex) {
			System.err.println("Casting error : " + ex.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @return
	 */
	public boolean closeRConnection() {
		return connection.close();
	}

}

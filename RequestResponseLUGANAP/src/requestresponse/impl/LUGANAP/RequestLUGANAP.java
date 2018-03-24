package requestresponse.impl.LUGANAP;

import db.airport.DAOFactoryAirport;
import db.airport.models.Airline;
import db.airport.models.stats.WeightDistance;
import db.airport.models.stats.WeightZone;
import db.decisions.DAOFactoryDecision;
import db.decisions.models.Statistics;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;
import statistics.exception.RServeStatsException;
import statistics.rserve.RServeStats;

/**
 * Implementation of Request for the LUGANAP protocol.
 *
 * @author Morgan
 */
public class RequestLUGANAP extends AbstractRequest {

	//Protocol constants
	public final static int GET_AIRLINES = 2;
	public final static int REG_CORR_LUG = 3;
	public final static int REG_CORR_LUG_PLUS = 4;
	public final static int ANOVA_1_LUG = 5;
	public final static int ANOVA_2_LUG_HF = 6;

	/**
	 * Constructor.
	 *
	 * @param type : one of the constants defined in the Request
	 * @param chargeUtile : the "message" to transmit
	 */
	public RequestLUGANAP(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 * Creates a Runnable that will call a different method depending on the
	 * type
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 * @return a Runnable
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				response = null;
				if (!parent.isLoggedIn() && type != LOGIN) {
					handleUnknownOrFail(s, cs, "NOT LOGGED IN");
				} else {
					switch (type) {
						case LOGIN:
							parent.setLoggedIn(handleLogin(s, cs));
							break;
						case LOGOUT:
							parent.setLoggedIn(handleLogout(s, cs));
							break;
						case GET_AIRLINES:
							handleGetAirlines(s, cs);
							break;
						case REG_CORR_LUG:
							handleRegCorrLug(s, cs);
							break;
						case REG_CORR_LUG_PLUS:
							handleRegCorrLugPlus(s, cs);
							break;
						case ANOVA_1_LUG:
							handleAnova1Lug(s, cs);
							break;
						case ANOVA_2_LUG_HF:
							handleAnova2LugHF(s, cs);
							break;
						default:
							handleUnknownOrFail(s, cs, "unknown command");
					}
				}
				send(s, response);
			}
		};
	}

	/**
	 * Builds the Response for a GET_AIRLINES Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleGetAirlines(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAP.RequestLUGANAP.handleGetAirlines()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#GET_AIRLINES#" + Thread.currentThread().getName());
		ArrayList<Airline> airlines = DAOFactoryAirport.getInstance().getAirlineDAO().selectAll();
		if (airlines != null) response = new ResponseLUGANAP(ResponseLUGANAP.GET_AIRLINES_OK, airlines);
		else response = new ResponseLUGANAP(ResponseLUGANAP.GET_AIRLINES_FAIL, "No airlines");
	}

	/**
	 * Builds the Response for a REG_CORR_LUG Request.
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleRegCorrLug(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAP.RequestLUGANAP.handleRegCorrLug()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#REG_CORR_LUG#" + Thread.currentThread().getName());
		//Get data
		Object[] cu = (Object[]) getChargeUtile();
		int year = (int) cu[0];
		int month = (int) cu[1];
		String airline = (String) cu[2];
		String varExpliquee = "MeanWeight";
		String varExplicative = "Distance";
		ArrayList<WeightDistance> weightDistances = DAOFactoryAirport.getInstance().getStatsDAO()
			.selectMeanWeightsAndDistances(year, month, airline);
		if (weightDistances != null && !weightDistances.isEmpty()) {
			//Write csv file
			String title = "RelationsPoidsDistance_" + year + ( month > 0 ?
				"_" + month : "" ) + ( airline != null ? "_" + airline : "" );
			String path = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "Data" + System.getProperty("file.separator") + title + ".csv";
			try (PrintWriter pw = new PrintWriter(path)) {
				StringBuilder sb = new StringBuilder(varExpliquee + ';' + varExplicative + '\n');
				for (WeightDistance wd : weightDistances) {
					sb.append(wd.getMeanWeight()).append(';').append(wd.getDistance()).append('\n');
				}
				pw.write(sb.toString());
				pw.flush();
			} catch (FileNotFoundException e) {
				System.err.println("Error with file : " + e.getMessage());
			}
			try {
				//Build response
				RServeStats rss = new RServeStats();
				rss.readData(path, ";");
				String simRegCorr = rss.simRegCorr(varExpliquee, varExplicative);
				HashMap<String, Object> dataPlot = rss.getDataForScatterPlotChart(varExpliquee, varExplicative);
				HashMap<String, HashMap<Integer, Double>> dataBar = new HashMap<>();
				HashMap<Integer, Double> value = rss.getDataForBarChart(varExpliquee, varExplicative);
				if (month > 0) {
					String[] monthName = {"Janvier", "Fevrier", "Mars", "Avril", "Mai",
						"Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
					dataBar.put(monthName[month - 1], value);
					//get next month data
					weightDistances = DAOFactoryAirport.getInstance().getStatsDAO()
						.selectMeanWeightsAndDistances(year, month + 1, airline);
					if (weightDistances != null && !weightDistances.isEmpty()) {
						//Write csv file
						path = System.getProperty("user.dir") + System.getProperty("file.separator")
							+ "Data" + System.getProperty("file.separator") + "RelationsPoidsDistance_"
							+ year + month + 1 + ( airline != null ? "_" + airline : "" )
							+ ".csv";
						try (PrintWriter pw = new PrintWriter(path)) {
							StringBuilder sb = new StringBuilder(varExpliquee + ';' + varExplicative + '\n');
							for (WeightDistance wd : weightDistances) {
								sb.append(wd.getMeanWeight()).append(';')
									.append(wd.getDistance()).append('\n');
							}
							pw.write(sb.toString());
							pw.flush();
						} catch (FileNotFoundException e) {
							System.err.println("Error with file : " + e.getMessage());
						}
						rss.readData(path, ";");
						value = rss.getDataForBarChart(varExpliquee, varExplicative);
						dataBar.put(monthName[month], value);
					}
				} else dataBar.put("" + year, value);
				rss.closeRConnection();
				//Insert into db_decisions
				if (simRegCorr != null) DAOFactoryDecision.getInstance()
					.getStatisticsDAO().insertOne(new Statistics(title, simRegCorr));
				//Send to client
				if (simRegCorr != null && dataPlot != null && !dataBar.isEmpty()) {
					Object[] toSend = {simRegCorr, dataPlot, dataBar};
					response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_OK, toSend);
				} else response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_FAIL, "Error RServe");
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_FAIL, msg);
			}
		} else response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_FAIL, "No data");
	}

	/**
	 * Builds the Response for a REG_CORR_LUG_PLUS Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleRegCorrLugPlus(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAP.RequestLUGANAP.handleRegCorrLugPlus()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#REG_CORR_LUG_PLUS#" + Thread.currentThread().getName());
		//Get dataPlot
		Object[] cu = (Object[]) getChargeUtile();
		int year = (int) cu[0];
		int month = (int) cu[1];
		String airline = (String) cu[2];
		boolean nbAcc = (boolean) cu[3];
		boolean age = (boolean) cu[4];
		String varExpliquee = "MeanWeight";
		String varExplicative = "Distance";
		String varExplicative2 = "NbAcc";
		String varExplicative3 = "Age";
		ArrayList<WeightDistance> weightDistances = DAOFactoryAirport.getInstance().getStatsDAO()
			.selectMeanWeightsAndDistances(year, month, airline, nbAcc, age);
		if (weightDistances != null && !weightDistances.isEmpty()) {
			//Write csv file
			String title = "RelationsPoidsDistance" + (nbAcc ? "NbAcc" : "") +
				(age ? "Age" : "") + "_" + year + ( month > 0 ? "_" + month : "" )
				+ ( airline != null ? "_" + airline : "" );
			String path = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "Data" + System.getProperty("file.separator") + title + ".csv";
			try (PrintWriter pw = new PrintWriter(path)) {
				StringBuilder sb = new StringBuilder(varExpliquee + ';' + varExplicative);
				if (nbAcc) sb.append(';').append(varExplicative2);
				if (age) sb.append(';').append(varExplicative3);
				sb.append('\n');
				for (WeightDistance wd : weightDistances) {
					sb.append(wd.getMeanWeight()).append(';').append(wd.getDistance());
					if (nbAcc) sb.append(';').append(wd.getNbAcc());
					if (age) sb.append(';').append(wd.getAge());
					sb.append('\n');
				}
				pw.write(sb.toString());
				pw.flush();
			} catch (FileNotFoundException e) {
				System.err.println("Error with file : " + e.getMessage());
			}
			try {
				//Build response
				RServeStats rss = new RServeStats();
				rss.readData(path, ";");
				String[] varExplicatives;
				varExplicatives = ( age && nbAcc ) ? new String[3] : new String[2];
				varExplicatives[0] = varExplicative;
				if (age && nbAcc) {
					varExplicatives[1] = varExplicative2;
					varExplicatives[2] = varExplicative3;
				} else varExplicatives[1] = (age) ? varExplicative3 : varExplicative2;
				String multRegCorr = rss.multRegCorr(varExpliquee, varExplicatives);
				rss.closeRConnection();
				if (multRegCorr != null) {
					//Insert into db_decisions
					DAOFactoryDecision.getInstance().getStatisticsDAO()
						.insertOne(new Statistics(title, multRegCorr));
					//Send to client
					response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_PLUS_OK, multRegCorr);
				} else response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_PLUS_FAIL, "Error RServe");
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_PLUS_FAIL, msg);
			}
		} else response = new ResponseLUGANAP(ResponseLUGANAP.REG_CORR_LUG_PLUS_FAIL, "No data");
	}

	/**
	 * Builds the Response for a ANOVA_1_LUG Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleAnova1Lug(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAP.RequestLUGANAP.handleAnova1Lug()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#ANOVA_1_LUG#" + Thread.currentThread().getName());
		//Get dataPlot
		Object[] cu = (Object[]) getChargeUtile();
		int year = (int) cu[0];
		int month = (int) cu[1];
		String airline = (String) cu[2];
		String varExpliquee = "MeanWeight";
		String varExplicative = "Zone";
		String varExpliquee2 = "TotalWeight";
		ArrayList<WeightZone> weightZones = DAOFactoryAirport.getInstance()
			.getStatsDAO().selectWeightsAndZones(year, month, airline, false);
		if (weightZones != null && !weightZones.isEmpty()) {
			//Write csv file
			String title = "RelationsPoidsZone_" + year + ( month > 0 ?
				"_" + month : "" ) + ( airline != null ? "_" + airline : "" );
			String path = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "Data" + System.getProperty("file.separator") + title + ".csv";
			try (PrintWriter pw = new PrintWriter(path)) {
				StringBuilder sb = new StringBuilder(varExpliquee + ';' +
					varExplicative + ';' + varExpliquee2 + '\n');
				for (WeightZone wz : weightZones) {
					sb.append(wz.getMeanWeight()).append(';').append(wz.getZone())
						.append(';').append(wz.getTotalWeight()).append('\n');
				}
				pw.write(sb.toString());
				pw.flush();
			} catch (FileNotFoundException e) {
				System.err.println("Error with file : " + e.getMessage());
			}
			try {
				//Build response
				RServeStats rss = new RServeStats();
				rss.readData(path, ";");
				String anova = rss.anova1(varExpliquee, varExplicative);
				HashMap<String, List> dataBox = rss.getDataForBoxAndWiskersChart(varExpliquee, varExplicative);
				HashMap<String, Double> dataPie = rss.getDataForPieChart(varExpliquee, varExplicative);
				rss.closeRConnection();
				//Insert into db_decisions
				if (anova != null) DAOFactoryDecision.getInstance()
					.getStatisticsDAO().insertOne(new Statistics(title, anova));
				//Send to client
				if (anova != null && dataBox != null && dataPie != null) {
					Object[] toSend = {anova, dataBox, dataPie};
					response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_1_LUG_OK, toSend);
				} else response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_1_LUG_FAIL, "Error RServe");
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_1_LUG_FAIL, msg);
			}
		} else response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_1_LUG_FAIL, "No Data");
	}

	/**
	 * Builds the Response for a ANOVA_2_LUG_HF Request
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleAnova2LugHF(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAP.RequestLUGANAP.handleAnova2LugHF()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#ANOVA_2_LUG_HF#" + Thread.currentThread().getName());
		//Get dataPlot
		Object[] cu = (Object[]) getChargeUtile();
		int year = (int) cu[0];
		int month = (int) cu[1];
		String airline = (String) cu[2];
		String varExpliquee = "MeanWeight";
		String varExplicative = "Zone";
		String varExplicative2 = "Gender";
		ArrayList<WeightZone> weightZones = DAOFactoryAirport.getInstance()
			.getStatsDAO().selectWeightsAndZones(year, month, airline, true);
		if (weightZones != null && !weightZones.isEmpty()) {
			//Write csv file
			String title = "RelationsPoidsZoneGender_" + year + ( month > 0 ?
				"_" + month : "" ) + ( airline != null ? "_" + airline : "" );
			String path = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "Data" + System.getProperty("file.separator") + title + ".csv";
			try (PrintWriter pw = new PrintWriter(path)) {
				StringBuilder sb = new StringBuilder(varExpliquee + ';' +
					varExplicative + ';' + varExplicative2 + '\n');
				for (WeightZone wz : weightZones) {
					sb.append(wz.getMeanWeight()).append(';').append(wz.getZone())
						.append(';').append(wz.getGender()).append('\n');
				}
				pw.write(sb.toString());
				pw.flush();
			} catch (FileNotFoundException e) {
				System.err.println("Error with file : " + e.getMessage());
			}
			try {
				//Build response
				RServeStats rss = new RServeStats();
				rss.readData(path, ";");
				String anova = rss.anova2(varExpliquee, varExplicative, varExplicative2);
				rss.closeRConnection();
				if (anova != null) {
					//Insert into db_decisions
					DAOFactoryDecision.getInstance().getStatisticsDAO()
						.insertOne(new Statistics(title, anova));
					//Send to client
					response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_2_LUG_HF_OK, anova);
				} else response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_2_LUG_HF_FAIL, "Error RServe");
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_2_LUG_HF_FAIL, msg);
			}
		} else response = new ResponseLUGANAP(ResponseLUGANAP.ANOVA_2_LUG_HF_FAIL, "No Data");
	}

}

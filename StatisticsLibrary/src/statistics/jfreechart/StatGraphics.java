package statistics.jfreechart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.Statistics;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author Morgan
 */
public class StatGraphics {

	/**
	 * Creates a BoxAndWiskers chart & returns it.
	 * 
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @param data : contains the categories as key and a list of its mesures as value
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildBoxAndWiskersChart(String varExpliquee,
		String varExplicative, HashMap<String, List> data) {
		//Create DataSet
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		for (Map.Entry<String, List> entry : data.entrySet()) {
			dataset.add(entry.getValue(), varExplicative, entry.getKey());
		}
		//Create Chart
		JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
			" ", varExplicative, varExpliquee, dataset, false);
		return chart;
	}

	/**
	 * Creates a Line Chart graphic with multiple lines & returns it.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @param data : contains the line categories as key and a hashmap as value.
	 * The value contains the x categories as key and the y values as value.
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildMultipleLinesChart(String varExpliquee,
		String varExplicative, HashMap<String, HashMap<String, Double>> data) {
		//Create DataSet
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map.Entry<String, HashMap<String, Double>> mapEntry : data.entrySet()) {
			String lineCategory = mapEntry.getKey();
			for (Map.Entry<String, Double> entry : mapEntry.getValue().entrySet()) {
				dataset.addValue(entry.getValue(), lineCategory, entry.getKey());
			}
		}
		//Create Chart
		JFreeChart chart = ChartFactory.createLineChart(" ",
			varExplicative, "Moyenne de " + varExpliquee, dataset,
			PlotOrientation.VERTICAL, true, false, false);
		return chart;
	}

	/**
	 * Creates a Scatter Plot graphic & returns it.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @param data : contains all the values of varExplicative, all the values
	 * of varExpliquee, the smallest value of x & the largest value of x
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildScatterPlotChart(String varExpliquee,
		String varExplicative, HashMap<String, Object> data) {
		Number[] x = (Number[]) data.get("xValues");
		Number[] y = (Number[]) data.get("yValues");
		double min = (double) data.get("min");
		double max = (double) data.get("max");
		String title = "Relations " + varExpliquee + "-" + varExplicative;
		//Create DataSet
		XYSeries xys = new XYSeries(title);
		for (int i = 0; i < x.length; i++) {
			xys.add(x[i], y[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection(xys);
		//Add abline
		double[] linearFit = Statistics.getLinearFit(x, y);
		double a = linearFit[1];
		double b = linearFit[0];
		XYSeries abline = new XYSeries("Droite de régression");
		for (double xr = min; xr <= max; xr += 0.1) {
			double yr = a * xr + b;
			abline.add(xr, yr);
		}
		dataset.addSeries(abline);
		//Create Chart
		JFreeChart chart = ChartFactory.createScatterPlot(title, varExplicative,
			varExpliquee, dataset, PlotOrientation.VERTICAL, true, false, false);
		return chart;
	}

	/**
	 * Creates a graphic with multiple Scatter Plots & returns it.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param data : contains all the values of varExpliquee & a hasmap that
	 * contains the varExplicatives as key and all their values as value
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildMultipleScatterPlotsChart(String varExpliquee,
		HashMap<String, Object> data) {
		HashMap<String, double[]> xValues =
			(HashMap<String, double[]>) data.get("xValues");
		double[] yValues = (double[]) data.get("yValues");
		//Create DataSet
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (Map.Entry<String, double[]> entry : xValues.entrySet()) {
			String varExplicative = entry.getKey();
			//Create Series
			XYSeries xys = new XYSeries("Relations " + varExpliquee + "-" + varExplicative);
			double[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				xys.add(values[i], yValues[i]);
			}
			dataset.addSeries(xys);
		}
		//Create Chart
		JFreeChart chart = ChartFactory.createScatterPlot(" ", " ",
			varExpliquee, dataset, PlotOrientation.VERTICAL, true, false, false);
		return chart;
	}

	/**
	 * Creates a Pie Chart graphic & returns it.
	 * 
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @param data : contains all the values of varExplicative as key & all the
	 * values of varExplicative as value
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildPieChart(String varExpliquee,
		String varExplicative, HashMap<String, Double> data){
		//Create DataSet
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Map.Entry<String, Double> entry : data.entrySet()) {
			dataset.setValue(entry.getKey(), entry.getValue());
		}
		//Create Chart
		JFreeChart chart = ChartFactory.createPieChart(
			varExpliquee + " par " + varExplicative, dataset, true, false, false);
		return chart;
	}

	/**
	 * Creates a Bar Chart graphic & returns it.
	 *
	 * @param varExpliquee : the name of the explained variable
	 * @param varExplicative : the name of the explaining variable
	 * @param data :
	 * @return The JFreeChart created
	 */
	public static JFreeChart buildBarChart(String varExpliquee,
		String varExplicative, HashMap<String, HashMap<Integer, Double>> data) {
		//Create DataSet
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map.Entry<String, HashMap<Integer, Double>> mapEntry : data.entrySet()) {
			String rowKey = mapEntry.getKey();
			for (Map.Entry<Integer, Double> entry : mapEntry.getValue().entrySet()) {
				Integer columnKey = entry.getKey();
				Double value = entry.getValue();
				dataset.addValue(value, rowKey, columnKey);
			}
		}
		//Create Chart
		JFreeChart chart = ChartFactory.createBarChart(" ", varExpliquee,
			varExplicative, dataset, PlotOrientation.VERTICAL, true, false, false);
		return chart;
	}

}

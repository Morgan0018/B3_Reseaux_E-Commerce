package requestresponse.impl.LUGANAPM;

import db.airport.DAOFactoryAirport;
import db.airport.models.Agent;
import db.airport.models.Airline;
import db.airport.models.stats.WeightDistance;
import db.airport.models.stats.WeightZone;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import requestresponse.multithread.abstracts.AbstractRequest;
import requestresponse.multithread.interfaces.ConsoleServer;
import statistics.exception.RServeStatsException;
import statistics.rserve.RServeStats;

/**
 * Implementation of Request for the LUGANAPM protocol.
 *
 * @author Morgan
 */
public class RequestLUGANAPM extends AbstractRequest {

	//Protocol constants
	public final static int REG_CORR_LUG_M = 2;
	public final static int ANOVA_1_LUG_M = 3;

	/**
	 * Constructor.
	 *
	 * @param type : one of the constants defined in the Request
	 * @param chargeUtile : the "message" to transmit
	 */
	public RequestLUGANAPM(int type, Object chargeUtile) {
		super(type, chargeUtile);
	}

	/**
	 *
	 * @param s
	 * @param cs
	 * @return
	 */
	@Override
	public Runnable createRunnable(Socket s, ConsoleServer cs) {
		return new Runnable() {
			@Override
			public void run() {
				response = null;
				if (!parent.isLoggedIn() && type != LOGIN && type != LOGOUT) {
					handleUnknownOrFail(s, cs, "NOT LOGGED IN");
				} else {
					switch (type) {
						case LOGIN:
							parent.setLoggedIn(handleLogin(s, cs));
							break;
						case LOGOUT:
							parent.setLoggedIn(handleLogout(s, cs));
							break;
						case REG_CORR_LUG_M:
							handleRegCorrLug(s, cs);
							break;
						case ANOVA_1_LUG_M:
							handleAnova1Lug(s, cs);
							break;
						default:
							handleUnknownOrFail(s, cs, "Unknown Command");
					}
				}
				send(s, response);
			}
		};
	}

	/**
	 * Builds the Response for a LOGIN Request. Verifies login information. If
	 * it is valid, adds a list of Airline to the Response.
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 * @return a boolean representing the success (or not) of the login
	 */
	@Override
	protected boolean handleLogin(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAPM.RequestLUGANAPM.handleLogin()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#LOGIN#" + Thread.currentThread().getName());
		Object cu[] = (Object[]) getChargeUtile();
		String login = (String) cu[0];
		String pwd = (String) cu[1];
		Agent a = DAOFactoryAirport.getInstance().getAgentDAO().selectOne(login);
		if (pwd.equals(a.getPassword())) {
			ArrayList<Airline> airlines = DAOFactoryAirport.getInstance().getAirlineDAO().selectAll();
			if (airlines != null) {
				response = new ResponseLUGANAPM(ResponseLUGANAPM.LOGIN_OK, airlines);
				return true;
			} else {
				response = new ResponseLUGANAPM(ResponseLUGANAPM.LOGIN_FAIL, "No airlines");
			}
		} else {
			response = new ResponseLUGANAPM(ResponseLUGANAPM.LOGIN_FAIL, "No such Agent");
		}
		return false;
	}

	/**
	 * Builds the Response for a REG_CORR_LUG_M Request.
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleRegCorrLug(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAPM.RequestLUGANAPM.handleRegCorrLug()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#REG_CORR_LUG_M#" + Thread.currentThread().getName());
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
			String title = "RelationsPoidsDistance_" + year + ( month > 0
				? "_" + month : "" ) + ( airline != null ? "_" + airline : "" );
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
					dataBar.put(monthName[month - 1] + " " + year, value);
				} else {
					dataBar.put("" + year, value);
				}
				rss.closeRConnection();
				//Send to client
				if (simRegCorr != null && dataPlot != null && !dataBar.isEmpty()) {
					Object[] toSend = {simRegCorr, dataPlot, dataBar};
					response = new ResponseLUGANAPM(ResponseLUGANAPM.REG_CORR_LUG_M_OK, toSend);
				} else {
					response = new ResponseLUGANAPM(ResponseLUGANAPM.REG_CORR_LUG_M_FAIL, "Error RServe");
				}
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAPM(ResponseLUGANAPM.REG_CORR_LUG_M_FAIL, msg);
			}
		} else {
			response = new ResponseLUGANAPM(ResponseLUGANAPM.REG_CORR_LUG_M_FAIL, "No data");
		}
	}

	/**
	 * Builds the Response for a ANOVA_1_LUG_M Request.
	 *
	 * @param s : the client Socket
	 * @param cs : an Object implementing
	 * requestresponse.multithread.interfaces.ConsoleServer
	 */
	private void handleAnova1Lug(Socket s, ConsoleServer cs) {
		System.out.println("requestresponse.impl.LUGANAPM.RequestLUGANAPM.handleAnova1Lug()");
		cs.TraceEvents(s.getRemoteSocketAddress() + "#ANOVA_1_LUG_M#" + Thread.currentThread().getName());
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
			String title = "RelationsPoidsZone_" + year + ( month > 0
				? "_" + month : "" ) + ( airline != null ? "_" + airline : "" );
			String path = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "Data" + System.getProperty("file.separator") + title + ".csv";
			try (PrintWriter pw = new PrintWriter(path)) {
				StringBuilder sb = new StringBuilder(varExpliquee + ';'
					+ varExplicative + ';' + varExpliquee2 + '\n');
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
				HashMap<String, Double> dataPie = rss.getDataForPieChart(varExpliquee, varExplicative);
				rss.closeRConnection();
				//Send to client
				if (anova != null && dataPie != null) {
					Object[] toSend = {anova, dataPie};
					response = new ResponseLUGANAPM(ResponseLUGANAPM.ANOVA_1_LUG_M_OK, toSend);
				} else {
					response = new ResponseLUGANAPM(ResponseLUGANAPM.ANOVA_1_LUG_M_FAIL, "Error RServe");
				}
			} catch (RServeStatsException e) {
				String msg = "Error RServe : " + e.getMessage();
				System.err.println(msg);
				response = new ResponseLUGANAPM(ResponseLUGANAPM.ANOVA_1_LUG_M_FAIL, msg);
			}
		} else {
			response = new ResponseLUGANAPM(ResponseLUGANAPM.ANOVA_1_LUG_M_FAIL, "No Data");
		}
	}

}

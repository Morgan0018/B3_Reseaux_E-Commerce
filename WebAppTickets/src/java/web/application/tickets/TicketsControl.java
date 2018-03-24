package web.application.tickets;

import crypto.utilities.CryptoUtilities;
import db.airport.DAOFactoryAirport;
import db.airport.models.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import requestresponse.impl.PAYP.RequestPAYP;
import requestresponse.impl.PAYP.ResponsePAYP;

/**
 *
 * @author Morgan
 */
@WebServlet(name = "TicketsControl", urlPatterns = {"/TicketsControl"})
public class TicketsControl extends HttpServlet {

	// <editor-fold defaultstate="collapsed" desc="CONST">
	private static final String PATH_INDEX = "/index.jsp";
	private static final String PATH_NEW_CLIENT = "/jsp/new-client.jsp";
	private static final String PATH_INIT = "/jsp/init.jsp";
	private static final String PATH_CADDIE = "/jsp/caddie.jsp";
	private static final String PATH_PAY = "/jsp/pay.jsp";
	private static final String SECURITY = "/WEB-INF/security/ksWebApp";
	public static final String BUNDLE = "web.resources.Strings";

	//actions
	public static final String LANG = "lang";
	public static final String LOGIN = "login";
	public static final String NEW_CLIENT = "new_client";
	public static final String CHOICE_DEST = "choice_dest";
	public static final String BACK = "back";
	public static final String TAKE = "take";
	public static final String PAY = "pay";
	public static final String PAY_DONE = "pay_done";
	public static final String DELETE_T = "delete_tickets";

	//params
	public static final String LOCALE = "locale";
	public static final String LANG_LIST = "lang_list";
	public static final String DEST_LIST = "dest_list";
	public static final String MSG = "msg";
	public static final String PWD = "pwd";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String AGE = "age";
	public static final String GENDER = "gender";
	public static final String CLIENT = "client";
	public static final String DEST = "dest";
	public static final String FLIGHT_LIST = "flight_list";
	public static final String FLIGHT = "flight";
	public static final String NB_TAKE = "nb";
	public static final String NB_FREE = "nb_free";
	public static final String NUM_ID = "num_id";
	public static final String TICKETS = "tickets";
	public static final String NUM_CARD = "num_card";
	public static final String OWNER_NAME = "owner_name";
	public static final String E_MAIL = "e_mail";
	// </editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Variables">
	private final String ipAddrPay = "10.59.22.4";
	//private final String ipAddrPay = "192.168.1.6";
	private final int portPay = 30106;

	private final char[] pwd = "azerty".toCharArray();
	private final String aliasPay = "SerPay";
	private final String asymetricAlgorithm = "RSA/ECB/PKCS1Padding";
	private final String signAlgorithm = "SHA1withRSA";
	private final String userCertificate = "WebApp";

	private SendMail sm;
	private Locale locale;
	private ResourceBundle rb;
	//</editor-fold>

	/**
	 *
	 * @param config
	 *
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		getServletContext().log("--Start servlet FlightControl--");
		sm = new SendMail();
		changeLocale("fr");
	}

	/**
	 *
	 */
	@Override
	public void destroy() {
		getServletContext().log("--Destroy servlet FlightControl--");
		super.destroy();
	}

	//<editor-fold defaultstate="collapsed" desc="Private">
	/**
	 *
	 * @param l
	 */
	private void changeLocale(String l) {
		locale = new Locale(l);
		getServletContext().setAttribute(LOCALE, locale);
		rb = ResourceBundle.getBundle(BUNDLE, locale);
	}

	/**
	 *
	 */
	private void setApplicationVar() {
		ServletContext sc = getServletContext();
		if (sc.getAttribute(LANG_LIST) == null) {
			ArrayList<Language> languages = DAOFactoryAirport.getInstance()
				.getLanguageDAO().selectAll();
			sc.setAttribute(LANG_LIST, languages);
		}
		if (sc.getAttribute(DEST_LIST) == null) {
			ArrayList<Destination> destinations = DAOFactoryAirport.getInstance()
				.getDestinationDAO().selectAll();
			sc.setAttribute(DEST_LIST, destinations);
		}
	}
	//</editor-fold>

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 *
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		ServletContext sc = getServletContext();
		sc.log("--in TicketsControl--");
		setApplicationVar();
		String action = request.getParameter("action");
		if (action == null) action = "default";
		sc.log("--Debug : " + action);
		switch (action) {
			// <editor-fold defaultstate="collapsed" desc="case LOGIN">
			case LOGIN: {
				String login = request.getParameter(LOGIN);
				String pwdUser = request.getParameter(PWD);
				Client c = DAOFactoryAirport.getInstance().getClientDAO().selectOne(login);
				if (request.getParameter("exists") != null) {
					sc.log("--New client");
					if (c == null) { //No client with login
						request.setAttribute(LOGIN, login);
						request.setAttribute(PWD, pwdUser);
						sc.getRequestDispatcher(PATH_NEW_CLIENT).forward(request, response);
					} else {
						sc.log("--Login already in use");
						redirectToIndex(request, response, rb.getString("LoginUsed"));
					}
				} else { //Existing client
					sc.log("--Login attempt for : " + login);
					if (c != null && pwdUser.equals(c.getPassword())) {
						c.setPassenger(DAOFactoryAirport.getInstance()
							.getPassengerDAO().selectOne(c.getRefPassenger()));
						redirectToInit(request, response, c);
					} else {
						sc.log("--Login failed");
						redirectToIndex(request, response, rb.getString("BadInfo"));
					}
				}
				break;
			}
			// </editor-fold>
			// <editor-fold defaultstate="collapsed" desc="case NEW_CLIENT">
			case NEW_CLIENT: {
				boolean ok = false;
				String firstname = (String) request.getParameter(FIRST_NAME);
				String lastname = (String) request.getParameter(LAST_NAME);
				char g = ("male".equals((String) request.getParameter(GENDER))) ? 'M' : 'F';
				Passenger p = new Passenger(lastname, firstname,
					Integer.parseInt(request.getParameter(AGE)), g);
				String idP = (firstname.substring(0, 2)).toUpperCase() + lastname.toUpperCase();
				Client c = new Client((String) request.getParameter(LOGIN),
					(String) request.getParameter(PWD), idP);
				if (DAOFactoryAirport.getInstance().getPassengerDAO().insertNew(p)) {
					sc.log("--Création passenger ok");
					int id = DAOFactoryAirport.getInstance().getClientDAO().insertNewClient(c);
					if (id != 0) {
						sc.log("--Creation client ok");
						c.setId(id);
						c.setPassenger(p);
						ok = true;
					}
				} else
					DAOFactoryAirport.getInstance().getPassengerDAO().deletePassenger(idP);
				if (ok) redirectToInit(request, response, c);
				else {
					sc.log("--Creation failed");
					redirectToIndex(request, response, rb.getString("CreateFail"));
				}
				break;
			}
			// </editor-fold>
			default: {
				HttpSession session = request.getSession();
				if (session.getAttribute(CLIENT) == null)
					redirectToIndex(request, response, null);
				else switch (action) {
					//<editor-fold defaultstate="collapsed" desc="case LANG">
					case LANG: {
						String lang = request.getParameter(LANG);
						changeLocale(lang);
						break;
					}
					//</editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case CHOICE_DEST">
					case CHOICE_DEST: {
						String destId = request.getParameter(DEST);
						session.setAttribute(DEST, destId);
						redirectToCaddie(request, response, destId);
						break;
					}
					// </editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case BACK">
					case BACK:
						redirectToInit(request, response, null);
						break;
					// </editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case TAKE">
					case TAKE: {
						int flightId = Integer.parseInt(request.getParameter(FLIGHT));
						int nbTicks = Integer.parseInt(request.getParameter(NB_TAKE));
						int nbFree = Integer.parseInt(request.getParameter(NB_FREE));
						String refPassenger = ((Client) session.getAttribute(CLIENT)).getRefPassenger();
						if (nbTicks >= nbFree)
							redirectToCaddie(request, response, rb.getString("NoFree"));
						else { //creates tickets (not paid)
							ArrayList<Ticket> tickets = new ArrayList<>();
							for (int i = 0; i < nbTicks; i++) {
								tickets.add(new Ticket(refPassenger, flightId));
							}
							DAOFactoryAirport.getInstance().getTicketDAO()
								.insertMultipleNew(tickets);
							redirectToCaddie(request, response, null);
						}
						break;
					}
					// </editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case PAY">
					case PAY:
						redirectToPay(request, response, null);
						break;
					// </editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case PAY_DONE">
					case PAY_DONE: {
						String numId = (String) request.getParameter(NUM_ID);
						if (numId == null || "".equals(numId.trim()))
							redirectToPay(request, response, rb.getString("IdReq"));
						String numCard = (String) request.getParameter(NUM_CARD);
						if (numCard == null || "".equals(numCard.trim()))
							redirectToPay(request, response, rb.getString("NumCardReq"));
						String ownerName = (String) request.getParameter(OWNER_NAME);
						if (ownerName == null || "".equals(ownerName.trim()))
							redirectToPay(request, response, rb.getString("OwnerReq"));
						String email = (String) request.getParameter(E_MAIL);
						if (email == null || "".equals(email.trim()))
							redirectToPay(request, response, rb.getString("EmailReq"));
						String idP = ((Client) session.getAttribute(CLIENT)).getRefPassenger();
						float price = 0; //FIXME : check price
						if (makePayment(numCard, ownerName, price)
							&& DAOFactoryAirport.getInstance().getTicketDAO()
							.updateAllPaidForPassenger(idP)) {
							sm.sendMail(true, email);
							redirectToInit(request, response, (Client) session.getAttribute(CLIENT));
						} else {
							sm.sendMail(false, email);
							redirectToPay(request, response, rb.getString("PayFail"));
						}
						break;
					}
					// </editor-fold>
					// <editor-fold defaultstate="collapsed" desc="case DELETE_T">
					case DELETE_T:
						int flightId = Integer.parseInt(request.getParameter(FLIGHT));
						String refPassenger = ((Client) session.getAttribute(CLIENT)).getRefPassenger();
						if (DAOFactoryAirport.getInstance().getTicketDAO()
							.deleteUnpaidTicketsForFlightAndPassenger(flightId, refPassenger))
							redirectToPay(request, response, null);
						else redirectToPay(request, response, rb.getString("DeleteFail"));
						break;
					// </editor-fold>
					default: redirectToIndex(request, response, null);
				}
			}
		}
	}

	//<editor-fold defaultstate="collapsed" desc="PAYP">
	/**
	 *
	 * @param cardNumber
	 * @param ownerName
	 * @param price
	 *
	 * @return
	 */
	private boolean makePayment(String cardNumber, String ownerName, float price) {
		String ksFileName = getServletContext().getRealPath(SECURITY);
		PublicKey pk = CryptoUtilities
			.getPublicKeyfromKeystoreCertificate(ksFileName, pwd, aliasPay);
		byte[] cryptedCardNumber = CryptoUtilities
			.asymmetricEncryption(cardNumber, asymetricAlgorithm, pk);
		Object toSign[] = {cryptedCardNumber, ownerName, price, userCertificate};
		PrivateKey prk = CryptoUtilities
			.getPrivateKeyFromKeystore(ksFileName, pwd, userCertificate);
		byte[] sign = CryptoUtilities.sign(toSign, signAlgorithm, prk);
		Object cu[] = {toSign, sign};
		ResponsePAYP res = connectToServerPayment(
			new RequestPAYP(RequestPAYP.MAKE_PAYMENT, cu));
		if (res != null) return res.getCode() == ResponsePAYP.MAKE_PAYMENT_OK;
		return false;
	}

	/**
	 *
	 * @param req
	 *
	 * @return
	 */
	private ResponsePAYP connectToServerPayment(RequestPAYP req) {
		ResponsePAYP res = null;
		//Connection to the server
		try (Socket paymentSocket = new Socket(ipAddrPay, portPay)) {
			//Send request
			ObjectOutputStream outputStream = new ObjectOutputStream(paymentSocket.getOutputStream());
			outputStream.writeObject(req);
			outputStream.flush();
			//Read response
			try (ObjectInputStream inputStream
				= new ObjectInputStream(paymentSocket.getInputStream())) {
				res = (ResponsePAYP) inputStream.readObject();
			}
		} catch (NumberFormatException e) {
			System.err.println("Number Format Error : " + e.getMessage());
		} catch (IOException e) {
			System.err.println("IO Error : " + e.getMessage());
		} catch (ClassNotFoundException ex) {
			System.err.println("Receive Error - ClassNotFound : " + ex.getMessage());
		}
		return res;
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Redirects">
	/**
	 *
	 * @param request
	 * @param response
	 * @param msg
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToIndex(HttpServletRequest request, HttpServletResponse response, String msg)
		throws ServletException, IOException {
		ServletContext sc = getServletContext();
		sc.log("--redirect to index");
		request.setAttribute(MSG, msg);
		RequestDispatcher rd;
		if (msg == null) rd = sc.getRequestDispatcher(PATH_INDEX);
		else rd = sc.getRequestDispatcher(PATH_INDEX + "?msg=" + URLEncoder.encode(msg));
		rd.forward(request, response);
	}

	/**
	 *
	 * @param request
	 * @param response
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToInit(HttpServletRequest request, HttpServletResponse response, Client c)
		throws ServletException, IOException {
		if (c != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute(CLIENT, c);
		}
		ServletContext sc = getServletContext();
		sc.log("--Redirect to main site");
		sc.getRequestDispatcher(PATH_INIT).forward(request, response);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param msg
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToCaddie(HttpServletRequest request, HttpServletResponse response, String msg)
		throws ServletException, IOException {
		if (msg != null) request.setAttribute(MSG, msg);
		else request.setAttribute(MSG, null);
		String destId = (String) request.getSession().getAttribute(DEST);
		HashMap<Flight, Integer> flights = DAOFactoryAirport.getInstance()
			.getFlightDAO().selectFlightsForDestination(destId);
		request.setAttribute(FLIGHT_LIST, flights);
		ServletContext sc = getServletContext();
		sc.log("--redirect to caddie");
		sc.getRequestDispatcher(PATH_CADDIE).forward(request, response);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @param msg
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToPay(HttpServletRequest request, HttpServletResponse response, String msg)
		throws ServletException, IOException {
		HttpSession session = request.getSession();
		Client c = (Client) session.getAttribute(CLIENT);
		String numId = c.getPassenger().getNumId();
		request.setAttribute(NUM_ID, numId);
		HashMap<Flight, Integer> flightsMap = DAOFactoryAirport.getInstance()
			.getFlightDAO().selectFlightsWithUnpaidTicketsForPassenger(c.getRefPassenger());
		request.setAttribute(TICKETS, flightsMap);

		if (msg != null) request.setAttribute(MSG, msg);

		ServletContext sc = getServletContext();
		sc.log("--Redirect to paiement page");
		sc.getRequestDispatcher(PATH_PAY).forward(request, response);
	}
	//</editor-fold>

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 *
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 *
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}

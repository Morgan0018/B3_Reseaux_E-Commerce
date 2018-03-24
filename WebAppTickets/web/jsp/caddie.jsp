<%-- 
    Document   : JSPCaddie
    Created on : 23-nov.-2017, 13:28:38
    Author     : Morgan
--%>

<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page import="db.airport.models.Flight"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="web.application.tickets.TicketsControl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	Locale locale = (Locale) application.getAttribute(TicketsControl.LOCALE);
	ResourceBundle rb = ResourceBundle.getBundle(TicketsControl.BUNDLE, locale);
	HashMap<Flight, Integer> flights = (HashMap<Flight, Integer>) request.getAttribute(TicketsControl.FLIGHT_LIST);
	DateFormat df = new SimpleDateFormat("dd MMMM yyyy");
	String msg = (String) request.getAttribute("msg");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=rb.getString("CaddieTitle")%></title>
		<link rel="stylesheet" href="css/bootstrap.min.css">
    </head>
    <body class="container">
		<br />
		<form class="form-inline" method="POST" action="TicketsControl">
			<input type="hidden" name="action" value="<%=TicketsControl.BACK%>"/>
			<button class="btn btn-default" type="submit"><%=rb.getString("Back")%></button>
		</form>
        <h1><%=rb.getString("CaddieHeader")%></h1>
		<% if (msg != null)%> <p class="text-danger"><%=msg%></p>
		<form class="form-inline" method="POST" action="TicketsControl">
			<input type="hidden" name="action" value="<%=TicketsControl.PAY%>"/>
			<button class="btn btn-primary col-sm-offset-10" type="submit"><%=rb.getString("ToPay")%></button>
		</form>
		<br/>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th><%=rb.getString("Flight")%></th>
					<th><%=rb.getString("From")%></th>
					<th><%=rb.getString("To")%></th>
					<th><%=rb.getString("Departure")%></th>
					<th><%=rb.getString("Arrival")%></th>
					<th><%=rb.getString("Seats")%></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<% for (HashMap.Entry<Flight, Integer> entry : flights.entrySet()) {
						Flight f = entry.getKey();%>
				<tr>
					<td><%=f.getId()%></td>
					<td><%=f.getRefAirline()%></td>
					<td><%=f.getRefDestination()%></td>
					<td>
						<%=df.format(f.getDepartureDate())%> - <%=f.getDepartureTime()%>
					</td>
					<td><%=f.getEta()%></td>
					<td><%=entry.getValue()%></td>
					<td>
						<form class="form-inline" method="POST" action="TicketsControl">
							<input type="hidden" name="action" value="<%=TicketsControl.TAKE%>"/>
							<input type="hidden" name="<%=TicketsControl.FLIGHT%>" value="<%=f.getId()%>"/>
							<input type="hidden" name="<%=TicketsControl.NB_FREE%>" value="<%=entry.getValue()%>"/>
							<input class="form-control" type="number" name="<%=TicketsControl.NB_TAKE%>" placeholder="0"/>
							<button class="btn" type="submit"><%=rb.getString("Take")%></button>
						</form>
					</td>
				</tr>
				<% }%>
			</tbody>
		</table>
    </body>
</html>

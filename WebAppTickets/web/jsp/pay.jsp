<%--
    Document   : JSPPay
    Created on : 23-nov.-2017, 16:54:24
    Author     : Morgan
--%>

<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="db.airport.models.Flight"%>
<%@page import="web.application.tickets.TicketsControl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	Locale locale = (Locale) application.getAttribute(TicketsControl.LOCALE);
	ResourceBundle rb = ResourceBundle.getBundle(TicketsControl.BUNDLE, locale);
	String msg = (String) request.getAttribute("msg");
	String numId = (String) request.getAttribute(TicketsControl.NUM_ID);
	LinkedHashMap<Flight, Integer> flightsMap = (LinkedHashMap<Flight, Integer>) request.getAttribute(TicketsControl.TICKETS);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=rb.getString("PayTitle")%></title>
		<link rel="stylesheet" href="css/bootstrap.min.css">
    </head>
    <body class="container">
		<br />
		<form class="form-inline" method="POST" action="TicketsControl">
			<input type="hidden" name="action" value="<%=TicketsControl.BACK%>"/>
			<button class="btn btn-default" type="submit"><%=rb.getString("Back")%></button>
		</form>
		<br />
		<% if (msg != null) %> <p class="text-danger"><%=msg%></p>
		<form class="form-horizontal" method="POST" action="TicketsControl">
			<input type="hidden" name="action" value="<%=TicketsControl.PAY_DONE%>" />
			<h2><%=rb.getString("PayHeader")%></h2>
			<div class="form-group">
				<label class="control-label col-sm-3" for="numId"><%=rb.getString("IdNumber")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="text" name="<%=TicketsControl.NUM_ID%>"
						   id="numId" value="<%=(numId == null) ? "" : numId%>" required/>
				</div>
			</div>
			<br />
			<div class="form-group">
				<label class="control-label col-sm-3" for="card"><%=rb.getString("CardNumber")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="text" name="<%=TicketsControl.NUM_CARD%>" id="card" required />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3" for="owner"><%=rb.getString("CardOwner")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="text" name="<%=TicketsControl.OWNER_NAME%>" id="owner" required />
				</div>
			</div>
			<br/>
			<div class="form-group">
				<label class="control-label col-sm-3" for="email"><%=rb.getString("Email")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="email" name="<%=TicketsControl.E_MAIL%>" id="email" required />
				</div>
			</div>
			<button class="btn btn-primary col-sm-offset-3" type="submit"><%=rb.getString("Pay")%></button>
		</form>
		<h2><%=rb.getString("PayTickets")%></h2>
		<table class="table table-striped">
			<thead>
				<tr>
					<th><%=rb.getString("Flight")%></th>
					<th><%=rb.getString("Tickets")%></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<%
					for (Map.Entry<Flight, Integer> entry : flightsMap.entrySet()) {
						Flight f = entry.getKey();
				%>
				<tr>
					<td><%=f.toString()%></td>
					<td><%=entry.getValue()%></td>
					<td>
						<form class="form-inline" method="POST" action="TicketsControl">
							<input type="hidden" name="action" value="<%=TicketsControl.DELETE_T%>"/>
							<input type="hidden" name="<%=TicketsControl.FLIGHT%>" value="<%=f.getId()%>"/>
							<button type="submit" class="btn"><%=rb.getString("Delete")%></button>
						</form>
					</td>
				</tr>
				<% } %>
			</tbody>
		</table>
    </body>
</html>

<%-- 
    Document   : JSPInit
    Created on : 23-nov.-2017, 12:20:30
    Author     : Morgan
--%>

<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.ArrayList"%>
<%@page import="web.application.tickets.TicketsControl"%>
<%@page import="db.airport.models.Destination"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	Locale locale = (Locale) application.getAttribute(TicketsControl.LOCALE);
	ResourceBundle rb = ResourceBundle.getBundle(TicketsControl.BUNDLE, locale);
	ArrayList<Destination> destinations = (ArrayList<Destination>) application.getAttribute(TicketsControl.DEST_LIST);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=rb.getString("IndexTitle")%></title>
		<link rel="stylesheet" href="css/bootstrap.min.css">
    </head>
    <body class="container">
        <h1><%=rb.getString("Hello")%></h1>
        <h2><%=rb.getString("InitHeader")%></h2>
        <form class="form-inline" method="POST" action="TicketsControl">
            <input type="hidden" name="action" value="<%=TicketsControl.CHOICE_DEST%>" />
			<div class="form-group">
				<select name="dest" class="form-control">
					<% for (Destination d : destinations) {%>
					<option value="<%=d.getId()%>"><%=d.toString()%></option>
					<% }%>
				</select>
				<button type="submit" class="btn btn-primary"><%=rb.getString("Go")%></button>
			</div>
        </form>
    </body>
</html>

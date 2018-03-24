<%-- 
    Document   : new-client
    Created on : 30-nov.-2017, 8:58:29
    Author     : Morgan
--%>

<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<%@page import="db.airport.models.Client"%>
<%@page import="web.application.tickets.TicketsControl"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	Locale locale = (Locale) application.getAttribute(TicketsControl.LOCALE);
	ResourceBundle rb = ResourceBundle.getBundle(TicketsControl.BUNDLE, locale);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="css/bootstrap.min.css">
        <title><%=rb.getString("NewClientTitle")%></title>
    </head>
    <body class="container">
        <form class="form-horizontal" method="POST" action="TicketsControl">
			<h2><%=rb.getString("NewClientHeader")%></h2>
			<h3><%=rb.getString("Required")%></h3>
			<input type="hidden" name="action" value="<%=TicketsControl.NEW_CLIENT%>" />
			<input type="hidden" name="<%=TicketsControl.LOGIN %>" value="<%=request.getAttribute(TicketsControl.LOGIN)%>" />
			<input type="hidden" name="<%=TicketsControl.PWD %>" value="<%=request.getAttribute(TicketsControl.PWD)%>" />
			<div class="form-group">
				<label class="control-label col-sm-2" for="first-name"><%=rb.getString("FirstName")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="text" name="<%=TicketsControl.FIRST_NAME%>" id="first-name" required />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="last-name"><%=rb.getString("LastName")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="text" name="<%=TicketsControl.LAST_NAME%>" id="last-name" required />
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="age"><%=rb.getString("Age")%></label>
				<div class="col-sm-4">
					<input class="form-control" type="number" name="<%=TicketsControl.AGE%>" id="age" required />
				</div>
			</div>
			<div class="form-group">
				<label for="male-radio" class="radio-inline col-sm-offset-2" >
					<input type="radio" id="male-radio" name="<%=TicketsControl.GENDER%>" value="male" checked> <%=rb.getString("Man")%>
				</label>
				<label for="female-radio" class="radio-inline" >
					<input type="radio" id="female-radio" name="<%=TicketsControl.GENDER%>" value="female"> <%=rb.getString("Woman")%>
				</label>
			</div>
			<button class="btn btn-primary col-sm-offset-2" type="submit"><%=rb.getString("Create")%></button>
		</form>
    </body>
</html>

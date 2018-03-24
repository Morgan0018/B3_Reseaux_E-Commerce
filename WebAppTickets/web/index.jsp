<%-- 
    Document   : index
    Created on : 18-janv.-2018, 11:47:31
    Author     : Morgan
--%>

<%@page import="java.util.Locale"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="web.application.tickets.TicketsControl"%>
<%@page import="db.airport.models.Language"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	Locale locale = (Locale) application.getAttribute(TicketsControl.LOCALE);
	ResourceBundle rb = ResourceBundle.getBundle(TicketsControl.BUNDLE, locale);
	String msg = (String) request.getAttribute("msg");
	ArrayList<Language> languages = (ArrayList<Language>) application.getAttribute(TicketsControl.LANG_LIST);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%=rb.getString("IndexTitle")%></title>
		<link rel="stylesheet" href="css/bootstrap.min.css">
		<!--<link rel="stylesheet" href="css/signin.css" type="text/css"/>!-->
    </head>
    <body class="container">
		<br/>
		<form class="form-inline" method="POST" action="TicketsControl">
			<input type="hidden" name="action" value="<%=TicketsControl.LANG%>" />
			<label for="lang" class="sr-only"><%=rb.getString("Lang")%></label>
			<select name="lang" id="lang" class="form-control">
				<% for (Language l : languages) { %>
				<option value="<%=l.getId()%>"><%=l.getName()%></option>
				<% } %>
			</select>
			<button class="btn bg-primary" type="submit"><%=rb.getString("Change")%></button>
		</form>
		<br/>
		<% if (msg != null) %> <p class="text-danger text-center"><%=msg%></p>
        <form class="form-horizontal" method="POST" action="TicketsControl" >
			<h2><%=rb.getString("ConnectionHeader")%></h2>
            <input type="hidden" name="action" value="<%=TicketsControl.LOGIN%>" />
			<label for="login" class="sr-only"><%=rb.getString("Login")%></label>
			<input type="text" name="login" id="login" class="form-control" placeholder="Login" required />
			<label for="pwd" class="sr-only"><%=rb.getString("Password")%></label>
			<input type="password" name="pwd" id="pwd" class="form-control" placeholder="*******" required />
			<div class="checkbox">
				<label for="exists" class="checkbox">
					<input type="checkbox" name="exists" id="exists" /> <%=rb.getString("NewClient")%>
				</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit"><%=rb.getString("Connect")%></button>
		</form>
    </body>
</html>

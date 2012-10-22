<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	/*
	 * AWS Elastic Beanstalk checks your application's health by periodically
	 * sending an HTTP HEAD request to a resource in your application. By
	 * default, this is the root or default resource in your application,
	 * but can be configured for each environment.
	 *
	 * Here, we report success as long as the app server is up, but skip
	 * generating the whole page since this is a HEAD request only. You
	 * can employ more sophisticated health checks in your application.
	 */
	if (request.getMethod().equals("HEAD"))
	{
		return;
	}

	final String title = "Hello from Classy Games Server!";
%>

<!DOCTYPE html>

<html>

	<head>
		<meta charset="utf-8" />
		<style type="text/css">
			h1
			{
				text-align: center;
			}
		</style>
		<title><%= title %></title>
	</head>

	<body>
		<h1><%= title %></h1>
	</body>

</html>
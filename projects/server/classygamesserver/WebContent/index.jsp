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
%>

<!DOCTYPE html>

<html>

	<head>
		<meta charset="utf-8" />
		<style type="text/css">
			h1
			{
				color: #44BB44;
				text-align: center;
			}
		</style>
		<title>Hello from Classy Games Server!</title>
	</head>

	<body>
		<h1>Hello from Classy Games Server!</h1>
	</body>

</html>
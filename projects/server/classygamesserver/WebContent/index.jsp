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

<!doctype html>

<html>

	<head>
		<link href="assets/css/main.css" rel="stylesheet" type="text/css" />
		<link href="favicon.ico" rel="shortcut icon" />
		<meta charset="utf-8" />
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" type="text/javascript"></script>
		<script src="assets/js/main.js" type="text/javascript"></script>
		<title>Classy Games</title>
	</head>

	<body>
		<div id="logo"></div>
		<a href="https://github.com/ScootrNova/ClassyGames" target="_blank"><div id="github"></div></a>
		<a href="https://www.facebook.com/ClassyGamesCommunity" target="_blank"><div id="facebook"><img alt="Facebook" id="facebookImage" src="assets/img/facebook.png" />/ ClassyGamesCommunity</div></a>
		<a href="https://play.google.com/store" target="_blank"><div id="playStore"></div></a>
	</body>

</html>
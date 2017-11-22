<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="java.net.InetAddress"%>

<%
	InetAddress addr = InetAddress.getLocalHost();
	String ipStr = addr.getHostAddress().toString();
	out.println(ipStr);
%>
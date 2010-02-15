<%-- 
    Document   : editObject
    Created on : 04-dic-2009, 12:04:12
    Author     : Pablo
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="object.newObject.title" bundle="${lang}"/></title>
    </head>
    <body>
        <h1>Hello World!</h1>
    </body>
</html>

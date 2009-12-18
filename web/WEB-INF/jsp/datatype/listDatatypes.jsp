<%-- 
    Document   : dataTypeList
    Created on : 23-nov-2009, 10:46:56
    Author     : Pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<html>
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="datatypes.list.title" bundle="${lang}"/></title>
    </head>
    <body>
        <div>
            <h1><fmt:message key="datatypes.list.header" bundle="${lang}"/></h1>
            <c:choose>
                <c:when test="${empty datatypes}">
                    <fmt:message key="datatypes.list.emptyList" bundle="${lang}"/>
                </c:when>
                <c:otherwise>
                    <ul>
                        <c:forEach items="${datatypes}" var="dt">
                            <li>
                                <a href="<c:url value="/object/objectList.html"/>?id=<c:out value="${dt.name}"/>">
                                    <c:out value="${dt.name}"/>
                                </a>
                                <form action="<c:url value="/datatype/editDatatype.html"/>?id=<c:out value="${dt.name}"/>">
                                    <input type="submit" value="<fmt:message key="datatypes.list.editDatatype" bundle="${lang}"/>">
                                </form>
                                <form action="<c:url value="/datatype/deleteDatatype.html"/>?id=<c:out value="${dt.name}"/>">
                                    <input type="submit" value="<fmt:message key="datatypes.list.deleteDatatype" bundle="${lang}"/>">
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
        <div>
            <form action="<c:url value="/datatype/newDataType.html"/>">
                <input type="submit" value="<fmt:message key="nuevo" bundle="${lang}"/>"/>
            </form>
        </div>
    </body>
</html>

<%-- 
    Document   : objectList
    Created on : 17-dic-2009, 11:21:22
    Author     : Pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<c:set value="${pageContext.request.locale}" var="locale"/>
<c:set value="dd/MM/yyyy HH:mm" var="datePattern"/>
<c:choose>
    <c:when test="${locale eq 'en_US'}">
        <c:set value="MM/dd/yyyy HH:mm" var="datePattern"/>
    </c:when>
</c:choose>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="object.search.title" bundle="${lang}"/></title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/style.css"/>"/>
        <script type="text/javascript">
            function error() {
                var text = '<c:out value="${error}"/>';
                if (text.length > 0 ) alert(text);
            }

            function deactivate(state) {
                var date = document.getElementById("date");
                if (state) date.disabled = "disabled";
                else date.disabled = null;
                return true;
            }
        </script>
    </head>
    <body onload="javascript:error()">
        <div>
            <form action="<c:url value="/object/objectList.html"/>" method="post">
                <h1>MapStore</h1>
                <input type="text" name="query" value="<c:out value="${query}"/>" class="queryText"/><br/>
                <c:choose>
                    <c:when test="${date eq null}">
                        <input onchange="javascript:deactivate(true);" type="radio" name="fechaRadio" id="deactivate" value="current" checked="checked"><label for="deactivate"><fmt:message key="object.search.labelCurrent" bundle="${lang}"/></label><br/>
                        <input onchange="javascript:deactivate(false);"  type="radio" name="fechaRadio" id="activate" value="historic"><label for="activate"><fmt:message key="object.search.labelHistoric" bundle="${lang}"/></label>
                        <input type="text" name="date" id="date" disabled="disabled"/>
                    </c:when>
                    <c:otherwise>
                        <input onchange="javascript:deactivate(true);"  type="radio" name="fechaRadio" id="deactivate" value="current"><label for="deactivate"><fmt:message key="object.search.labelCurrent" bundle="${lang}"/></label>
                        <input onchange="javascript:deactivate(false);"  type="radio" name="fechaRadio" id="activate" value="historic" checked="checked"><label for="activate"><fmt:message key="object.search.labelHistoric" bundle="${lang}"/></label><br/>
                        <input type="text" name="date" id="date" value="<c:out value="${date}"/>"/>
                    </c:otherwise>
                </c:choose>
            </form>
        </div>
        <div>
            <c:choose>
                <c:when test="${empty query}">

                </c:when>
                <c:otherwise>
                    <h1><fmt:message key="object.search.result" bundle="${lang}"/></h1>
                    <table>
                        <thead>
                            <tr>
                                <th><fmt:message key="id" bundle="${lang}"/></th>
                                <th><fmt:message key="version" bundle="${lang}"/></th>                                
                                <th><fmt:message key="recordDate" bundle="${lang}"/>(<c:out value="${datePattern}"/>)</th>
                                <th><fmt:message key="name" bundle="${lang}"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty items}">
                                    <tr>
                                        <td colspan="0"><fmt:message key="object.search.emptyList" bundle="${lang}"/></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${items}" var="item">
                                        <tr>
                                            <td><c:out value="${item.id}"/></td>
                                            <td><c:out value="${item.version}"/></td>
                                            <td><fmt:formatDate value="${item.recordDate}" pattern="${datePattern}"/></td>
                                            <td>
                                                <a href="<<c:url value="/object/showObject.html"/>?id=<c:out value="${item.id}"/>&version=<c:out value="${item.version}"/>"/>"/>
                                                <c:out value="${item.value.name}"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
            <form >
                <input type="submit" value="<fmt:message key="object.search.newObject" bundle="${lang}"/>">
            </form>
        </div>
    </body>
</html>

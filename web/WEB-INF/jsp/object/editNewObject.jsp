<%-- 
    Document   : editNewObject
    Created on : 17-dic-2009, 18:56:08
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
        <link rel="stylesheet" href="<c:url value="/css/jquery.csspopup.css"/>" media="all" />
        <script type="text/javascript" src="<c:url value="/js/jquery.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/object/editObject.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/object/createObject.js"/>"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                    locale = '<c:out value="${pageContext.request.locale}"/>';
                    contextPath = '<c:url value="/"/>';
                    keyInternationalizedString = '<fmt:message key="object.newObject.key" bundle="${lang}"/>';
                    valueInternationalizedString = '<fmt:message key="object.newObject.value" bundle="${lang}"/>';
                    addToListInternationalizedString = '<fmt:message key="object.newObject.add" bundle="${lang}"/>';
                    addToMapInternationalizedString = '<fmt:message key="object.newObject.add" bundle="${lang}"/>';
                    addToObjectInternationalizedString = '<fmt:message key="object.newObject.add" bundle="${lang}"/>';
                    clearObjectInternationalizedString = '<fmt:message key="object.newObject.clear" bundle="${lang}"/>';
                    changeDatatype();
            });
        </script>

    </head>
    <body>
        <h1><fmt:message key="object.newObject.title" bundle="${lang}"/></h1>
        <c:choose>
            <c:when test="${empty datatypes}">
                <fmt:message key="object.newObject.noTypesDefined" bundle="${lang}"/>
            </c:when>
            <c:otherwise>
                <form id="form" action="<c:url value="/object/insertObject.html"/>">
                    <div id="header">
                        <select id="datatype" name="datatype">
                                <c:forEach items="${datatypes}" var="dt">
                                    <option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option>
                                </c:forEach>
                        </select><br/>
                        <label for="name"><fmt:message key="name" bundle="${lang}"/></label>
                        <input type="text" name="name" id="name"/><br/>
                    </div>
                    <hr/>
                    <div id="properties"></div>
                    <input type="submit" id="submit" value="<fmt:message key="object.newObject.createDataObject" bundle="${lang}"/>"/>
                </form>
            </c:otherwise>
        </c:choose>
    </body>
</html>

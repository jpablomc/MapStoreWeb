<%-- 
    Document   : editNewObject
    Created on : 17-dic-2009, 18:56:08
    Author     : Pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="object.newObject.title" bundle="${lang}"/></title>
        <script type="text/javascript" src="<c:url value="/ajax.js"/>"/>

        <script type="text/javascript">
            function generateProperties() {
                var dt = document.getElementById(datatype).value;
                var parameters = "datatype="+dt;
                var clazz = "es.uc3m.it.mapstore.web.controller.DataTypeController";
                var method = "getFormForNew";
                var ctx = "<c:url value="/"/>";
                request(ctx,clazz,method,parameters,createPropertiesForm);
            }

            function createPropertiesForm() {
                if (xmlhttp.readyState==4) {
                    document.getElementById("properties").innerHTML=xmlhttp.responseText;
                }
            }

            var nameProperty = null;
            function changeName() {
                if (nameProperty != null) {
                    nameProperty.value = document.getElementById("name").value;
                }
            }
        </script>
    </head>
    <body onload="javascript:generateProperties()">
        <h1><fmt:message key="object.newObject.title" bundle="${lang}"/></h1>
        <c:choose>
            <c:when test="${empty datatype}">
                <fmt:message key="object.newObject.noTypesDefined" bundle="${lang}"/>
            </c:when>
            <c:otherwise>
                <form>
                    <div id="header">
                        <select id="datatype" name=""datatype" onchange="javascript:generateProperties();">
                                <c:forEach items="${datatypes}" var="dt">
                                    <option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option>
                                </c:forEach>
                        </select><br/>
                        <label for="name"><fmt:message key="name" bundle="${lang}"/></label>
                        <input type="text" onchange="javascript:changeName()" name="name" id="name"/><br/>
                    </div>
                    <hr/>
                    <div id="properties"></div>
                </form>
            </c:otherwise>
        </c:choose>
    </body>
</html>

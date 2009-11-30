<%-- 
    Document   : editDataType
    Created on : 25-nov-2009, 11:37:22
    Author     : Pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<c:choose>
    <c:when test="${edit}">
        <fmt:message key="datatypes.edit.updateTitle" bundle="${lang}" var="title"/>
        <c:url var="action" value="/datatype/updateDataType.html"/>
    </c:when>
    <c:otherwise>
        <fmt:message key="datatypes.edit.createTitle" bundle="${lang}" var="title"/>
        <c:url var="action" value="/datatype/createDataType.html"/>
    </c:otherwise>
</c:choose>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${title}"/></title>

        <script type="text/javascript">
            var maxProperty = <c:out value="${fn:length(properties)}"/>+1;

            function error() {
                var text = '<c:out value="${error}"/>';
                if (text.length > 0 ) alert(text);
            }

            function addProperty() {

                var text = '<div id="property'+maxProperty+'">' +
                    '<label for="propertyName'+maxProperty+'"><fmt:message key="property" bundle="${lang}"/></label>' +
                    '<input type="text" id="propertyName'+maxProperty+'" name="propertyName'+maxProperty+'" value=""/>' +
                    '<label for="propertyType'+maxProperty+'"><fmt:message key="type" bundle="${lang}"/></label>'+
                    '<select name="propertyType'+maxProperty+'" id="propertyType'+maxProperty+'">'+
            <c:forEach items="${datatypes}" var="dt">'<option value="${dt.name}">${dt.name}</option>'+</c:forEach>
                    '</select>' +
                        '<input type="button" onclick="javascript:deleteProperty('+maxProperty+')" value="<fmt:message key="datatypes.edit.deleteProperty" bundle="${lang}"/>"/>'+
                        '<br/>'+
                        '</div>';
                    document.getElementById('properties').innerHTML += text;
                    maxProperty++;
                    return false; //Esto es para que no realize el envio del formulario
                }

                function deleteProperty(divId) {
                    var div = document.getElementById("property"+divId);
                    div.innerHTML = "";
                }
        </script>
    </head>
    <body onload="javascript:error()">
        <div>
            <h1><c:out value="${title}"/></h1>
        </div>
        <div>
            <form action="<c:out value="${action}"/>" id="form">
                <label for="name"><fmt:message key="name" bundle="${lang}"/></label>
                <input type="text" id="name" name="name" value="<c:out value='${name}'/>" <c:if test='${edit}'>readonly="readonly"</c:if>/><br/>
                <div id="properties">
                    <c:forEach items="${properties}" var="prop" varStatus="status">
                        <div id="property<c:out value='${status.count}'/>">
                            <label for="propertyName<c:out value='${status.count}'/>"><fmt:message key="property" bundle="${lang}"/></label>
                            <input type="text" id="propertyName<c:out value='${status.count}'/>" name="propertyName<c:out value='${status.count}'/>" value="<c:out value='${prop.key}'/>"/>
                            <label for="propertyType<c:out value='${status.count}'/>"><fmt:message key="type" bundle="${lang}"/></label>
                            ${prop.key} -->  ${prop.value}
                            <select name="propertyType<c:out value='${status.count}'/>" id="propertyType<c:out value='${status.count}'/>">
                                <c:forEach items="${datatypes}" var="dt">
                                    <c:choose>
                                        <c:when test="${dt.name eq prop.value.name}"><option selected="selected" value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:when>
                                        <c:otherwise><option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:otherwise>
                                    </c:choose>

                                </c:forEach>
                            </select>

                            <input type="button" onclick="javascript:deleteProperty(<c:out value='${status.count}'/>)" value="<fmt:message key="datatypes.edit.deleteProperty" bundle="${lang}"/>"/>
                            <br/>
                        </div>
                    </c:forEach>
                </div>
                <input type="button" onclick="javascript:addProperty()" value="<fmt:message key="datatypes.edit.addProperty" bundle="${lang}"/>"/><br/>
                <input type="submit" value="<fmt:message key="saveChanges" bundle="${lang}"/>"/><br/>
            </form>
        </div>
    </body>
</html>

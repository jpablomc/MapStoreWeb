<%-- 
    Document   : showObject
    Created on : 17-dic-2009, 11:19:56
    Author     : Pablo
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<c:set var="currentObject" value="${data.root}"/>
<c:set var="currentItem" value="${data[currentObject]}"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="object.showObject.title" bundle="${lang}"/></title>
    </head>
    <body>
        <div id="header">
            <h4><fmt:message key="object.showObject.name" bundle="${lang}"/>:<c:out value="${currentObject['_NAME']}"/></h4>
            <h5><fmt:message key="object.showObject.type" bundle="${lang}"/>:<c:out value="${currentObject['_TYPE']}"/></h5>
        </div>
        <div id="versions">
            <h6><fmt:message key="object.showObject.version" bundle="${lang}"/></h6>
            <c:set var="lastVersion" value="${data.lastVersion.version}"/>
            <c:set var="currentVersion" value="${currentItem.version}"/>
            <c:set var="id" value="${data.lastVersion.id}"/>
            <c:set var="cnt" value="0"/>
            <c:forEach begin="0" end="${lastVersion}" step="1" var="version">
                <c:choose>
                    <c:when test="${version eq currentVersion}">
                        <b>${version}</b>&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value="/object/showObject.html"/>?id=${id}&version=${version}">
                            ${version}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <hr/>
        <div id="data">
            <c:set var="datatype" value="${currentObject['_DATATYPE']}"/>
            <c:forEach var="attrib" items="${currentObject}">
                <c:choose>
                    <c:when test="${attrib.key == '_DATATYPE'}"/>
                    <c:when test="${attrib.key == '_NAME'}"/>
                    <c:when test="${attrib.key == '_TYPE'}"/>
                    <c:otherwise>
                        <c:out value="${attrib.key}"/>
                        <c:set var="dtProperty" value="${datatype.attributes[attrib.key]}"/>
                        <c:choose>
                            <c:when test="${dtProperty.name eq 'String' || dtProperty.name eq 'Integer' || dtProperty.name eq 'Long' || dtProperty.name eq 'Float' || dtProperty.name eq 'Double'}">
                                <input type="text" disabled="disabled" value="${attrib.value}"/><br/>
                            </c:when>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </body>
</html>

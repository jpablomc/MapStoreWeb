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

<div id="versions">
    <h6><fmt:message key="object.showObject.version" bundle="${lang}"/></h6>
    <c:forEach begin="0" end="${LASTVERSION}" step="1" var="version">
        <c:choose>
            <c:when test="${version eq CURRENTVERSION}">
                <b>${version}</b>&nbsp;
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/object/showObject.html"/>?id=${_ID}&version=${version}">
                    ${version}
                </a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>

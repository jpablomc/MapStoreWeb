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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value="/css/mapstore.css"/>" media="all" />
        <title><fmt:message key="object.showObject.title" bundle="${lang}"/></title>
    </head>
    <body>
        <div id="header">
            <h4><fmt:message key="object.showObject.name" bundle="${lang}"/>:<c:out value="${NAME}"/></h4>
            <h5><fmt:message key="object.showObject.type" bundle="${lang}"/>:<c:out value="${TYPE}"/></h5>
        </div>
        <jsp:include page="showObjectVersion.jsp"/>
        <hr/>
        <div id="data">
            <c:forEach var="property" items="${ATTRIB}">
                <b><c:out value="${property}"/>: </b>
                <c:set var="aux" value="TYPE_${property}"/>
                <c:set var="typeProperty" value="${requestScope[aux]}"/>
                <c:set var="propertyValue" value="${requestScope[property]}"/>
                <c:set var="index1" value=""/>
                <c:set var="index2" value=""/>
                <c:set var="index3" value=""/>
                <c:choose>
                    <c:when test="${typeProperty eq 'STRING'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            <c:if test="${status.count eq 3}"><c:set var="index3" value="${split}"/></c:if>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${index1 eq 'URL'}">
                                <a href="<c:out value="${index3}"/>"><c:out value="${index2}"/></a>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${index2}"/>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:when test="${typeProperty eq 'DOUBLE'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                        </c:forEach>
                        <c:out value="${index2}"/>
                    </c:when>
                    <c:when test="${typeProperty eq 'FLOAT'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                        </c:forEach>
                        <c:out value="${index2}"/>
                    </c:when>
                    <c:when test="${typeProperty eq 'INTEGER'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                        </c:forEach>
                        <c:out value="${index2}"/>
                    </c:when>
                    <c:when test="${typeProperty eq 'LONG'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                        </c:forEach>
                        <c:out value="${index2}"/>
                    </c:when>
                    <c:when test="${typeProperty eq 'DATE'}">
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                        </c:forEach>
                        <c:out value="${index2}"/>
                    </c:when>
                    <c:when test="${typeProperty eq 'LIST'}">
                        <div>
                            <table id="<c:out value="${property}"/>">
                                <thead>
                                    <tr>
                                        <th><fmt:message key="object.newObject.key" bundle="${lang}"/></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="sindex1" value=""/>
                                    <c:set var="sindex2" value=""/>
                                    <c:set var="sindex3" value=""/>
                                    <c:set var="sindex4" value=""/>
                                    <c:set var="aux" value="SUBTYPE_${property}"/>
                                    <c:set var="subtypeProperty" value="${requestScope[aux]}"/>
                                    <c:choose>
                                        <c:when test="${subtypeProperty eq 'STRING'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 3}"><c:set var="sindex3" value="${split}"/></c:if>
                                                </c:forEach>
                                                <c:choose>
                                                    <c:when test="${sindex1 eq 'URL'}">
                                                        <tr><td><a href="<c:out value="${sindex3}"/>"><c:out value="${sindex2}"/></a></td></tr>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <tr><td><c:out value="${sindex2}"/></td></tr>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${subtypeProperty eq 'DATE'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr><td><c:out value="${sindex2}"/></td></tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${subtypeProperty eq 'INTEGER'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr><td><c:out value="${sindex2}"/></td></tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${subtypeProperty eq 'LONG'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr><td><c:out value="${sindex2}"/></td></tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${subtypeProperty eq 'FLOAT'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr><td><c:out value="${sindex2}"/></td></tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:when test="${subtypeProperty eq 'DOUBLE'}">
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr><td><c:out value="${sindex2}"/></td></tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <%--Caso de objetos --%>
                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 3}"><c:set var="sindex3" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 4}"><c:set var="sindex4" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr>
                                                    <td>
                                                        <a href="<c:url value="/object/showObject.html"/>?id=<c:out value="${sindex2}"/>&version=<c:out value="${sindex3}"/>">
                                                            <c:out value="${sindex4}"/>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:when test="${typeProperty eq 'MAP'}">
                        <c:set var="aux" value="KEYTYPE_${property}"/>
                        <c:set var="keyTypeProperty" value="${requestScope[aux]}"/>
                        <c:set var="aux" value="VALUETYPE_${property}"/>
                        <c:set var="valueTypeProperty" value="${requestScope[aux]}"/>
                        <div>
                            <table id="<c:out value="${property}"/>">
                                <thead>
                                    <tr>
                                        <th><fmt:message key="object.newObject.key" bundle="${lang}"/></th>
                                        <th><fmt:message key="object.newObject.value" bundle="${lang}"/></th>
                                    </tr></thead>
                                <tbody>
                                    <c:forEach var="mapItem" items="${propertyValue}">
                                        <tr>
                                            <c:set var="sindex1" value=""/>
                                            <c:set var="sindex2" value=""/>
                                            <c:set var="sindex3" value=""/>
                                            <c:set var="sindex4" value=""/>
                                            <c:forEach var="split" items="${fn:split(mapItem.key, '|')}" varStatus="status">
                                                <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 3}"><c:set var="sindex3" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 4}"><c:set var="sindex4" value="${split}"/></c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${keyTypeProperty eq 'STRING'}">
                                                    <c:choose>
                                                        <c:when test="${sindex1 eq 'URL'}">
                                                            <td><a href="<c:out value="${sindex3}"/>"><c:out value="${sindex2}"/></a></td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td><c:out value="${sindex2}"/></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:when test="${keyTypeProperty eq 'INTEGER'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${keyTypeProperty eq 'LONG'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${keyTypeProperty eq 'FLOAT'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${keyTypeProperty eq 'DOUBLE'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${keyTypeProperty eq 'DATE'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>
                                                        <a href="<c:url value="/object/showObject.html"/>?id=<c:out value="${sindex2}"/>&version=<c:out value="${sindex3}"/>">
                                                            <c:out value="${sindex4}"/>
                                                        </a>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:set var="sindex1" value=""/>
                                            <c:set var="sindex2" value=""/>
                                            <c:set var="sindex3" value=""/>
                                            <c:set var="sindex4" value=""/>
                                            <c:forEach var="split" items="${fn:split(mapItem.value, '|')}" varStatus="status">
                                                <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 3}"><c:set var="sindex3" value="${split}"/></c:if>
                                                <c:if test="${status.count eq 4}"><c:set var="sindex4" value="${split}"/></c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${valueTypeProperty eq 'STRING'}">
                                                    <c:choose>
                                                        <c:when test="${sindex1 eq 'URL'}">
                                                            <td><a href="<c:out value="${sindex3}"/>"><c:out value="${sindex2}"/></a></td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td><c:out value="${sindex2}"/></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:when test="${valueTypeProperty eq 'INTEGER'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${valueTypeProperty eq 'LONG'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${valueTypeProperty eq 'FLOAT'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${valueTypeProperty eq 'DOUBLE'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:when test="${valueTypeProperty eq 'DATE'}">
                                                    <td><c:out value="${sindex2}"/></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>
                                                        <a href="<c:url value="/object/showObject.html"/>?id=<c:out value="${sindex2}"/>&version=<c:out value="${sindex3}"/>">
                                                            <c:out value="${sindex4}"/>
                                                        </a>
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- Caso de referencia a objeto --%>
                        <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                            <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                            <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            <c:if test="${status.count eq 3}"><c:set var="index3" value="${split}"/></c:if>
                            <c:if test="${status.count eq 4}"><c:set var="index4" value="${split}"/></c:if>
                        </c:forEach>
                        <a href="<c:url value="/object/showObject.html"/>?id=<c:out value="${index2}"/>&version=<c:out value="${index3}"/>">
                            <c:out value="${index4}"/>
                        </a>
                    </c:otherwise>
                </c:choose>
                <br/>
            </c:forEach>
        </div>
        <div>
            <form action="<c:url value="/object/editDataObject.html"/>" method="post">
                <input type="hidden" name="id" value="<c:out value="${_ID}"/>"/>
                <input type="submit" value="<fmt:message key="object.showObject.edit" bundle="${lang}"/>"/>
            </form>
        </div>
    </body>
</html>

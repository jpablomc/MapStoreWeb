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
        <link rel="stylesheet" href="<c:url value="/css/jquery.csspopup.css"/>" media="all" />
        <link rel="stylesheet" href="<c:url value="/css/mapstore.css"/>" media="all" />
        <script type="text/javascript" src="<c:url value="/js/jquery.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/object/editObject.js"/>"></script>
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
                errorOnInteger = '<fmt:message key="object.editObject.errorOnInteger" bundle="${lang}"/>';
                errorOnDecimal = '<fmt:message key="object.editObject.errorOnDecimal" bundle="${lang}"/>';
                errorOnDate = '<fmt:message key="object.editObject.errorOnDate" bundle="${lang}"/>';
                promptListString = '<fmt:message key="object.editObject.promptListString" bundle="${lang}"/>';
                promptListDecimal = '<fmt:message key="object.editObject.promptListDecimal" bundle="${lang}"/>';
                promptListInteger = '<fmt:message key="object.editObject.promptListInteger" bundle="${lang}"/>';
                promptListDate = '<fmt:message key="object.editObject.promptListDate" bundle="${lang}"/>';
                errorListDecimal = '<fmt:message key="object.editObject.errorListDecimal" bundle="${lang}"/>';
                errorListInteger = '<fmt:message key="object.editObject.errorListInteger" bundle="${lang}"/>';
                errorListDate = '<fmt:message key="object.editObject.errorListDate" bundle="${lang}"/>';
                deleteRowList = '<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>';
                errorMapKeyUndefined = '<fmt:message key="object.editObject.errorMapKeyUndefined" bundle="${lang}"/>';
                errorMapKeyNotInteger = '<fmt:message key="object.editObject.errorMapKeyInteger" bundle="${lang}"/>';
                errorMapKeyNotDecimal = '<fmt:message key="object.editObject.errorMapKeyDecimal" bundle="${lang}"/>';
                errorMapKeyNotDate = '<fmt:message key="object.editObject.errorMapKeyDate" bundle="${lang}"/>';
                errorMapValueNotInteger = '<fmt:message key="object.editObject.errorMapValueInteger" bundle="${lang}"/>';
                errorMapValueNotDecimal = '<fmt:message key="object.editObject.errorMapValueDecimal" bundle="${lang}"/>';
                errorMapValueNotDate = '<fmt:message key="object.editObject.errorMapValueDate" bundle="${lang}"/>';
                upRowList = '<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>';
                downRowList = '<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>';
                selecObjectInternationalizedString = '<fmt:message key="object.newObject.select" bundle="${lang}"/>';
            });
        </script>

        <title><fmt:message key="object.showObject.title" bundle="${lang}"/></title>
    </head>
    <body>
        <form id="form" action="<c:url value="/object/updateObject.html"/>" method="post">
            <div id="header">
                <h4><fmt:message key="object.showObject.name" bundle="${lang}"/>:<c:out value="${NAME}"/>
                    <input type="hidden" name="name" id="name" value="<c:out value="${NAME}"/>"/></h4>
                <h5><fmt:message key="object.showObject.type" bundle="${lang}"/>:<c:out value="${TYPE}"/>
                    <input type="hidden" name="datatype" id="datatype" value="<c:out value="${TYPE}"/>"/></h5>
            </div>
            <hr/>
            <div id="properties">
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
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${PK eq property}">
                                    <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="stringInput" readonly="readonly"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="stringInput"/>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:when test="${typeProperty eq 'DOUBLE'}">
                            <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="decimalInput"/>
                        </c:when>
                        <c:when test="${typeProperty eq 'FLOAT'}">
                            <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="decimalInput"/>
                        </c:when>
                        <c:when test="${typeProperty eq 'INTEGER'}">
                            <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="integerInput"/>
                        </c:when>
                        <c:when test="${typeProperty eq 'LONG'}">
                            <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                                <c:if test="${status.count eq 1}"><c:set var="index1" value="${split}"/></c:if>
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <c:out value="${index2}"/>
                            <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="integerInput"/>
                        </c:when>
                        <c:when test="${typeProperty eq 'DATE'}">
                            <c:forEach var="split" items="${fn:split(propertyValue, '|')}" varStatus="status">
                                <c:if test="${status.count eq 2}"><c:set var="index2" value="${split}"/></c:if>
                            </c:forEach>
                            <input type="text" id="<c:out value="${property}"/>" name="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="dateInput"/>
                        </c:when>
                        <c:when test="${typeProperty eq 'LIST'}">
                            <div>
                                <table id="<c:out value="${property}"/>">
                                    <thead>
                                        <tr>
                                            <th><fmt:message key="object.newObject.key" bundle="${lang}"/></th>
                                            <th></th>
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
                                                <c:set var="classList" value="addToListString"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${subtypeProperty eq 'DATE'}">
                                                <c:set var="classList" value="addToListDate"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${subtypeProperty eq 'INTEGER'}">
                                                <c:set var="classList" value="addToListInteger"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${subtypeProperty eq 'LONG'}">
                                                <c:set var="classList" value="addToListInteger"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${subtypeProperty eq 'FLOAT'}">
                                                <c:set var="classList" value="addToListDecimal"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:when test="${subtypeProperty eq 'DOUBLE'}">
                                                <c:set var="classList" value="addToListDecimal"/>
                                                <c:forEach var="listItem" items="${propertyValue}">
                                                    <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                        <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    </c:forEach>
                                                    <tr>
                                                        <td>
                                                            <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                            <span><c:out value="${sindex2}"/></span>
                                                        </td>
                                                        <td>
                                                            <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                                   class="upRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                                   class="deleteRow"/>
                                                            <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                                   class="downRow"/>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <%--Caso de objetos --%>
                                                <c:set var="classList" value="addToListObject"/>
                                            <script type="text/javascript">
                                                listPropertyName.push('<c:out value="${property}"/>');
                                                listPropertySubtype.push('<c:out value="${subtypeProperty}"/>');
                                            </script>

                                            <c:forEach var="listItem" items="${propertyValue}">
                                                <c:forEach var="split" items="${fn:split(listItem, '|')}" varStatus="status">
                                                    <c:if test="${status.count eq 1}"><c:set var="sindex1" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 2}"><c:set var="sindex2" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 3}"><c:set var="sindex3" value="${split}"/></c:if>
                                                    <c:if test="${status.count eq 4}"><c:set var="sindex4" value="${split}"/></c:if>
                                                </c:forEach>
                                                <tr>
                                                    <td>
                                                        <input type="hidden" value="<c:out value="${sindex2}"/>" name="<c:out value="${property}"/>"/>
                                                        <span><c:out value="${sindex4}"/></span>
                                                    </td>
                                                    <td>
                                                        <input type="button" value="<fmt:message key="object.editObject.upRowList" bundle="${lang}"/>"
                                                               class="upRow"/>
                                                        <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>"
                                                               class="deleteRow"/>
                                                        <input type="button" value="<fmt:message key="object.editObject.downRowList" bundle="${lang}"/>"
                                                               class="downRow"/>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                    </tbody>
                                </table>
                                <input type="button" value="<fmt:message key="object.newObject.add" bundle="${lang}"/>" class="<c:out value="${classList}"/>"/>
                            </div>
                        </c:when>
                        <c:when test="${typeProperty eq 'MAP'}">
                            <c:set var="aux" value="KEYTYPE_${property}"/>
                            <c:set var="keyTypeProperty" value="${requestScope[aux]}"/>
                            <c:set var="aux" value="VALUETYPE_${property}"/>
                            <c:set var="valueTypeProperty" value="${requestScope[aux]}"/>
                            <c:set var="keytype">
                                <c:choose>
                                    <c:when test="${keyTypeProperty eq 'STRING'}">String
                                    </c:when>
                                    <c:when test="${keyTypeProperty eq 'INTEGER'}">Integer
                                    </c:when>
                                    <c:when test="${keyTypeProperty eq 'LONG'}">Long
                                    </c:when>
                                    <c:when test="${keyTypeProperty eq 'FLOAT'}">Float
                                    </c:when>
                                    <c:when test="${keyTypeProperty eq 'DOUBLE'}">Double
                                    </c:when>
                                    <c:when test="${keyTypeProperty eq 'DATE'}">Date
                                    </c:when>
                                    <c:otherwise>
                                        ${keyTypeProperty}
                                    </c:otherwise>
                                </c:choose>
                            </c:set>
                            <c:set var="valuetype">
                                <c:choose>
                                    <c:when test="${valueTypeProperty eq 'STRING'}">String
                                    </c:when>
                                    <c:when test="${valueTypeProperty eq 'INTEGER'}">Integer
                                    </c:when>
                                    <c:when test="${valueTypeProperty eq 'LONG'}">Long
                                    </c:when>
                                    <c:when test="${valueTypeProperty eq 'FLOAT'}">Float
                                    </c:when>
                                    <c:when test="${valueTypeProperty eq 'DOUBLE'}">Double
                                    </c:when>
                                    <c:when test="${valueTypeProperty eq 'DATE'}">Date
                                    </c:when>
                                    <c:otherwise>
                                        ${valueTypeProperty}
                                    </c:otherwise>
                                </c:choose>
                            </c:set>


                                    <script type="text/javascript">
                                mapPropertyName.push('<c:out value="${property}"/>');
                                mapPropertyKeyType.push('<c:out value="${keytype}"/>');
                                mapPropertyValueType.push('<c:out value="${valuetype}"/>');
                            </script>
                            <div>
                                <table id="<c:out value="${property}"/>">
                                    <thead>
                                        <tr>
                                            <th><fmt:message key="object.newObject.key" bundle="${lang}"/></th>
                                            <th><fmt:message key="object.newObject.value" bundle="${lang}"/></th>
                                            <th></th>
                                        </tr></thead>
                                    <tbody>
                                        <c:forEach var="mapItem" items="${propertyValue}" varStatus="statusItem">
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
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${keyTypeProperty eq 'INTEGER'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${keyTypeProperty eq 'LONG'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${keyTypeProperty eq 'FLOAT'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${keyTypeProperty eq 'DOUBLE'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${keyTypeProperty eq 'DATE'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:otherwise>
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_key_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex4}"/></td>
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
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>                                                    </c:when>
                                                        <c:when test="${valueTypeProperty eq 'INTEGER'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${valueTypeProperty eq 'LONG'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${valueTypeProperty eq 'FLOAT'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>
                                                        </c:when>
                                                        <c:when test="${valueTypeProperty eq 'DOUBLE'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>                                                    </c:when>
                                                        <c:when test="${valueTypeProperty eq 'DATE'}">
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex2}"/></td>                                                    </c:when>
                                                        <c:otherwise>
                                                        <td><input type="hidden" name="<c:out value="${property}"/>_value_<c:out value="${statusItem.count-1}"/>" value="<c:out value="${sindex2}"/>"/><c:out value="${sindex4}"/></td>                                                    </c:otherwise>
                                                    </c:choose>
                                                <td>
                                                    <input type="button" value="<fmt:message key="object.editObject.deleteRowList" bundle="${lang}"/>" class="deleteRowMap"/>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                                <input type="button" value="<fmt:message key="object.newObject.add" bundle="${lang}"/>" class="addToMap"/>
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
                            <div><input type="hidden" name="<c:out value="${property}"/>" id="<c:out value="${property}"/>" value="<c:out value="${index2}"/>" class="hiddenObject"/>
                                <input type="text" disabled="disabled" class="visibleObject" value="<c:out value="${index4}"/>"/>
                                <input type="button" value="<fmt:message key="object.newObject.select" bundle="${lang}"/>" class="addToObject"/>
                                <input type="button" value="<fmt:message key="object.newObject.clear" bundle="${lang}"/>" class="clearObject"/>
                            </div>
                            <script type="text/javascript">
                                objectPropertyName.push('<c:out value="${property}"/>');
                                objectPropertyType.push('<c:out value="${typeProperty}"/>');
                            </script>

                        </c:otherwise>
                    </c:choose>
                    <br/>
                </c:forEach>
            </div>
            <input type="submit" id="submit" value="<fmt:message key="object.newObject.updateDataObject" bundle="${lang}"/>"/>
        </form>
    </body>
</html>

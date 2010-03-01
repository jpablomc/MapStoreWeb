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
<c:set var="propsLen" value = "${fn:length(properties)}"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value="/css/mapstore.css"/>" media="all" />
        <title><c:out value="${title}"/></title>

        <script type="text/javascript">
            var maxProperty = <c:out value="${propsLen}"/>+1;

            function error() {
                var text = '<c:out value="${error}"/>';
                if (text.length > 0 ) alert(text);
            }

            function addProperty() {
                var table = document.getElementById("properties");
                if (document.getElementById("emptyTable") != null) {
                    table.deleteRow(1);
                }
                var row = table.insertRow(table.rows.length);
                row.id = "property" + maxProperty;
                var c0 = row.insertCell(0);
                c0.innerHTML = '<input type="radio" name="pk" value="propertyName'+maxProperty+'"/>';
                var c1 = row.insertCell(1);
                c1.innerHTML = '<input type="text" id="propertyName'+maxProperty+'" name="propertyName'+maxProperty+'" value=""/>';
                var c2 = row.insertCell(2);
                c2.innerHTML = '<select name="propertyType'+maxProperty+'" id="propertyType'+maxProperty+'"  onchange="javascript:checkIndex('+maxProperty+');">'+
            <c:forEach items="${datatypes}" var="dt">'<option value="${dt.name}">${dt.name}</option>'+</c:forEach>
                    '</select>';
                    var c3 = row.insertCell(3);
                    c3.id = 'extraData'+ maxProperty;
                    var c4 = row.insertCell(4);
                    c4.innerHTML = '<input type="button" onclick="javascript:deleteProperty('+maxProperty+')" value="<fmt:message key="datatypes.edit.deleteProperty" bundle="${lang}"/>"/>';
                    checkIndex(maxProperty);
                    maxProperty++;
                    return false; //Esto es para que no realize el envio del formulario
                }

                function getMapText(index) {
                    var text = '<label for="propertyMapKeyType'+index+'"><fmt:message key="datatypes.edit.mapKeyLabel" bundle="${lang}"/></label>'+
                        '<select name="propertyMapKeyType'+index+'" id="propertyMapKeyType'+index+'">'+
                        '<option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>'+
            <c:forEach items="${datatypes}" var="dt"><c:choose>
                    <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                    <c:otherwise>'<option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option>'+</c:otherwise></c:choose>
            </c:forEach>
                    '</select>'+
                        '<label for="propertyMapValueType'+index+'"><fmt:message key="datatypes.edit.mapValueLabel" bundle="${lang}"/></label>'+
                        '<select name="propertyMapValueType'+index+'" id="propertyMapValueType'+index+'">'+
                        '<option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>'+
            <c:forEach items="${datatypes}" var="dt"><c:choose>
                    <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                    <c:otherwise>'<option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option>'+</c:otherwise></c:choose>
            </c:forEach>
                    '</select>';
                    return text;
                }

                function getListText(index) {
                    var text = '<label for="propertyListType'+index+'"><fmt:message key="datatypes.edit.listTypeLabel" bundle="${lang}"/></label>'+
                        '<select name="propertyListType'+index+'" id="propertyListType'+index+'">'+
                        '<option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>'+
            <c:forEach items="${datatypes}" var="dt"><c:choose>
                    <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                    <c:otherwise>'<option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option>'+</c:otherwise></c:choose>
            </c:forEach>
                    '</select>';
                    return text;
                }

                function deleteProperty(divId) {
                    var table = document.getElementById("properties");
                    for (i = 0;i<table.rows.length;i++) {
                        if (table.rows[i].id == "property"+divId) {
                            table.deleteRow(i);
                            break;
                        }
                    }
                    if (table.rows.length == 1) {
                        var row = table.insertRow(1);
                        row.id = "emptyTable";
                        var cell = row.insertCell(0);
                        cell.innerHTML = '<fmt:message key="datatypes.edit.noProperties" bundle="${lang}"/>';
                        cell.colSpan = 0;
                    }
                }

                function checkIndex(index) {
                    var text = "";
                    if (isList(index)) {
                        text = getListText(index);
                    } else if (isMap(index)) {
                        text = getMapText(index);
                    }
                    var div = document.getElementById("extraData"+index);
                    if (div != null) div.innerHTML = text;
                }

                function isList(index) {
                    var isList = false;
                    var select = document.getElementById("propertyType" +index);
                    if (select != null) {
                        var value = select.options[select.selectedIndex].value;
            <c:forEach items="${constant.LISTTYPES}" var="datatypes">
                        if (value == "<c:out value="${datatypes}"/>") isList =true;
            </c:forEach>
                    }
                    return isList;
                }

                function isMap(index) {
                    var isMap = false;
                    var select = document.getElementById("propertyType" +index);
                    if (select != null) {
                        var value = select.options[select.selectedIndex].value;
            <c:forEach items="${constant.MAPTYPES}" var="datatypes">
                        if (value == "<c:out value="${datatypes}"/>") isMap =true;
            </c:forEach>
                    }
                    return isMap;
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
                <table id="properties">
                    <thead>
                        <tr>
                            <th><fmt:message key="primary_key" bundle="${lang}"/></th>
                            <th><fmt:message key="property" bundle="${lang}"/></th>
                            <th><fmt:message key="type" bundle="${lang}"/></th>
                            <th><fmt:message key="datatypes.edit.extraLabel" bundle="${lang}"/></th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${propsLen == 0}">
                                <tr id="emptyTable"><td colspan="0"><fmt:message key="datatypes.edit.noProperties" bundle="${lang}"/></td></tr>
                            </c:when>                
                            <c:otherwise>
                                <c:forEach items="${properties}" var="prop" varStatus="status">
                                    <tr id="property<c:out value='${status.count}'/>">
                                        <td>
                                            <c:choose>
                                                <c:when test="${pk eq prop.key}"><input type="radio" name="pk" value="propertyName<c:out value='${status.count}'/>" checked="checked"/></c:when>
                                                <c:otherwise><input type="radio" name="pk" value="propertyName<c:out value='${status.count}'/>"/></c:otherwise>
                                            </c:choose>                                            
                                        </td>
                                        <td>
                                            <input type="text" id="propertyName<c:out value='${status.count}'/>" name="propertyName<c:out value='${status.count}'/>" value="<c:out value='${prop.key}'/>"/>
                                        </td>
                                        <td>
                                            <select name="propertyType<c:out value='${status.count}'/>" id="propertyType<c:out value='${status.count}'/>" onchange="javascript:checkIndex(<c:out value='${status.count}'/>);">
                                                <c:forEach items="${datatypes}" var="dt">
                                                    <c:choose>
                                                        <c:when test="${dt.name eq prop.value.name}"><option selected="selected" value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:when>
                                                        <c:otherwise><option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:otherwise>
                                                    </c:choose>

                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td id="extraData<c:out value='${status.count}'/>">
                                            <c:set var="isList" value="false"/>
                                            <c:set var="isMap" value="false"/>
                                            <c:forEach items="${constant.LISTTYPES}" var="listDatatype">
                                                <c:if test="${listDatatype eq prop.value.name}">
                                                    <c:set var="isList" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:forEach items="${constant.MAPTYPES}" var="mapDatatype">
                                                <c:if test="${mapDatatype eq prop.value.name}">
                                                    <c:set var="isMap" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${isList}">
                                                    <c:set var="aux" value="${constant.LIST_PREFIX}${prop.key}"/>
                                                    <c:set var="listType" value="${extraData[aux]}"/>
                                                    <label for="propertyListType<c:out value='${status.count}'/>"><fmt:message key="datatypes.edit.listTypeLabel" bundle="${lang}"/></label>
                                                    <select name="propertyListType<c:out value='${status.count}'/>" id="propertyListType<c:out value='${status.count}'/>">
                                                        <option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>
                                                        <c:forEach items="${datatypes}" var="dt">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                                                                    <c:otherwise>
                                                                        <c:choose>
                                                                            <c:when test="${dt.name eq listType.name}"><option selected="selected" value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:when>
                                                                            <c:otherwise><option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:otherwise>
                                                                        </c:choose>
                                                                    </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </select>
                                                </c:when>
                                                <c:when test="${isMap}">
                                                    <c:set var="aux" value="${constant.MAP_KEY__PREFIX}${prop.key}"/>
                                                    <c:set var="mapKeyType" value="${extraData[aux]}"/>
                                                    <c:set var="aux" value="${constant.MAP_VALUE_PREFIX}${prop.key}"/>
                                                    <c:set var="mapValueType" value="${extraData[aux]}"/>
                                                    <label for="propertyMapKeyType<c:out value='${status.count}'/>"><fmt:message key="datatypes.edit.mapKeyLabel" bundle="${lang}"/></label>
                                                    <select name="propertyMapKeyType<c:out value='${status.count}'/>" id="propertyMapKeyType<c:out value='${status.count}'/>">
                                                        <option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>
                                                        <c:forEach items="${datatypes}" var="dt">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${dt.name eq mapKeyType.name}"><option selected="selected" value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:when>
                                                                        <c:otherwise><option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </select>
                                                    <label for="propertyMapValueType<c:out value='${status.count}'/>"><fmt:message key="datatypes.edit.mapValueLabel" bundle="${lang}"/></label>
                                                    <select name="propertyMapValueType<c:out value='${status.count}'/>" id="propertyMapValueType<c:out value='${status.count}'/>">
                                                        <option value="" ><fmt:message key="datatypes.edit.undetermined" bundle="${lang}"/></option>
                                                        <c:forEach items="${datatypes}" var="dt">
                                                            <c:choose>
                                                                <c:when test="${fn:contains(constant.LISTTYPES,dt.name)}"></c:when><c:when test="${fn:contains(constant.MAPTYPES,dt.name)}"></c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when test="${dt.name eq mapValueType.name}"><option selected="selected" value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:when>
                                                                        <c:otherwise><option value="<c:out value="${dt.name}"/>"><c:out value="${dt.name}"/></option></c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    </select>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <input type="button" onclick="javascript:deleteProperty(<c:out value='${status.count}'/>)" value="<fmt:message key="datatypes.edit.deleteProperty" bundle="${lang}"/>"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </tbody>
                </table>


                <div id="properties">
                </div>
                <input type="button" onclick="javascript:addProperty()" value="<fmt:message key="datatypes.edit.addProperty" bundle="${lang}"/>"/><br/>
                <input type="submit" value="<fmt:message key="saveChanges" bundle="${lang}"/>"/><br/>
            </form>
        </div>
    </body>
</html>

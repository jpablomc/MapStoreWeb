<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<div style="width:100%; height:100%; background-color:white;" id="mainMap">
    <h6><fmt:message key="object.popupMap.key" bundle="${lang}"/></h6>
    <div id="divMapPopUpKey">
        <c:choose>
            <c:when test="${keyClass == 'BASIC'}">
                <c:set var="type" value="${typeKey}" scope="request"/>
                <jsp:include page="popupMapBasic.jsp"/>
            </c:when>
            <c:when test="${keyClass == 'FILE'}">
                <jsp:include page="popupMapFile.jsp"/>
            </c:when>
            <c:otherwise>
                <c:set var="type" value="${typeKey}" scope="request"/>
                <c:set var="query" value="${queryKey}" scope="request"/>
                <c:set var="items" value="${itemsKey}" scope="request"/>
                <c:set var="keyOrValue" value="_key" scope="request"/>
                <jsp:include page="popupMapComplex.jsp"/>
            </c:otherwise>                            
        </c:choose>
    </div>
    <h6><fmt:message key="object.popupMap.value" bundle="${lang}"/></h6>
    <div id="divMapPopUpValue">
        <c:choose>
            <c:when test="${valueClass == 'BASIC'}">
                <c:set var="type" value="${typeValue}" scope="request"/>
                <jsp:include page="popupMapBasic.jsp"/>
            </c:when>
            <c:when test="${valueClass == 'FILE'}">
                <jsp:include page="popupMapFile.jsp"/>
            </c:when>
            <c:otherwise>
                <c:set var="type" value="${typeValue}" scope="request"/>
                <c:set var="query" value="${queryValue}" scope="request"/>
                <c:set var="items" value="${itemsValue}" scope="request"/>
                <c:set var="keyOrValue" value="_value" scope="request"/>
                <jsp:include page="popupMapComplex.jsp"/>
            </c:otherwise>
        </c:choose>
    </div>
    <input type="button" class="popupMapAccept" value="<fmt:message key="object.popupMap.accept" bundle="${lang}"/>"
    <input type="button" class="popupMapCancel" value="<fmt:message key="object.popupMap.cancel" bundle="${lang}"/>"

</div>

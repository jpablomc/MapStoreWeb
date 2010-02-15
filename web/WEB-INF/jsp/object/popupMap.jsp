<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<div style="width:100%; height:100%; background-color:white;" id="mainMap">
    <h6><fmt:message key="object.popupMap.key" bundle="${lang}"/></h6>
    <div class="divMapPopUpKey">
        <c:choose>
            <c:when test="${isKeyBasic}">
                <c:set property="type" value="${typeKey}"/>
                <jsp:include page="/object/popupMapBasic.jsp"/>
            </c:when>
            <c:otherwise>
                <c:set property="items" value="${itemsKey}"/>
                <jsp:include page="/object/popupMapBasic.jsp"/>
            </c:otherwise>                            
        </c:choose>
    </div>
    <h6><fmt:message key="object.popupMap.value" bundle="${lang}"/></h6>
    <div class="divMapPopUpvalue">
        <c:choose>
            <c:when test="${isValueBasic}">
                <c:set property="type" value="${typeValue}"/>
                <jsp:include page="/object/popupMapBasic.jsp"/>
            </c:when>
            <c:otherwise>
                <c:set property="items" value="${itemsValue}"/>
                <jsp:include page="/object/popupMapBasic.jsp"/>
            </c:otherwise>
                            
        </c:choose>
    </div>
    <input type="button" class="popupMaptAccept" value="<fmt:message key="object.popupMap.accept" bundle="${lang}"/>"
    <input type="button" class="popupMapCancel" value="<fmt:message key="object.popupMap.cancel" bundle="${lang}"/>"

</div>

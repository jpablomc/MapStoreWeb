<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<div class="basic">
    <c:choose>
        <c:when test="${type eq 'String'}">
            <input type="text" value="" name="basic" class="basicString"/>
        </c:when>
        <c:when test="${type eq 'Integer' || type eq 'Long'}">
            <input type="text" value="" name="basic" class="basicInteger"/>
        </c:when>
        <c:when test="${type eq 'Float' || type eq 'Double'}">
            <input type="text" value="" name="basic" class="basicDecimal"/>
        </c:when>
        <c:otherwise>
            <input type="text" value="" name="basic" class="basicDate"/>
        </c:otherwise>
    </c:choose>   
</div>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>
<div style="width:100%; height:100%; background-color:white;">
    <h6><fmt:message key="object.popupObject.header" bundle="${lang}"/></h6>
                    <table>
                        <thead>
                            <tr>
                                <th></th>
                                <th><fmt:message key="id" bundle="${lang}"/></th>
                                <th><fmt:message key="version" bundle="${lang}"/></th>
                                <th><fmt:message key="recordDate" bundle="${lang}"/>(<c:out value="${datePattern}"/>)</th>
                                <th><fmt:message key="type" bundle="${lang}"/></th>
                                <th><fmt:message key="name" bundle="${lang}"/></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty items}">
                                    <tr>
                                        <td colspan="0"><fmt:message key="object.search.emptyList" bundle="${lang}"/></td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach items="${items}" var="item">
                                        <tr>
                                            <td><input type="radio" name="selected" value="${item.id}|${item.type}|${item.name}"</td>
                                            <td><c:out value="${item.id}"/></td>
                                            <td><c:out value="${item.version}"/></td>
                                            <td><fmt:formatDate value="${item.recordDate}" pattern="${datePattern}"/></td>
                                            <td><c:out value="${item.type}"/></td>
                                            <td><c:out value="${item.name}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                            <input type="button" class="popupObjectAccept" value="<fmt:message key="object.popupObject.accept" bundle="${lang}"/>"
                            <input type="button" class="popupObjectCancel" value="<fmt:message key="object.popupObject.cancel" bundle="${lang}"/>"
</div>
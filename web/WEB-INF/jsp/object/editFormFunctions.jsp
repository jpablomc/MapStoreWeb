<%-- 
    Document   : editFormFunctions
    Created on : 17-dic-2009, 22:20:39
    Author     : Pablo
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setBundle basename="es.uc3m.it.mapstore.web.internationalization.ViewInternationalization" var="lang"/>

<script type="text/javascript">
    function listAddString(property) {
        var text = <fmt:message key="object.newObject.listAddString" bundle="${lang}"/>
        var value=prompt(text,"");
        var table = document.getElementById(property);
        var index = getIndex(table)+1;
        var row = table.insertRow();
        row.id = property+ "_"+ index;
        var c1 = row.insertCell(0);
        c1.innerHTML = '<input type="text" value="'+value+'" name="'+property+'"/>';
        var c2 = row.insertCell(1);
        c1.innerHTML = '<input type="button" value="<fmt:message key="delete" bundle="${lang}"/>" onclick="javascript:deleteRow('+row.id+')"/>';

    }

    function getIndex(table) {
        var row = table.rows[table.rows.length];
        var id = row.id;
        var index = id.lastIndexOf('_');
        return id.substr(index, id.length);
    }
</script>
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var xmlhttp;

function request(contextPath,clazz,method,param,responseFuntion) {
    xmlhttp=GetXmlHttpObject();
    if (xmlhttp==null) {
        alert ("Your browser does not support XMLHTTP!");
        return;
    }
    var url = contextPath+ "/ajax.ajax?" + "clazz=" + clazz + "&method=" + method + "&" + param;
    xmlhttp.onreadystatechange= responseFuntion;
    xmlhttp.open("GET",url,true);
    xmlhttp.send(null);
}

function GetXmlHttpObject() {
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        return new XMLHttpRequest();
    }
    if (window.ActiveXObject){
        // code for IE6, IE5
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
    return null;
}

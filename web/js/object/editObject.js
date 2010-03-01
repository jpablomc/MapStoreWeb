/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var locale;
var contextPath;
var errorOnInteger;
var errorOnDecimal;
var errorOnDate;
var promptListString;
var promptListDecimal;
var promptListInteger;
var promptListDate;
var errorListDecimal;
var errorListInteger;
var errorListDate;
var deleteRowList;
var errorMapKeyUndefined;
var errorMapKeyNotInteger;
var errorMapKeyNotDecimal;
var errorMapKeyNotDate;
var errorMapValueNotInteger;
var errorMapValueNotDecimal;
var errorMapValueNotDate;
var upRowList;
var downRowList;
var selecObjectInternationalizedString;

var objectPropertyName = new Array();
var objectPropertyType = new Array();
var listPropertyName = new Array();
var listPropertySubtype = new Array();
var mapPropertyName = new Array();
var mapPropertyKeyType = new Array();
var mapPropertyValueType = new Array();

$(function(){
//    $('.integerInput').live('focusout',function(e) {
//        var event = e;
//        var integerStr = /\d+/i;
//        var value = $(this).attr('value');
//        if (value.length > 0) {
//            var result = integerStr.exec(value);
//            if (result == null || result.length != 1 || result[0].length != value.length) {
//                $(this).focus();
//            }
//        }
//    })
    $('#form').submit(function() {
        var valid = validateForm();
        if (valid) {
            return true;
        }
        return false;
    })

    //METODOS PARA ASOCIAR UN OBJETO
    $('.addToObject').live('click',function() {
        var inputVisible = $(this).parent().find('.visibleObject');
        var inputHidden = $(this).parent().find('.hiddenObject');
        var property = inputHidden.attr('name');
        var type;
        var i;
        for (i=0;i<objectPropertyName.length;i++) {
            if (objectPropertyName[i] == property) {
                type = objectPropertyType[i];
            }
        }
        showPopUpForObject(type,inputVisible,inputHidden);
    })

    $('.clearObject').live('click',function() {
        var inputVisible = $(this).parent().find('.visibleObject');
        var inputHidden = $(this).parent().find('.hiddenObject');
        inputVisible.attr('value', '');
        inputHidden.attr('value', '');
    })

    $('.popupObjectAccept').live('click',function() {
        $("input[name='selected']").each( function() {
            var selected = $(this).attr('checked');
            if (selected) {
                var value = $(this).attr('value');
                var data = value.split("|", 3);
                popUpForObjectInputHidden.attr('value',data[0]);
                popUpForObjectInputVisible.attr('value',data[1]+": "+data[2]);
            }
        })
        resolvePopUpForObject();
    })

    $('.popupObjectCancel').live('click',function() {
        resolvePopUpForObject();
    })

    //METODOS PARA LOS TIPOS LISTA
    $('.addToListString').live('click',function() {
        var result = prompt(promptListString,"");
        if (result.length>0) {
            //No hay validacion
            //Introducir en la tabla
            var property = $(this).parent().find("table").attr('id');
            var tbody = $(this).parent().find("tbody"); //tenemos el cuerpo de la tabla            
            tbody.append('<tr><td><input type="hidden" value='+result+
                ' name="'+property+'"/><span>'+result+'</span></td><td><input type="button" value="'+ upRowList +
                '" class="upRow"/><input type="button" value="'+ deleteRowList +
                '" class="deleteRow"/><input type="button" value="'+ downRowList +
                '" class="downRow"/></td></tr>')
        }
    })

    $('.addToListDecimal').live('click',function() {
        var result = prompt(promptListDecimal,"");
        if (result.length>0) {
            //Validar
            if (validateDecimal(result)) {
                //Introducir en la tabla
                var property = $(this).parent().find("table").attr('id');
                var tbody = $(this).parent().find("tbody"); //tenemos el cuerpo de la tabla
                tbody.append('<tr><td><input type="hidden" value='+result+
                    ' name="'+property+'"/><span>'+result+'</span></td><td><input type="button" value="'+ upRowList +
                    '" class="upRow"/><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/><input type="button" value="'+ downRowList +
                    '" class="downRow"/></td></tr>')
            } else alert (errorListDecimal);
        }
    })

    $('.addToListInteger').live('click',function() {
        var result = prompt(promptListDecimal,"");
        if (result.length>0) {
            //Validar
            if (validateInteger(result)) {
                //Introducir en la tabla
                var property = $(this).parent().find("table").attr('id');
                    var tbody = $(this).parent().find("tbody"); //tenemos el cuerpo de la tabla
                    tbody.append('<tr><td><input type="hidden" value='+result+
                    ' name="'+property+'"/><span>'+result+'</span></td><td><input type="button" value="'+ upRowList +
                    '" class="upRow"/><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/><input type="button" value="'+ downRowList +
                    '" class="downRow"/></td></tr>')
        } else alert (errorListDecimal);
        }
    })

    $('.addToListDate').live('click',function() {
        var result = prompt(promptListDecimal,"");
        if (result.length>0) {
            //Validar
            if (validateDate(result)) {
                //Introducir en la tabla
                var property = $(this).parent().find("table").attr('id');
                var tbody = $(this).parent().find("tbody"); //tenemos el cuerpo de la tabla
                tbody.append('<tr><td><input type="hidden" value='+result+
                    ' name="'+property+'"/><span>'+result+'</span></td><td><input type="button" value="'+ upRowList +
                    '" class="upRow"/><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/><input type="button" value="'+ downRowList +
                    '" class="downRow"/></td></tr>')
            } else alert (errorListDecimal);
        }
    })

    $('.upRow').live('click',function() {
        var $thisRow = $(this).parents('tr:first');
        $thisRow.insertBefore( $thisRow.prev() );
    })

    $('.downRow').live('click',function() {
        var $thisRow = $(this).parents('tr:first');
        $thisRow.insertAfter( $thisRow.next() );
    })

    $('.deleteRow').live('click',function() {
        var tr = $(this).parent().parent();
        tr.remove();
    })

    //Metodos para la lista de ficheros
    $('.addToListFile').live('click',function() {
        //determinar tipo de la lista
        var property = $(this).parent().find("table").attr('id');
        showPopUpForFileList(property);
    })


    //Metodos para la lista de objetos
    $('.addToListObject').live('click',function() {
        //determinar tipo de la lista
        var property = $(this).parent().find("table").attr('id');
        var type;
        var i;
        for (i=0;i<listPropertyName.length;i++) {
            if (listPropertyName[i] == property) {
                type = listPropertySubtype[i];
            }
        }
        showPopUpForObjectList(type,property);
    })

    $('.popupObjectListAccept').live('click',function() {
        //Determinar el tbody
        var tbody = $("#"+popUpForObjectListProperty).find("tbody");
        $("input[name='selected']").each( function() {
            var selected = $(this).attr('checked');
            if (selected) {
                var value = $(this).attr('value');
                var data = value.split("|", 3);
                tbody.append('<tr><td><input type="hidden" value="'+ data[0] +
                    '" name="'+popUpForObjectListProperty+'"/><span>'
                    +data[1]+': ' + data[2] + '</span></td><td><input type="button" value="'+ upRowList +
                    '" class="upRow"/><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/><input type="button" value="'+ downRowList +
                    '" class="downRow"/></td></tr>')
            }
        })
        resolvePopUpForObjectList();
    })

    $('.popupObjectListCancel').live('click',function() {
        resolvePopUpForObjectList();
    })

    $('.addToMap').live('click',function() {
        var tbody = $(this).parent().find("tbody");
        var count = tbody.find("tr").length;
        var property = $(this).parent().find("table").attr('id');
        var typeKey;
        var typeValue;
        var i;
        for (i=0;i<mapPropertyName.length;i++) {
            if (mapPropertyName[i] == property) {
                typeKey = mapPropertyKeyType[i];
                typeValue = mapPropertyValueType[i];
            }
        }
        if (typeKey == "File") {
            text = '<tr><td><input type="file" name="' + property + '_key_' +count+ '"/></td>';
        } else if (typeKey == "String") {
            text = '<tr><td><input type="text" name="' + property + '_key_' +count+ '" class="stringInput"/></td>';
        } else if (typeKey == "Date") {
            text = '<tr><td><input type="text" name="' + property + '_key_' +count+ '" class="dateInput"/></td>';
        } else if (typeKey == "Integer" || typeKey == "Long") {
            text = '<tr><td><input type="text" name="' + property + '_key_' +count+ '" class="integerInput"/></td>';
        } else if (typeKey == "Double" || typeKey == "Float") {
            text = '<tr><td><input type="text" name="' + property + '_key_' +count+ '" class="decimalInput"/></td>';
        } else {
            text = '<tr><td><input type="hidden" name="' + property + '_key_' +count+ '"/>'+
                '<input type="text" disabled="disabled"/>'+
                '<input type="button" value="'+addToListInternationalizedString+'" class="searchForMapObject/></td>'
        }

        if (typeValue == "File") {
            text += '<td><input type="file" name="' + property + '_value_' +count+ '"/></td>';
        } else if (typeValue == "String") {
            text += '<td><input type="text" name="' + property + '_value_' +count+ '" class="stringInput"/></td>';
        } else if (typeValue == "Date") {
            text += '<td><input type="text" name="' + property + '_value_' +count+ '" class="dateInput"/></td>';
        } else if (typeValue == "Integer" || typeValue == "Long") {
            text += '<td><input type="text" name="' + property + '_value_' +count+ '" class="integerInput"/></td>';
        } else if (typeValue == "Double" || typeValue == "Float") {
            text += '<td><input type="text" name="' + property + '_value_' +count+ '" class="decimalInput"/></td>';
        } else {
            text += '<td><input type="hidden" name="' + property + '_value_' +count+ '"/>'+
                '<input type="text" disabled="disabled"/>'+
                '<input type="button" value="'+addToListInternationalizedString+'" class="searchForMapObject"/></td>'
        }
        text += '<td><input type="button" value="'+ deleteRowList + '" class="deleteRowMap"/></td></tr>';
        $(tbody).append(text);

    });

    $('.searchForMapObject').live('click',function() {
        var table = $(this).parents("table:first");
        var td = $(this).parent();
        //Determine if it is key or value
        var col = $(td).parent().children().index($(this));
        var array;
        if (col == 1) array = mapPropertyKeyType;
        else array = mapPropertyValueType;
        var property = $(table).attr("id");
        var type;
        var i;

        for (i=0;i<mapPropertyName.length;i++) {
            if (mapPropertyName[i] == property) {
                type = array[i];
            }
        }
        showPopUpForMap(type, td);
    });

    $('.searchMap').live('click',function() {
        var div = $(this).parent();
        var type = $(div).find('input[name="type"]').attr("value");
        var query = $(div).find('input[name="query"]').attr("value");
        var URL =  contextPath + "/object/popupMapSearch.html?type="+type + "&query=" + escape(query);

        $("#cssPopup").load(URL,function(){
		//$("#cssPopupContainer").center(); //Si activamos esta línea y desactivamos la de abajo nos centrará el PopUp en el medio de la pantalla.
		$("#cssPopupContainer").css("top",50);
		ancho=$(window).width();
		$("#cssPopupContainer").slideDown("fast", function () {
			anchopopup=$("#cssPopup").width();
			$("#cssPopupContainer").css("width",ancho);
		});
	});
    });

    //Metodos para el mapa
//    $('.popupMapAccept').live('click',function() {
//        var keyValue ="";
//        var valueValue ="";
//        var classKey;
//        var classValue;
//
//        //Validar
//        var divKey = $("#divMapPopUpKey").find("div");
//        if (divKey.attr("class") == "basic") {
//            classKey = "BASIC";
//            var input = divKey.find("input");
//            keyValue = input.attr("value");
//            var clazz = input.attr("class");
//            if (clazz == "basicString") {
//            } else if (clazz == "basicInteger" && !validateInteger(keyValue)) {
//                alert(errorMapKeyNotInteger);
//                return;
//            } else if (clazz == "basicDecimal" && !validateDecimal(keyValue)) {
//                alert(errorMapKeyNotDecimal);
//                return;
//            } else if (clazz == "basicDate" && !validateDate(keyValue)) {
//                alert(errorMapKeyNotDate);
//                return;
//            }
//        } else if (divKey.attr("class") == "file") {
//            input = divKey.find("input");
//            keyValue = input;
//            classKey = "FILE";
//        } else {
//            classKey = "COMPLEX";
//            var tbody = divKey.find("tbody");
//            tbody.find("input[name='selected_key']").each( function() {
//                var selected = $(this).attr('checked');
//                if (selected) {
//                    keyValue= $(this).attr('value');
//                }
//            })
//        }
//        //Validar
//        var divValue = $("#divMapPopUpValue").find("div");
//        if (divValue.attr("class") == "basic") {
//            classValue = "BASIC";
//            input = divValue.find("input");
//            valueValue = input.attr("value");
//            clazz = input.attr("class");
//            if (clazz == "basicString") {
//            } else if (clazz == "basicInteger" && !validateInteger(valueValue)) {
//                    alert(errorMapValueNotInteger);
//                    return;
//            } else if (clazz == "basicDecimal" && !validateDecimal(valueValue)) {
//                    alert(errorMapValueNotDecimal);
//                    return;
//            } else if (clazz == "basicDate" && !validateDate(valueValue)) {
//                    alert(errorMapValueNotDate);
//                    return;
//            }
//        } else if (divValue.attr("class") == "file") {
//            classValue = "FILE";
//            input = divValue.find("input");
//            valueValue = input.attr("value");
//        } else {
//            classValue = "COMPLEX";
//            tbody = divValue.find("tbody");
//            tbody.find("input[name='selected_value']").each( function() {
//                var selected = $(this).attr('checked');
//                if (selected) {
//                    valueValue= $(this).attr('value');
//                }
//            })
//        }
//        addRowToMap(keyValue, classKey, valueValue, classValue);
//        resolvePopUpForMap();
//    })
//
//
//    $('.popupMapCancel').live('click',function() {
//        resolvePopUpForMap();
//    })
//
//    $('.deleteRowMap').live('click',function() {
//        deleteRowMap($(this));
//    })
});

function validateForm() {
        //Validate form
        var validForm = true;
        $('#form').find('input').each(
        function() {
                var clazz = $(this).attr('class');
                var aux;
                var valid = true;
                if (clazz == 'integerInput') {
                    valid = validateInteger($(this).attr('value'));
                    aux = errorOnInteger;
                } else if (clazz == 'decimalInput') {
                    valid = validateDecimal($(this).attr('value'));
                    aux = errorOnDecimal;
                } else if (clazz == 'dateInput') {
                    valid = validateDate($(this).attr('value'));
                    aux = errorOnDate;
                }
                if (!valid) {
                    var name = $(this).attr('name');                    
                    alert(aux.replace("#PROPERTY#", name));
                    this.focus();
                    validForm = false;
                }                
            }
        )
        return validForm;
}

function validateInteger(value) {
    var integerStr = /-?\d+/i;
    if (value.length > 0) {
        var result = integerStr.exec(value);
        if (result == null || result.length != 1 || result[0].length != value.length) return false;
    }
    return true;
}

function validateDecimal(value) {
    if (value.length > 0) {
        if (!validateInteger(value)) {
            var decimalStr = /-?\d+\.\d+/i;
            var result = decimalStr.exec(value);
            if (result == null || result.length != 1 || result[0].length != value.length) return false;
        }
    }
    return true;
}

function validateDate(value) {
    if (value.length > 0) {
        var dateStr;
        if (value.length == 10) dateStr = /\d{2}\/\d{2}\/\d{4}/i;
        else dateStr = /\d{2}\/\d{2}\/\d{4}\s\d{2}:\d{2}/i;
        var result = dateStr.exec(value);
        if (result == null || result.length != 1 || result[0].length != value.length) return false;
        var day,month,year,hour,minute = null;
        if (locale == 'en_US') {
            day= value.substr(3,2);
            month= value.substr(0,2);
            year= value.substr(6,4);
            if (value.length>10) {
                hour= value.substr(11,2);
                minute= value.substr(14,2);
            }
        } else {
            month= value.substr(3,2);
            day= value.substr(0,2);
            year= value.substr(6,4);
            if (value.length>10) {
                hour= value.substr(11,2);
                minute= value.substr(14,2);
            }
        }
        if (month>12) return false;
        month = month -1; //Correcion del mes para javascript
        var maxDay;
        switch(month) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                maxDay = 31;
            break;
            case 1:
                if (year %4 == 0) {
                    if (year % 100 == 0) {
                        if (year % 400 == 0) maxDay = 29
                        else maxDay = 28
                    } else maxDay=29;
                } else maxDay = 28;
            break;
            case 3:
            case 5:
            case 8:
            case 10:
                maxDay = 30
            break;
        }
        if (day > maxDay) return false;
        if (hour != null && hour > 23) return false;
        if (minute != null && minute > 59) return false;        
    }
    return true;
}

//Funciones pop up referencia
var popUpForObjectInputVisible;
var popUpForObjectInputHidden;

function showPopUpForObject(type,inputVisible,inputHidden) {
    var url = contextPath + "object/popupObject.html?type="+type;
    popUpForObjectInputVisible = inputVisible;
    popUpForObjectInputHidden = inputHidden;
    popupCssShow(url, 460);
}

function resolvePopUpForObject() {
    popUpForObjectInputVisible = null;
    popUpForObjectInputHidden = null;
    popupCssHide();
}

//Funciones pop up lista objetos
var popUpForObjectListProperty;

function showPopUpForObjectList(type,property) {
    var url = contextPath + "object/popupObjectList.html?type="+type;
    popUpForObjectListProperty = property;
    popupCssShow(url, 460);
}

function resolvePopUpForObjectList() {
    popUpForObjectListProperty = null;
    popupCssHide();
}

//Funciones pop up lista objetos
var popUpForMapInput;
function showPopUpForMap(type,td) {
    var url = contextPath + "/object/popupMapSearch.html?type="+type;
    popUpForMapInput = td;
    popupCssShow(url, 460);

}

function resolvePopUpForMap() {
    popUpForMapInput = null;
    popupCssHide();
}

function addRowToMap(keyValue, classKey, valueValue, classValue) {


    var tbody = $("#"+popUpForMapProperty).find("tbody");
    $(tbody).append("<tr>/tr>");
//
//
//    if (keyValue == "") classKey = "BASIC";
//    if (valueValue == "") classValue = "BASIC";
//    var key;
//    var value;
//    if (classKey == "COMPLEX") {
//        var data = keyValue.split("|", 3);
//        key = data[0];
//        value = data[1] +":" + data[2];
//    } else {
//        key = keyValue;
//        value = keyValue;
//    }
//    //Eliminamos la posible clave duplicada
//    tbody.find("input[name^="+popUpForMapProperty+"_key_]").each(function(){
//        if ($(this).attr("value") == key) {
//            deleteRowMap($(this));
//        }
//    })
//
//
//    var count = tbody.find("input[name^="+popUpForMapProperty+"_key_]").length;
//
//    //Generamos el contenido de la celda de la clave
//    if (classKey == "COMPLEX" || classKey == "BASIC") {
//        var keyCell = '<input type="hidden" name="'+popUpForMapProperty+'_key_'
//        +count+ '" value="'+key+'"/>'+ value;
//    } else {
//        keyCell = '<input type="file" name="'+popUpForMapProperty+'_key_'
//        +count+ '" value="'+key+'"/>'
//    }
//    var text = '<tr><td>'+ keyCell+'</td>';
//    if (classValue == "COMPLEX") {
//        data = valueValue.split("|", 3);
//        key = data[0];
//        value = data[1] +":" + data[2];
//    } else {
//        key = valueValue;
//        value = valueValue;
//    }
//    //Generamos el contenido de la celda de la valor
//    if (classKey == "COMPLEX" || classKey == "BASIC") {
//        var valueCell = '<input type="hidden" name="'+popUpForMapProperty+'_value_'+count+'" value="'+key+'"/>'+value;;
//    } else {
//        valueCell = '<input type="file" name="'+popUpForMapProperty+'_value_'+count+ '" value="'+key+'"/>'
//    }
//    text += '<td>'+valueCell+'</td><td><input type="button" value="'+ deleteRowList + '" class="deleteRowMap"/></td></tr>';
//    tbody.append(text);

}

function deleteRowMap(element) {
    var tbody = element.parent().parent().parent();
    var property = tbody.parent().attr("id");
    var tr = element.parent().parent();
    tr.remove();
    var count = 0;
    tbody.find("input[name^="+property+"_key_]").each(function(){
        var name = $(this).attr("name");
        var prevIndex = name.substr((property + "_key_").length);
        var value = $("input[name="+property+"_value_"+prevIndex+"]");
        $(this).attr("name",property+"_key_"+count);
        value.attr("name",property+"_value_"+count);
        count++;
    })
}

//Variable que almacena la posición del scroll, por defecto tiene valor 0.
scrollCachePosition = 0;
function popupCssShow (URL, width, height) { //Parámetros: URL (URL, Anchura de la capa, Altura de la capa)
	if (typeof document.body.style.maxHeight === "undefined") {//Añade la propiedad maxHeight para IE6.
		$("body","html").css({height: "100%", width: "100%"});
	}
	//La capa 'cssBackground' ocupa toda la pantalla para darle una opacidad.
	//La capa 'cssPopupContainer' es la capa madre del PopUp.
	if (!$("#cssBackground").length>0) {
		$("body").append("<div id=\"cssBackground\"></div><div id=\"cssPopupContainer\"><div id=\"cssPopup\"></div></div>"); //Añade las capas en la página.
	}
	if (width!=undefined) {
		$("#cssPopup").css("width",width);
	}
	if (height!=undefined) {
		$("#cssPopup").css("height",height);
		$("#cssPopup").css("overflow","auto");
	}
	$("#cssBackground").slideDown("slow"); //Efecto jQuery
	scrollCachePosition = $(window).scrollTop();
	window.top.scroll(0,0);
	$("#cssPopup").load(URL,function(){
		//$("#cssPopupContainer").center(); //Si activamos esta línea y desactivamos la de abajo nos centrará el PopUp en el medio de la pantalla.
		$("#cssPopupContainer").css("top",50);
		ancho=$(window).width();
		$("#cssPopupContainer").slideDown("fast", function () {
			anchopopup=$("#cssPopup").width();
			$("#cssPopupContainer").css("width",ancho);
		});
	});
}

function popupCssHide () {
	$("#cssPopupContainer").slideUp("fast", function () {
		$("#cssBackground").fadeOut("fast",function () {
			$("#cssBackground").remove(); //Elimina la capa 'cssBackground'.
			$("#cssPopupContainer").remove(); //Elimina la capa 'cssPopupContainer'.
		});
	});
	if (scrollCachePosition > 0) {
		window.top.scroll(0,scrollCachePosition); //Vuelve a la posición donde estaba el scroll.
		//Reseteamos la variable scrollCachePosition a 0 para poder ejecutar el script tantas veces como sea necesario.
		scrollCachePosition = 0;
	}
}
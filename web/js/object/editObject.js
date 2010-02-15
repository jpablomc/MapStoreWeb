/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var locale;
var contextPath;
var errorOnInteger = "Property #PROPERTY# must be an integer";
var errorOnDecimal = "Property #PROPERTY# must be a decimal";
var errorOnDate = "Property #PROPERTY# must be a date";
var promptListString = "Introduce the string to add";
var promptListDecimal = "Introduce the decimal number to add";
var promptListInteger = "Introduce the integer number to add";
var promptListDate = "Introduce the date to add";
var errorListDecimal = "Value must be a decimal number";
var errorListInteger = "Value must be a integer number";
var errorListDate = "Value must be a date number";
var deleteRowList = "Delete";
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
    $('#form').submit(function(e) {
        var valid = validateForm();
        if (!valid) e.preventDefault();
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
            tbody.append('<tr><td><input type="text" value='+result+
                ' name="'+property+'" reodonly="true"/></td><td><input type="button" value="'+ deleteRowList +
                '" class="deleteRow"/></td></tr>')
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
                tbody.append('<tr><td><input type="text" value='+result+
                    ' name="'+property+'" reodonly="true"/></td><td><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/></td></tr>')
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
                tbody.append('<tr><td><input type="text" value='+result+
                    ' name="'+property+'" reodonly="true"/></td><td><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/></td></tr>')
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
                tbody.append('<tr><td><input type="text" value='+result+
                    ' name="'+property+'" reodonly="true"/></td><td><input type="button" value="'+ deleteRowList +
                    '" class="deleteRow"/></td></tr>')
            } else alert (errorListDecimal);
        }
    })

    $('.deleteRow').live('click',function() {
        var tr = $(this).parent().parent();
        tr.remove();
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
                    '" name="'+popUpForObjectListProperty+'"/><input type="text" value="'
                    +data[1]+': ' + data[2] + '"  reodonly="true"/></td><td><input type="button" value="'
                    + deleteRowList + '" class="deleteRow"/></td></tr>')
            }
        })
        resolvePopUpForObjectList();
    })

    $('.popupObjectListCancel').live('click',function() {
        resolvePopUpForObjectList();
    })

    //Metodos para el mapa
    $('.addToMap').live('click',function() {
        //determinar tipo de la lista
        var property = $(this).parent().find("table").attr('id');
        var typeKey;
        var typeValue;
        var i;
        for (i=0;i<mapPropertyName.length;i++) {
            if (listPropertyName[i] == property) {
                typeKey = mapPropertyKeyType[i];
                typeValue = mapPropertyValueType[i];
            }
        }
        showPopUpForMap(typeKey,typeValue,property);
    });

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
                    valid = validateInteger(this.attr('value'));
                    aux = errorOnInteger;
                } else if (clazz == 'decimalInput') {
                    valid = validateDecimal(this.attr(value));
                    aux = errorOnDecimal;
                } else if (clazz == 'dateInput') {
                    valid = validateDate(this);
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
    var integerStr = /\d+/i;
    if (value.length > 0) {
        var result = integerStr.exec(value);
        if (result == null || result.length != 1 || result[0].length != value.length) return false;
    }
    return true;
}

function validateDecimal(value) {
    if (value.length > 0) {
        if (!validateInteger(value)) {
            var decimalStr = /\d+\.\d+/i;
            var result = decimalStr.exec(value);
            if (result == null || result.length != 1 || result[0].length != value.length) return false;
        }
    }
    return true;
}

function validateDate(input) {
    var value = input.value;
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
var popUpForObjectListProperty;

function showPopUpForMap(typeKey,typeValue,property) {
    var url = contextPath + "object/popupObjectMap.html?type="+type;
    popUpForObjectListProperty = property;
    popupCssShow(url, 460);

}

function resolvePopUpForMap() {

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
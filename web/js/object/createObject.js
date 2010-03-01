/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var keyInternationalizedString;
var valueInternationalizedString;
var addToListInternationalizedString;
var addToMapInternationalizedString;
var addToObjectInternationalizedString;
var clearObjectInternationalizedString;
$(function(){
$('#datatype').change(function() {
    changeDatatype();
});

$('#name').keyup(function() {
    $('.stringName').attr('value', $('#name').attr('value'));
});

});

function changeDatatype() {
                var text ="";
                var ajaxUrl = contextPath+ "/object/getFormForNewObject.html";
                $.ajax({
                    type: "POST",
                    url: ajaxUrl,
                    data: ({datatype: $('#datatype').attr('value')}),
                    success: function(response) {
                        $(response).find('property').each(function(){
                            var propertyName = $(this).attr('name');
                            text += propertyName + ':';
                            var type = $(this).attr('type');
                            if (type == 'String') {
                                if ($(this).attr('isName') != null) {
                                    var name = $('#name').attr('value');
                                    text += '<input type="text" id="'+propertyName+'" name="'+propertyName+'" readonly="readonly" value="'+name+'" class="stringName"/>';

                                } else {
                                    text += '<input type="text" id="'+propertyName+'" name="'+propertyName+'" class="stringInput"/>';
                                }
                            } else if (type == 'INTEGER' ) {
                                    text += '<input type="text" id="'+propertyName+'" name="'+propertyName+'" class="integerInput"/>';
                            } else if (type == 'DECIMAL' ) {
                                    text += '<input type="text" id="'+propertyName+'" name="'+propertyName+'" class="decimalInput"/>';
                            } else if (type == 'Date' ) {
                                    text += '<input type="text" id="'+propertyName+'" name="'+propertyName+'" class="dateInput"/>';
                            } else if (type == 'File' ) {
                                    text += '<input type="file" id="'+propertyName+'" name="'+propertyName+'" class="dateFile"/>';
                            } else if (type == 'List' ) {
                                var subtype = $(this).attr('subtype');
                                if (subtype == 'String') {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListString"/></div>';
                                } else if (subtype == 'INTEGER' ) {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListInteger"/></div>';
                                } else if (subtype == 'DECIMAL' ) {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListDecimal"/></div>';
                                } else if (subtype == 'Date' ) {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListDate"/></div>';
                                } else if (subtype == 'File' ) {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListFile"/></div>';

                                } else {
                                    text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                        valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                        addToListInternationalizedString+'" class="addToListObject"/></div>';
                                        listPropertyName.push(propertyName);
                                        listPropertySubtype.push(subtype);
                                }
                            } else if (type == 'Map' ) {
                                var mapKeyType = $(this).attr('mapKeyType');
                                var mapValueType = $(this).attr('mapValueType');
                                text += '<div><table id="'+propertyName+'"><thead><tr><th>'+
                                    keyInternationalizedString+'</th><th>'+
                                    valueInternationalizedString+'</th><th></th></tr></thead><tbody></tbody></table><input type="button" value="'+
                                    addToMapInternationalizedString+'" class="addToMap"/></div>';
                                    mapPropertyName.push(propertyName);
                                    mapPropertyKeyType.push(mapKeyType);
                                    mapPropertyValueType.push(mapValueType);
                            } else {
                                text += '<div><input type="hidden" name="'+
                                    propertyName+'" id="'+propertyName+
                                    '" class="hiddenObject"/><input type="text" disabled="disabled" class="visibleObject"/>'+
                                    '<input type="button" value="'+
                                    selecObjectInternationalizedString+
                                    '" class="addToObject"/><input type="button" value="'+
                                    clearObjectInternationalizedString+
                                    '" class="clearObject"/></div>';
                                    objectPropertyName.push(propertyName);
                                    objectPropertyType.push(type);                                    
                            }
                            text +='<br/>';
                        });
                        $('#properties').html(text);
                    }
                })
}
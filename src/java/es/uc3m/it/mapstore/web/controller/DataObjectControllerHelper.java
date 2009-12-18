/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;

/**
 *
 * @author Pablo
 */
public class DataObjectControllerHelper {
    public static String createFormForNewObject(DataType dt) {
        StringBuffer sb = new StringBuffer();
        for (String atributo : dt.getAttributes().keySet()) {
            sb.append(createLabelForProperty(atributo));
            sb.append(createInputForProperty(atributo,dt));
            sb.append("<br/>");
        }
        return sb.toString();
    }

    private static String createLabelForProperty(String property) {
        StringBuffer sb = new StringBuffer("<label for=\"").append(property)
                .append("\">").append(property).append(": </label>");
        return sb.toString();
    }

    private static String createInputForProperty(String property,DataType dt) {
        String result;
        DataType aux = dt.getAttributes().get(property);
        String name = aux.getName();
        if (DataTypeConstant.LISTTYPES.contains(aux.getName())) {
            DataType aux2 = dt.getListDataType(property);
            result = createInputForList(property,aux2);
        }else if (DataTypeConstant.MAPTYPES.contains(aux.getName())) {
            DataType aux2 = dt.getMapKeyDataType(property);
            DataType aux3 = dt.getMapValueDataType(property);
            result = createInputForMap(property,aux2,aux3);
        }else if (DataTypeConstant.DATETYPE.equals(name)) {
            result = createInputForDate(property);
        }else if (DataTypeConstant.STRINGTYPE.equals(name)) {
            result = createInputForString(property,dt.getPk());
        }else if (DataTypeConstant.INTEGERTYPE.equals(name) || DataTypeConstant.LONGTYPE.equals(name)) {
            result = createInputForInteger(property);
        }else if (DataTypeConstant.FLOATTYPE.equals(name) || DataTypeConstant.DOUBLETYPE.equals(name)) {
            result = createInputForDecimal(property);
        }else result = createInputForObject(property,aux);
        return result;
    }

    private static String createInputForList(String property, DataType aux2) {
        String name = aux2.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("<table id=").append(property).append("><thead><tr><th></th><th></th></tr></thead><<tbody></tbody>/table");
        if (DataTypeConstant.BASICTYPES.contains(name)) {
            if(DataTypeConstant.STRINGTYPE.equals(name)) {
                sb.append("<input type=\"button\" onclicK=\"listAddString("+property+")\" value=\"Add\" />");
            } else if(DataTypeConstant.DATETYPE.equals(name)) {
                sb.append("<input type=\"button\" onclicK=\"listAddDate("+property+")\" value=\"Add\" />");
            } else if (DataTypeConstant.FLOATTYPE.equals(name) ||DataTypeConstant.DOUBLETYPE.equals(name)) {
                sb.append("<input type=\"button\" onclicK=\"listAddDecimal("+property+")\" value=\"Add\" />");
            } else {
                sb.append("<input type=\"button\" onclicK=\"listAddInteger("+property+")\" value=\"Add\" />");
            }
        } else{
                sb.append("<input type=\"button\" onclicK=\"listAddObject("+property+","+name+")\" value=\"Add\" />");
        }
        return sb.toString();
    }

    private static String createInputForMap(String property, DataType key, DataType value) {
        String keyStr = key.getName();
        String valueStr = value.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("<table id=").append(property).append("><thead><tr><th></th><th></th><th></th></tr></thead><<tbody></tbody>/table");
        sb.append("<input type=\"button\" onclicK=\"mapAddObject("+property+","+keyStr+","+valueStr+")\" value=\"Add\" />");
        return sb.toString();
    }

    private static String createInputForDate(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"text\" id=\"").append(property).
                append("\" name=\"").append(property).
                append("\" onblur=\"checkDate(").append(property).append("\"/>");
        return sb.toString();
    }

    private static String createInputForString(String property, String pk) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"text\" id=\"").append(property).
                append("\" name=\"").append(property).append("\" ");
        if (property.equals(pk)) sb.append("readonly=\"readonly\" ");
        sb.append("/>");
        return sb.toString();
    }

    private static String createInputForInteger(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"text\" id=\"").append(property).
                append("\" name=\"").append(property).
                append("\" onblur=\"checkInteger(").append(property).append("\"/>");
        return sb.toString();
    }

    private static String createInputForDecimal(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"text\" id=\"").append(property).
                append("\" name=\"").append(property).
                append("\" onblur=\"checkDecimal(").append(property).append("\"/>");
        return sb.toString();
    }

    private static String createInputForObject(String property, DataType aux) {
        String name = aux.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"text\" id=\"").append(property).
                append("\" name=\"").append(property).append("\"").
                append("readOnly=\"readonly\"/>");
        sb.append("<input type=\"button\" onclicK=\"inputAddObject("+property+","+name+")\" value=\"Add\" />");
        sb.append("<input type=\"button\" onclicK=\"inputClearObject("+property+")\" value=\"Remove\" />");
        return sb.toString();
    }

}

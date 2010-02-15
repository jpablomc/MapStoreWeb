/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.exception.MapStoreRunTimeException;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pablo
 */
public class DataObjectControllerHelper {
    public static String createFormForNewObject(DataType dt) {
        StringBuffer sb = new StringBuffer();
        sb.append("<properties>");
        List<String> aux = new ArrayList(dt.getAttributes().keySet());
        Collections.sort(aux);
        for (String atributo : aux) {
            sb.append(createInputForProperty(atributo,dt));
        }
        sb.append("</properties>");
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
        String name = "";
        if (aux2 != null) name = aux2.getName();
        StringBuffer sb = new StringBuffer();
        if (DataTypeConstant.BASICTYPES.contains(name)) {
            if(DataTypeConstant.STRINGTYPE.equals(name)) {
                sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.LISTTYPE+"\" subtype=\""+DataTypeConstant.STRINGTYPE+"\" />");
            } else if(DataTypeConstant.DATETYPE.equals(name)) {
                sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.LISTTYPE+"\" subtype=\""+DataTypeConstant.DATETYPE+"\" />");
            } else if (DataTypeConstant.FLOATTYPE.equals(name) ||DataTypeConstant.DOUBLETYPE.equals(name)) {
                sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.LISTTYPE+"\" subtype=\"DECIMAL\" />");
            } else if (DataTypeConstant.INTEGERTYPE.equals(name) ||DataTypeConstant.LONGTYPE.equals(name)) {
                sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.LISTTYPE+"\" subtype=\"INTEGER\" />");
            } else {
                throw new MapStoreRunTimeException("IMPOSIBLE");
            }
        } else{
                sb.append("<property name=\""+property+"\"  type=\""+DataTypeConstant.LISTTYPE+"\" subtype=\""+name+"\" />");
        }
        return sb.toString();
    }

    private static String createInputForMap(String property, DataType key, DataType value) {
        String keyStr = key.getName();
        String valueStr = value.getName();
        StringBuffer sb = new StringBuffer();
        String mapKey;
        String mapValue;
        mapKey = (key != null)?key.getName():"";
        mapValue = (value != null)?value.getName():"";
        sb.append("<property name=\""+property+"\" type=\"")
                .append(DataTypeConstant.MAPTYPE).append("\" mapKeyType=\"").append(mapKey)
                .append("\" mapValueType=\"").append(mapValue).append("\"/>");
        return sb.toString();
    }

    private static String createInputForDate(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.DATETYPE+"\"/>");
        return sb.toString();
    }

    private static String createInputForString(String property, String pk) {
        StringBuffer sb = new StringBuffer();
        sb.append("<property name=\""+property+"\" type=\""+DataTypeConstant.STRINGTYPE+"\"");
        if (property.equals(pk)) sb.append(" isName=\"true\"");
        sb.append(" />");
        return sb.toString();
    }

    private static String createInputForInteger(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<property name=\""+property+"\" type=\"INTEGER\"/>");
        return sb.toString();
    }

    private static String createInputForDecimal(String property) {
        StringBuffer sb = new StringBuffer();
        sb.append("<property name=\""+property+"\" type=\"DECIMAL\"/>");
        return sb.toString();
    }

    private static String createInputForObject(String property, DataType aux) {
        String name = aux.getName();
        StringBuffer sb = new StringBuffer();
        sb.append("<property name=\""+property+"\" type=\""+aux.getName()+"\"/>");
        return sb.toString();
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.bean.MapStoreExtendedItem;
import es.uc3m.it.mapstore.bean.MapStoreItem;
import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import es.uc3m.it.mapstore.web.beans.DataObject;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import es.uc3m.it.mapstore.web.services.exception.NonUniqueResultException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pablo
 */
@Service
public class DataObjectService {

    private List<DataObject> getAllByDatatype(String datatype) {
        MapStoreSession s = MapStoreSession.getSession();
        List<DataObject> results = s.findByType(DataObject.class, datatype);
        return results;
    }

    /**
     *
     * The method returns the object with the given type and name or null if it does not exist
     *
     *
     * @param datatype The datatype name
     * @param name The name of the object
     * @return The object
     */
    private DataObject getByDatatypeName(String datatype, String name) {
        MapStoreSession s = MapStoreSession.getSession();
        DataObject result = s.findByNameType(name, datatype, DataObject.class);
        return result;
    }

    /**
     *
     * The method returns the object with the given type and name or null if it does not exist.
     * The object is wrapped with additional info.
     *
     * @param datatype The datatype name
     * @param name The name of the object
     * @param d The date when the object is in databse (filters version). null for current version
     * @return The wrapped object
     */
    private MapStoreExtendedItem<DataObject> getByDatatypeNameDate(String datatype, String name, Date d) {
        String query = "_TYPE = " + datatype + " AND _NAME = " + name;
        MapStoreSession s = MapStoreSession.getSession();
        List<MapStoreExtendedItem<DataObject>> results = s.queryExtended(DataObject.class, query, d);
        if (results.size() != 1) {
            throw new NonUniqueResultException("Query: (" + query + ") returned " + results.size() + " results");
        }
        return results.get(0);
    }

    /**
     *
     * Performs the query indicated
     *
     * @param query
     * @param d
     * @return
     */
    public List<MapStoreExtendedItem<DataObject>> find(String query, Date d) {
        MapStoreSession s = MapStoreSession.getSession();
        List<MapStoreExtendedItem<DataObject>> results = s.queryExtended(DataObject.class, query, d);
        return results;
    }

    public DataObject getById(int id) {
        MapStoreSession s = MapStoreSession.getSession();
        return s.recoverById(id, DataObject.class);
    }

    public void createDataObject(DataObject object) {
        MapStoreSession s = MapStoreSession.getSession();
        try {
            s.beginTransaction();
            s.save(object);
            s.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
    }

    public void updateDataObject(DataObject object) {
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        s.update(object);
        s.commit();
        s.close();
    }


    public Map<String, Object> showObject(int id, Locale loc) {
        MapStoreSession s = MapStoreSession.getSession();
        MapStoreItem last = s.recoverRawById(id);
        s.close();
        return showObject(last.getId(), last.getVersion(), loc);
    }

    public Map<String, Object> showObject(int id, int version, Locale loc) {
        Map<String, Object> data = new HashMap<String, Object>();
        MapStoreSession s = MapStoreSession.getSession();
        MapStoreItem root = s.recoverRawByIdVersion(id, version);
        MapStoreItem last = s.recoverRawById(id);
        DataObject dataObject = s.recoverByIdVersion(id, version, DataObject.class);
        DataType dt = dataObject.getDataType();
        String pattern;
        if (Locale.US.equals(loc)) {
            pattern = "MM/dd/yyyy HH:mm:ss";
        } else {
            pattern = "dd/MM/yyyy HH:mm:ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        List<String> attrib = new ArrayList();
        data.put("ATTRIB", attrib);
        data.put("LASTVERSION", last.getVersion());
        data.put("CURRENTVERSION", root.getVersion());
        data.put("NAME", root.getName());
        data.put("TYPE", root.getType());
        data.put("_ID", root.getId());
        data.put("PK", dataObject.getDataType().getPk());
        for (String property : dt.getAttributes().keySet()) {
            attrib.add(property);
            DataType typeAttrib = dt.getAttributes().get(property);
            if (DataTypeConstant.STRINGTYPE.contains(typeAttrib.getName())) {
                showObjectHelperForString(root, property, dataObject, data);
            } else if (DataTypeConstant.DATETYPE.contains(typeAttrib.getName())) {
                showObjectHelperForDate(dataObject, property, df, data);
            } else if (DataTypeConstant.DOUBLETYPE.contains(typeAttrib.getName())) {
                String text = "DOUBLE|";
                Double aux = (Double)dataObject.get(property);
                if (aux!= null) text += aux;
                data.put(property, text);
                data.put("TYPE_" + property, "DOUBLE");
            } else if (DataTypeConstant.FLOATTYPE.contains(typeAttrib.getName())) {
                String text = "FLOAT|";
                Float aux = (Float)dataObject.get(property);
                if (aux!= null) text += aux;
                data.put(property, text);
                data.put("TYPE_" + property, "FLOAT");
            } else if (DataTypeConstant.INTEGERTYPE.contains(typeAttrib.getName())) {
                String text = "INTEGER|";
                Integer aux = (Integer)dataObject.get(property);
                if (aux!= null) text += aux;
                data.put(property, text);
                data.put("TYPE_" + property, "INTEGER");
            } else if (DataTypeConstant.LONGTYPE.contains(typeAttrib.getName())) {
                String text = "LONG|";
                Long aux = (Long)dataObject.get(property);
                if (aux!= null) text += aux;
                data.put(property, text);
                data.put("TYPE_" + property, "LONG");
            } else if (DataTypeConstant.ARRAYTYPE.contains(typeAttrib.getName())) {
                throw new UnsupportedOperationException("Not implemented");
            } else if (DataTypeConstant.LISTTYPE.contains(typeAttrib.getName())) {
                showObjectHelperForList(data, property, dt, root, s, df);
            } else if (DataTypeConstant.MAPTYPE.contains(typeAttrib.getName())) {
                showObjectHelperForMap(data, property, dt, root, s,loc);
            } else {
                //Es una referencia a un objeto
                data.put("TYPE_" + property, typeAttrib.getName());
                String reference = (String)root.getProperty(MapStoreItem.NONPROCESSABLE_REFERENCE + property);
                if (reference != null) {
                    String[] refData = reference.split("_");
                    MapStoreItem related = s.recoverRawByIdVersion(Integer.parseInt(refData[0]), Integer.parseInt(refData[1]));
                    String text = related.getType() + "|" + related.getId() + "|" +related.getVersion() + "|" + related.getType() +": " + related.getName();
                    data.put(property, text);
                }
            }
        }
        s.close();
        return data;
    }

    private void showObjectHelperForMap(Map<String, Object> data, String property, DataType dt, MapStoreItem root, MapStoreSession s, Locale loc){
        data.put("TYPE_" + property, "MAP");
        DataType keyType = dt.getMapKeyDataType(property);
        DataType valueType = dt.getMapValueDataType(property);
        boolean isKeyReference = false;
        boolean isValueReference = false;
        Map<String, String> properties = new HashMap<String, String>();
        if (keyType == null) {
            isKeyReference = true;
            data.put("KEYTYPE_" + property, "");
        } else if (DataTypeConstant.STRINGTYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "STRING");
        } else if (DataTypeConstant.INTEGERTYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "INTEGER");
        } else if (DataTypeConstant.LONGTYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "LONG");
        } else if (DataTypeConstant.DOUBLETYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "DOUBLE");
        } else if (DataTypeConstant.FLOATTYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "FLOAT");
        } else if (DataTypeConstant.DATETYPE.equals(keyType.getName())) {
            data.put("KEYTYPE_" + property, "DATE");
        } else {
            isKeyReference = true;
            data.put("KEYTYPE_" + property, keyType.getName());
        }
        if (valueType == null) {
            isValueReference = true;
            data.put("VALUETYPE_" + property, "");
        } else if (DataTypeConstant.STRINGTYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "STRING");
        } else if (DataTypeConstant.INTEGERTYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "INTEGER");
        } else if (DataTypeConstant.LONGTYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "LONG");
        } else if (DataTypeConstant.DOUBLETYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "DOUBLE");
        } else if (DataTypeConstant.FLOATTYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "FLOAT");
        } else if (DataTypeConstant.DATETYPE.equals(valueType.getName())) {
            data.put("VALUETYPE_" + property, "DATE");
        } else {
            isValueReference = true;
            data.put("VALUETYPE_" + property, valueType.getName());
        }
        //Obtenemos la referencia
        String refStr = (String) root.getProperty(MapStoreItem.NONPROCESSABLE_REFERENCE + property);
        if (refStr != null) {
            String[] reference = refStr.split("_");
            MapStoreItem mapItem = s.recoverRawByIdVersion(Integer.parseInt(reference[0]), Integer.parseInt(reference[1]));
            if (mapItem.getProperty("_type_key_") != null) {
                //Caso Map<Object,Object>
                int indexMax = -1;
                for (String aux : mapItem.getProperties().keySet()) {
                    int tmp;
                    int index = aux.indexOf("_key_");
                    if (index > -1 ) {
                        String tmpStr = aux.substring(index+"_key_".length());
                        if (tmpStr.length() > 0) {
                            tmp = Integer.parseInt(tmpStr);
                            if (tmp > indexMax) indexMax = tmp;
                        }
                    } else {
                        index = aux.indexOf("_value_");
                        if (index > -1 ) {
                            String tmpStr = aux.substring(index+"_value_".length());
                            if (tmpStr.length()>0) {
                                tmp = Integer.parseInt(tmpStr);
                                if (tmp > indexMax) indexMax = tmp;
                            }
                        }
                    }
                }
                for(int i = 0; i <= indexMax;i++) {
                    String key = null;
                    String value = null;
                    if (isKeyReference) key = MapStoreItem.NONPROCESSABLE_REFERENCE + "_key_" + i;
                    else  key = "_key_" + i;
                    if (isValueReference) value = MapStoreItem.NONPROCESSABLE_REFERENCE + "_value_" + i;
                    else  value = "_value_" + i;
                    String keyTypeStr = (keyType != null) ? keyType.getName() : "";
                    String valueTypeStr = (valueType != null) ? valueType.getName() : "";
                    String keyStr = showObjectHelperForMapEntry(mapItem.getProperty(key), key, s, keyTypeStr, mapItem,loc);
                    String valueStr = showObjectHelperForMapEntry(mapItem.getProperty(value), value, s, valueTypeStr, mapItem, loc);
                    properties.put(keyStr, valueStr);
                }
            } else {
                //Caso Map<String,Object>
                String keys = (String) mapItem.getProperty("_processed_");
                for (String key : keys.split("\\|")) {
                    String value;
                    if (isValueReference) {
                        value = MapStoreItem.NONPROCESSABLE_REFERENCE + key;
                    } else {
                        value = key;
                    }
                    String keyTypeStr = (keyType != null) ? keyType.getName() : "";
                    String valueTypeStr = (valueType != null) ? valueType.getName() : "";
                    String keyStr = showObjectHelperForMapEntry(key, key, s, keyTypeStr, mapItem,loc);
                    String valueStr = showObjectHelperForMapEntry(mapItem.getProperty(value), value, s, valueTypeStr, mapItem,loc);
                    properties.put(keyStr, valueStr);
                }
            }
        }
        data.put(property, properties);
    }

    private String showObjectHelperForMapEntry(Object content, String var, MapStoreSession s, String valueType, MapStoreItem item,Locale loc) {
        String str = null;
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setGroupingUsed(false);
        NumberFormat df = NumberFormat.getNumberInstance();
        df.setGroupingUsed(false);
        if (df instanceof DecimalFormat) {
            DecimalFormat decf = (DecimalFormat) df;
            DecimalFormatSymbols dfs = decf.getDecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            decf.setDecimalFormatSymbols(dfs);
        }
        String pattern;
        if (Locale.US.equals(loc)) {
            pattern = "MM/dd/yyyy HH:mm:ss";
        } else {
            pattern = "dd/MM/yyyy HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        if (content != null) {
            if (var.startsWith(MapStoreItem.NONPROCESSABLE_REFERENCE)) {
                String[] cntRef = ((String) content).split("_");

                MapStoreItem contentItem = s.recoverRawByIdVersion(Integer.parseInt(cntRef[0]), Integer.parseInt(cntRef[1]));
                str = contentItem.getType() + "|" + contentItem.getId() + "|" + contentItem.getVersion() + "|" + contentItem.getType() + ":" + contentItem.getName();
            } else {
                if (DataTypeConstant.STRINGTYPE.equals(valueType)) {
                    String url = (String) item.getProperty(MapStoreItem.NONPROCESSABLE_URL + var);
                    String aux = ("null".equals(content))?"":(String)content;
                    if (url != null) {
                        str = "URL|" + aux + "|" + url;
                    } else {
                        str = "STRING|" + aux;
                    }
                } else if (DataTypeConstant.INTEGERTYPE.equals(valueType)) {
                    str = "INTEGER|" + nf.format((Integer) content);
                } else if (DataTypeConstant.LONGTYPE.equals(valueType)) {
                    str = "LONG|" + nf.format((Long) content);
                } else if (DataTypeConstant.DOUBLETYPE.equals(valueType)) {
                    str = "DOUBLE|" + df.format((Double) content);
                } else if (DataTypeConstant.FLOATTYPE.equals(valueType)) {
                    str = "FLOAT|" + df.format((Float) content);
                } else if (DataTypeConstant.DATETYPE.equals(valueType)) {
                    str = "DATE|" + sdf.format((Date)content);
                    if (str.endsWith(" 00:00:00")) {
                        str = str.substring(0,str.lastIndexOf(" 00:00:00"));
                    } else if (str.endsWith(":00")) {
                        str = str.substring(0,str.lastIndexOf(":00"));
                    }
                }
            }
        } else {
            if (DataTypeConstant.STRINGTYPE.equals(valueType)) {
                str = "STRING|";
            } else if (DataTypeConstant.INTEGERTYPE.equals(valueType)) {
                str = "INTEGER|";
            } else if (DataTypeConstant.LONGTYPE.equals(valueType)) {
                str = "LONG|";
            } else if (DataTypeConstant.DOUBLETYPE.equals(valueType)) {
                str = "DOUBLE|";
            } else if (DataTypeConstant.FLOATTYPE.equals(valueType)) {
                str = "FLOAT|";
            } else if (DataTypeConstant.DATETYPE.equals(valueType)) {
                str = "DATE|";
            } else {
                str = valueType + "|||";
            }
        }
        return str;
    }

    private void showObjectHelperForDate(DataObject dataObject, String property, SimpleDateFormat df, Map<String, Object> data) {
        Date value = (Date) dataObject.get(property);
        if (value != null) {
            String fecha = df.format(value);
            if (fecha.endsWith(" 00:00:00")) {
                fecha = fecha.substring(0, 10);
            }
            if (fecha.endsWith(":00")) {
                fecha = fecha.substring(0, 16);
            }
            String text = "DATE|" + fecha;
            data.put(property, text);
        }
        data.put("TYPE_" + property, "DATE");
    }

    private void showObjectHelperForString(MapStoreItem root, String property, DataObject dataObject, Map<String, Object> data) {
        String text;

        String url = (String) root.getProperty(MapStoreItem.NONPROCESSABLE_URL + property);
        String value = (String)dataObject.get(property);
        if (value == null) value = "";
        if (url != null) {
            text = "URL|" + value + "|" + url;
        } else {
            text = "STRING|" + value;
        }
        data.put(property, text);
        data.put("TYPE_" + property, "STRING");
    }

    private void showObjectHelperForList(Map<String, Object> data, String property, DataType dt, MapStoreItem root, MapStoreSession s, SimpleDateFormat df) throws NumberFormatException {
        data.put("TYPE_" + property, "LIST");
        DataType listType = dt.getListDataType(property);
        String reference = (String) root.getProperty(MapStoreItem.NONPROCESSABLE_REFERENCE + property);
        MapStoreItem listItem = null;
        if (reference != null) {
            String[] referenceArray = reference.split("_");
            listItem = s.recoverRawByIdVersion(Integer.parseInt(referenceArray[0]), Integer.parseInt(referenceArray[1]));
        }
        if (listType == null) {
            data.put("SUBTYPE_" + property, "");
        } else if (DataTypeConstant.STRINGTYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        String value = (String) listItem.getProperty(propertyList);
                        String url = (String) listItem.getProperty(MapStoreItem.NONPROCESSABLE_URL + propertyList);
                        String text = (url != null) ? "URL|" + value + "|" + url : "STRING|" + value;
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put("SUBTYPE_" + property, "STRING");
            data.put(property, values);
        } else if (DataTypeConstant.DATETYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        Date value = (Date) listItem.getProperty(propertyList);
                        String fecha = df.format(value);
                        if (fecha.endsWith(" 00:00:00")) {
                            fecha = fecha.substring(0, 10);
                        } else if (fecha.endsWith(":00")) {
                            fecha = fecha.substring(0, 16);
                        }
                        String text = "DATE|" + fecha;
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put("SUBTYPE_" + property, "DATE");
            data.put(property, values);
        } else if (DataTypeConstant.DOUBLETYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        Double value = (Double) listItem.getProperty(propertyList);
                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setGroupingUsed(false);
                        if (nf instanceof DecimalFormat) {
                            DecimalFormat decf = (DecimalFormat) nf;
                            DecimalFormatSymbols dfs = decf.getDecimalFormatSymbols();
                            dfs.setDecimalSeparator('.');
                            decf.setDecimalFormatSymbols(dfs);
                        }
                        String text = "DOUBLE|" + nf.format(value);
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put(property, values);
            data.put("SUBTYPE_" + property, "DOUBLE");
        } else if (DataTypeConstant.FLOATTYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        Float value = (Float) listItem.getProperty(propertyList);
                        NumberFormat nf = NumberFormat.getNumberInstance();
                        nf.setGroupingUsed(false);
                        if (nf instanceof DecimalFormat) {
                            DecimalFormat decf = (DecimalFormat) nf;
                            DecimalFormatSymbols dfs = decf.getDecimalFormatSymbols();
                            dfs.setDecimalSeparator('.');
                            decf.setDecimalFormatSymbols(dfs);
                        }
                        String text = "FLOAT|" + nf.format(value);
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put(property, values);
            data.put("SUBTYPE_" + property, "FLOAT");
        } else if (DataTypeConstant.INTEGERTYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        Integer value = (Integer) listItem.getProperty(propertyList);
                        NumberFormat nf = NumberFormat.getIntegerInstance();
                        nf.setGroupingUsed(false);
                        if (nf instanceof DecimalFormat) {
                            DecimalFormat decf = (DecimalFormat) nf;
                            DecimalFormatSymbols dfs = decf.getDecimalFormatSymbols();
                            dfs.setDecimalSeparator('.');
                            decf.setDecimalFormatSymbols(dfs);
                        }
                        String text = "INTEGER|" + nf.format(value);
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put(property, values);
            data.put("SUBTYPE_" + property, "INTEGER");
        } else if (DataTypeConstant.LONGTYPE.contains(listType.getName())) {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith("_prop_")) {
                        int index = Integer.parseInt(propertyList.substring("_prop_".length()));
                        Long value = (Long) listItem.getProperty(propertyList);
                        NumberFormat nf = NumberFormat.getIntegerInstance();
                        nf.setGroupingUsed(false);
                        if (nf instanceof DecimalFormat) {
                            DecimalFormat decf = (DecimalFormat) nf;
                            DecimalFormatSymbols dfs = decf.getDecimalFormatSymbols();
                            dfs.setDecimalSeparator('.');
                            decf.setDecimalFormatSymbols(dfs);
                        }
                        String text = "LONG|" + nf.format(value);
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put(property, values);
            data.put("SUBTYPE_" + property, "LONG");
        } else {
            Map<Integer, String> aux = new HashMap<Integer, String>();
            if (listItem != null) {
                for (String propertyList : listItem.getProperties().keySet()) {
                    if (propertyList.startsWith(MapStoreItem.NONPROCESSABLE_REFERENCE + "_prop_")) {
                        int index = Integer.parseInt(propertyList.substring((MapStoreItem.NONPROCESSABLE_REFERENCE + "_prop_").length()));
                        String referenceContent = (String) listItem.getProperty(propertyList);
                        String[] referenceArray = referenceContent.split("_");
                        MapStoreItem contentItem = s.recoverRawByIdVersion(Integer.parseInt(referenceArray[0]), Integer.parseInt(referenceArray[1]));
                        String text = contentItem.getType() + "|" + contentItem.getId() + "|" + contentItem.getVersion() + "|" + contentItem.getType() + ":" + contentItem.getName();
                        aux.put(index, text);
                    }
                }
            }
            List<String> values = new ArrayList<String>();
            for (int i = 0; i < aux.size(); i++) {
                values.add(aux.get(i));
            }
            data.put(property, values);
            data.put("SUBTYPE_" + property, listType.getName());
        }
    }
}

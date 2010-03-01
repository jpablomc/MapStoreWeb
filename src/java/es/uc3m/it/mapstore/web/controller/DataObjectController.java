/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.bean.MapStoreExtendedItem;
import es.uc3m.it.mapstore.bean.MapStoreFileItem;
import es.uc3m.it.mapstore.exception.MapStoreRunTimeException;
import es.uc3m.it.mapstore.web.beans.DataObject;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import es.uc3m.it.mapstore.web.services.DataObjectService;
import es.uc3m.it.mapstore.web.services.DataTypeService;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Pablo
 */
@Controller
public class DataObjectController {

    private DataObjectService serviceDataObject;
    private DataTypeService serviceDataType;

    @Autowired
    public DataObjectController(DataObjectService serviceDataObject, DataTypeService serviceDataType) {
        this.serviceDataObject = serviceDataObject;
        this.serviceDataType = serviceDataType;
    }

    public DataObjectService getServiceDataObject() {
        return serviceDataObject;
    }

    public void setServiceDataObject(DataObjectService serviceDataObject) {
        this.serviceDataObject = serviceDataObject;
    }

    public DataTypeService getServiceDataType() {
        return serviceDataType;
    }

    public void setServiceDataType(DataTypeService serviceDataType) {
        this.serviceDataType = serviceDataType;
    }
    private static final String paramSearchByDatatype = "id";
    private static final String paramSearchByQuery = "query";
    private static final String paramSearchDate = "date";
    private static final String viewQuery = "query";
    private static final String viewDate = "date";
    private static final String viewItems = "items";
    private static final String viewError = "error";

    @RequestMapping(value = "/object/objectList.html")
    public ModelAndView showObjects(HttpServletRequest req) {
        //Three cases: filter by datatype, by query or no search
        ModelAndView mav;
        if (req.getParameter(paramSearchByDatatype) != null && req.getParameter(paramSearchByQuery) == null) {
            mav = showAllByDatatype(req.getParameter(paramSearchByDatatype));
        } else if (req.getParameter(paramSearchByDatatype) == null && req.getParameter(paramSearchByQuery) != null) {
            mav = showByQuery(req);
        } else if (req.getParameter(paramSearchByDatatype) == null && req.getParameter(paramSearchByQuery) == null) {
            mav = showEmpty();
        } else {
            throw new IllegalArgumentException("User must provide parameter " + paramSearchByDatatype + " or " + paramSearchByQuery);
        }
        return mav;
    }

    private Object processFormDate(HttpServletRequest req, String property, List<String> error, Map<String, Object> data) {
        Object value = null;
        String parameter = req.getParameter(property);
        if (!esNuloOVacio(parameter)) {
            String pattern = "dd/MM/yyyy";
            if (Locale.US.equals(req.getLocale())) {
                pattern = "MM/dd/yyyy";
            }
            if (parameter.length() > 10) {
                pattern += " HH:mm";
            }
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                value = df.parse(parameter);
                data.put(property, "DATE|" + parameter);
            } catch (ParseException ex) {
                error.add("Property " + property + " has a non valid value. Must be a date");
            }
        }
        return value;
    }

    private Object processFormDouble(HttpServletRequest req, String property, List<String> error, Map<String, Object> data) {
        Object value = null;
        String parameter = req.getParameter(property);
        try {
            if (!esNuloOVacio(parameter)) {
                value = Double.valueOf(parameter);
                data.put(property, "DOUBLE|" + parameter);
            }
        } catch (NumberFormatException e) {
            error.add("Property " + property + " has a non valid value. Must be a double");
        }
        return value;
    }

    private Object processFormFloat(HttpServletRequest req, String property, List<String> error, Map<String, Object> data) {
        Object value = null;
        String parameter = req.getParameter(property);
        try {
            if (!esNuloOVacio(parameter)) {
                value = Float.valueOf(parameter);
                data.put(property, "FLOAT|" + parameter);
            }
        } catch (NumberFormatException e) {
            error.add("Property " + property + " has a non valid value. Must be a float");
        }
        return value;
    }

    private Object processFormInteger(HttpServletRequest req, String property, List<String> error, Map<String, Object> data) {
        Object value = null;
        String parameter = req.getParameter(property);
        try {
            if (!esNuloOVacio(parameter)) {
                value = Integer.valueOf(parameter);
                data.put(property, "INTEGER|" + parameter);
            }
        } catch (NumberFormatException e) {
            error.add("Property " + property + " has a non valid value.Must be an integer");
        }
        return value;
    }

    private Object processFormList(MultipartHttpServletRequest req, String property, DataType dt, List<String> error, DataObject objectProcessing, Map<String, Object> data) {
        Object value = null;
        String[] parameters = req.getParameterValues(property);
        if (parameters == null) {
            parameters = new String[0];
        }
        DataType listDataType = dt.getListDataType(property);
        if (listDataType == null) {
            //Caso de objeto indeterminado
            data.put("SUBTYPE_" + property, "");
            List<DataObject> aux = new ArrayList<DataObject>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                if (!esNuloOVacio(parameter)) {
                    DataObject obj = serviceDataObject.getById(Integer.parseInt(parameter));
                    //Si se apunta a si mismo
                    if (obj.getName().equals(objectProcessing.getName()) && obj.getDataType().equals(objectProcessing.getDataType())) {
                        aux.add(objectProcessing);
                    } else {
                        aux.add(obj);
                    }
                    datosMapa.add(obj.getDataType().getType() + "|" + parameter + "|0|" + obj.getDataType().getType() + ":" + obj.getName());
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.STRINGTYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "STRING");
            List<String> aux = new ArrayList<String>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                if (!esNuloOVacio(parameter)) {
                    aux.add(parameter);
                    datosMapa.add("STRING|" + parameter);
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.INTEGERTYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "INTEGER");
            List<Integer> aux = new ArrayList<Integer>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                try {
                    if (!esNuloOVacio(parameter)) {
                        datosMapa.add("INTEGER|" + parameter);
                        aux.add(Integer.valueOf(parameter));
                    }
                } catch (NumberFormatException e) {
                    error.add("Property " + property + " has a non valid value.Must be an integer");
                }

            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.LONGTYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "LONG");
            List<Long> aux = new ArrayList<Long>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                try {
                    if (!esNuloOVacio(parameter)) {
                        datosMapa.add("LONG|" + parameter);
                        aux.add(Long.valueOf(parameter));
                    }
                } catch (NumberFormatException e) {
                    error.add("Property " + property + " has a non valid value.Must be a long");
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.DOUBLETYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "DOUBLE");
            List<Double> aux = new ArrayList<Double>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                try {
                    if (!esNuloOVacio(parameter)) {
                        datosMapa.add("DOUBLE|" + parameter);
                        aux.add(Double.valueOf(parameter));
                    }
                } catch (NumberFormatException e) {
                    error.add("Property " + property + " has a non valid value. Must be a double");
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.FLOATTYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "FLOAT");
            List<Float> aux = new ArrayList<Float>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                try {
                    if (!esNuloOVacio(parameter)) {
                        datosMapa.add("FLOAT|" + parameter);
                        aux.add(Float.valueOf(parameter));
                    }
                } catch (NumberFormatException e) {
                    error.add("Property " + property + " has a non valid value. Must be a float");
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.DATETYPE.equals(listDataType.getName())) {
            data.put("SUBTYPE_" + property, "DATE");
            List<Date> aux = new ArrayList<Date>();
            List<String> datosMapa = new ArrayList<String>();
            for (String parameter : parameters) {
                try {
                    if (!esNuloOVacio(parameter)) {
                        String pattern = "dd/MM/yyyy";
                        if (Locale.US.equals(req.getLocale())) {
                            pattern = "MM/dd/yyyy";
                        }
                        if (parameter.length() > 10) {
                            pattern += " HH:mm";
                        }
                        SimpleDateFormat df = new SimpleDateFormat(pattern);
                        datosMapa.add("DATE|" + parameter);
                        aux.add(df.parse(parameter));
                    }
                } catch (ParseException ex) {
                    error.add("Property " + property + " has a non valid value. Must be a date");
                }
            }
            if (!aux.isEmpty()) {
                value = aux;
                data.put(property, datosMapa);
            } else {
                value = null;
            }
        } else if (DataTypeConstant.FILETYPE.equals(listDataType.getName())) {
            //Se recibiran como property_index
            data.put("SUBTYPE_" + property, DataTypeConstant.FILETYPE);
            List<MapStoreFileItem> files = new ArrayList<MapStoreFileItem>();
            List<String> dataMap = new ArrayList<String>();
            boolean continuar = true;
            int index = 0;
            do {
                MultipartFile file = req.getFile(property);
                if (file != null && file.getSize()>0) {
                    dataMap.add(DataTypeConstant.FILETYPE + "|" + file.getOriginalFilename());
                    try {
                        files.add(new MapStoreFileItem(file.getOriginalFilename(), file.getBytes()));
                    } catch (IOException ex) {
                        error.add("Error processing file " + file.getOriginalFilename() + " on property " + property);
                    }
                    index++;
                } else {
                    continuar = false;
                }
            } while (continuar);
            if (files.isEmpty()) {
                value = null;
                data.put(property, dataMap);
            } else {
                value = files;
            }
        } else {
            //Caso de objeto
            data.put("SUBTYPE_" + property, listDataType.getName());
            try {
                List<DataObject> aux = new ArrayList<DataObject>();
                List<String> datosMapa = new ArrayList<String>();
                for (String parameter : parameters) {
                    if (!esNuloOVacio(parameter)) {
                        DataObject obj = serviceDataObject.getById(Integer.parseInt(parameter));
                        if (listDataType.getName().equals(obj.getDataType().getName())) {
                            //Si se apunta a si mismo
                            if (obj.getName().equals(objectProcessing.getName()) && obj.getDataType().equals(objectProcessing.getDataType())) {
                                aux.add(objectProcessing);
                            } else {
                                aux.add(obj);
                            }
                            datosMapa.add(obj.getDataType().getName() + "|" + parameter + "|0|" + obj.getDataType().getName() + ": " + obj.getName());
                        } else {
                            error.add("Property " + property + " has a non valid value. Must be a " + listDataType.getName());
                        }
                    }
                }
                if (!aux.isEmpty()) {
                    value = aux;
                    data.put(property, datosMapa);
                } else {
                    value = null;
                }
            } catch (NumberFormatException e) {
                error.add("Property " + property + " has a non valid value. Must be a float");
            }
        }
        return value;
    }

    private Object processFormLong(HttpServletRequest req, String property, List<String> error, Map<String, Object> data) {
        Object value = null;
        String parameter = req.getParameter(property);
        try {
            if (!esNuloOVacio(parameter)) {
                value = Long.valueOf(parameter);
                data.put(property, "LONG|" + parameter);
            }
        } catch (NumberFormatException e) {
            error.add("Property " + property + " has a non valid value.Must be a long");
        }
        return value;
    }

    private Object processFormMap(Map<String, Object> data, String property, DataType dt, MultipartHttpServletRequest req, List<String> error) throws NumberFormatException, MapStoreRunTimeException {
        Object value;
        data.put("TYPE_" + property, "MAP");
        DataType keyType = dt.getMapKeyDataType(property);
        DataType valueType = dt.getMapValueDataType(property);
        int REFERENCE = 0;
        int FILE = 1;
        int VALUE = 2;
        int typeKey = VALUE;
        int typeValue = VALUE;
        if (keyType == null) {
            typeKey = REFERENCE;
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
        } else if (DataTypeConstant.FILETYPE.equals(keyType.getName())) {
            typeKey = FILE;
            data.put("KEYTYPE_" + property, DataTypeConstant.FILETYPE);
        } else {
            typeKey = REFERENCE;
            data.put("KEYTYPE_" + property, keyType.getName());
        }
        if (valueType == null) {
            typeValue = REFERENCE;
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
        } else if (DataTypeConstant.FILETYPE.equals(valueType.getName())) {
            typeValue = FILE;
            data.put("VALUETYPE_" + property, DataTypeConstant.FILETYPE);
        } else {
            typeValue = REFERENCE;
            data.put("VALUETYPE_" + property, valueType.getName());
        }
        boolean hasNext = true;
        int index = 0;
        Map<String, String> datosMapa = new HashMap<String, String>();
        Map<Object, Object> values = new HashMap<Object, Object>();
        do {
            boolean errorOnKeyValue = false;
            Object keyValue = null;
            Object valueValue = null;
            String keyMap = null;
            String valueMap = null;
            Object keyParameter = (typeKey == FILE) ? req.getFile(property + "_key_" + index) : req.getParameter(property + "_key_" + index);
            if (keyParameter != null) {
                Object valueParameter = (typeValue == FILE) ? req.getFile(property + "_value_" + index) : req.getParameter(property + "_value_" + index);
                if ("".equals(keyParameter)) {
                    keyParameter = null;
                }
                if ("".equals(valueParameter)) {
                    valueParameter = null;
                }
                if (typeKey == REFERENCE) {
                    if (keyParameter != null) {
                        DataObject reference = serviceDataObject.getById(Integer.parseInt((String) keyParameter));
                        keyMap = reference.getDataType().getName() + "|" + keyParameter + "|0|" + reference.getDataType().getName() + ":" + reference.getName();
                        if (keyType != null && !keyType.getName().equals(reference.getDataType().getName())) {
                            error.add("Property " + property + " has a non valid key.Must be a" + keyType.getName());
                            errorOnKeyValue = true;
                        } else {
                            keyValue = reference;
                        }
                    } else {
                        keyMap = keyType.getName() + "|||";
                    }
                } else if (typeKey == FILE) {
                    MultipartFile file = (MultipartFile) keyParameter;
                    if (file.getSize() >0 ) {
                        keyMap = DataTypeConstant.FILETYPE + "|" + file.getOriginalFilename();
                        keyValue = null;
                        try {
                            keyValue = new MapStoreFileItem(file.getOriginalFilename(), file.getBytes());
                        } catch (IOException ex) {
                            error.add("Error processing file " + file.getOriginalFilename() + " on property " + property);
                        }
                    }
                } else {
                    if (DataTypeConstant.STRINGTYPE.equals(keyType.getName())) {
                        keyValue = keyParameter;
                        keyMap = "STRING|" + ((keyValue == null) ? "" : keyValue);
                    } else if (DataTypeConstant.INTEGERTYPE.equals(keyType.getName())) {
                        if (keyParameter != null) {
                            try {
                                keyMap = "INTEGER|" + keyParameter;
                                keyValue = Integer.valueOf((String) keyParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid key.Must be an integer");
                            }
                        } else {
                            keyMap = "INTEGER|";
                        }
                    } else if (DataTypeConstant.LONGTYPE.equals(keyType.getName())) {
                        if (keyParameter != null) {
                            try {
                                keyMap = "LONG|" + keyParameter;
                                keyValue = Long.valueOf((String) keyParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid key.Must be an integer");
                            }
                        } else {
                            keyMap = "LONG|";
                        }
                    } else if (DataTypeConstant.DOUBLETYPE.equals(keyType.getName())) {
                        if (keyParameter != null) {
                            try {
                                keyMap = "DOUBLE|" + keyParameter;
                                keyValue = Double.valueOf((String) keyParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid key.Must be a number");
                            }
                        } else {
                            keyMap = "DOUBLE|";
                        }
                    } else if (DataTypeConstant.FLOATTYPE.equals(keyType.getName())) {
                        if (keyParameter != null) {
                            try {
                                keyMap = "FLOAT|" + keyParameter;
                                keyValue = Float.valueOf((String) keyParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid key.Must be a number");
                            }
                        } else {
                            keyMap = "FLOAT|";
                        }
                    } else if (DataTypeConstant.DATETYPE.equals(keyType.getName())) {
                        if (keyParameter != null) {
                            keyMap = "DATE|" + keyParameter;
                            String pattern = "dd/MM/yyyy";
                            if (Locale.US.equals(req.getLocale())) {
                                pattern = "MM/dd/yyyy";
                            }
                            if (((String) keyParameter).length() > 10) {
                                pattern += " HH:mm";
                            }
                            SimpleDateFormat df = new SimpleDateFormat(pattern);
                            try {
                                keyValue = df.parse((String) keyParameter);
                            } catch (ParseException ex) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value. Must be a date");
                            }
                        } else {
                            keyMap = "DATE|";
                        }
                    }
                }

                if (typeValue == REFERENCE) {
                    if (valueParameter != null) {
                        DataObject reference = serviceDataObject.getById(Integer.parseInt((String) valueParameter));
                        valueMap = reference.getDataType().getName() + "|" + valueParameter + "|0|" + reference.getDataType().getName() + ":" + reference.getName();
                        if (valueType != null && !valueType.getName().equals(reference.getDataType().getName())) {
                            error.add("Property " + property + " has a non valid value.Must be a" + valueType.getName());
                            errorOnKeyValue = true;
                        } else {
                            valueValue = reference;
                        }
                    } else {
                        valueMap = valueType.getName() + "|||";
                    }
                } else if (typeValue == FILE) {
                    MultipartFile file = (MultipartFile) valueParameter;
                    if (file.getSize() > 0) {
                    valueMap = DataTypeConstant.FILETYPE + "|" + file.getOriginalFilename();
                    valueValue = null;
                    try {
                        valueValue = new MapStoreFileItem(file.getOriginalFilename(), file.getBytes());
                    } catch (IOException ex) {
                        error.add("Error processing file " + file.getOriginalFilename() + " on property " + property);
                    }
                    }
                } else {
                    if (DataTypeConstant.STRINGTYPE.equals(valueType.getName())) {
                        valueValue = valueParameter;
                        valueMap = "STRING|" + ((valueValue == null) ? "" : valueValue);
                    } else if (DataTypeConstant.INTEGERTYPE.equals(valueType.getName())) {
                        if (valueParameter != null) {
                            try {
                                valueMap = "INTEGER|" + valueParameter;
                                valueValue = Integer.valueOf((String) valueParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value.Must be an integer");
                            }
                        } else {
                            valueMap = "INTEGER|";
                        }
                    } else if (DataTypeConstant.LONGTYPE.equals(valueType.getName())) {
                        if (valueParameter != null) {
                            try {
                                valueMap = "LONG|" + valueParameter;
                                valueValue = Long.valueOf((String) valueParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value.Must be an integer");
                            }
                        } else {
                            valueMap = "LONG|";
                        }
                    } else if (DataTypeConstant.DOUBLETYPE.equals(valueType.getName())) {
                        if (valueParameter != null) {
                            try {
                                valueMap = "DOUBLE|" + valueParameter;
                                valueValue = Double.valueOf((String) valueParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value.Must be a number");
                            }
                        } else {
                            valueMap = "DOUBLE|";
                        }
                    } else if (DataTypeConstant.FLOATTYPE.equals(valueType.getName())) {
                        if (valueParameter != null) {
                            try {
                                valueMap = "FLOAT|" + valueParameter;
                                valueValue = Float.valueOf((String) valueParameter);
                            } catch (NumberFormatException e) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value.Must be a number");
                            }
                        } else {
                            valueMap = "FLOAT|";
                        }
                    } else if (DataTypeConstant.DATETYPE.equals(valueType.getName())) {
                        if (valueParameter != null) {
                            valueMap = "DATE|" + valueParameter;
                            String pattern = "dd/MM/yyyy";
                            if (Locale.US.equals(req.getLocale())) {
                                pattern = "MM/dd/yyyy";
                            }
                            if (((String) valueParameter).length() > 10) {
                                pattern += " HH:mm";
                            }
                            SimpleDateFormat df = new SimpleDateFormat(pattern);
                            try {
                                valueValue = df.parse((String) valueParameter);
                            } catch (ParseException ex) {
                                errorOnKeyValue = true;
                                error.add("Property " + property + " has a non valid value. Must be a date");
                            }
                        } else {
                            valueMap = "DATE|";
                        }
                    }
                }
                if (!errorOnKeyValue) {
                    values.put(keyValue, valueValue);
                }
                datosMapa.put(keyMap, valueMap);
            } else {
                hasNext = false;
            }
            index++;
        } while (hasNext);
        data.put(property, datosMapa);
        if (!values.isEmpty()) {
            value = values;
        } else {
            value = null;
        }
        return value;
    }

    private Object processFormString(HttpServletRequest req, String property, Map<String, Object> data) {
        Object value = null;
        //Caso String
        String parameter = req.getParameter(property);
        if (!esNuloOVacio(parameter)) {
            value = parameter;
            data.put(property, "STRING|" + value);
        }
        return value;
    }

    private ModelAndView showAllByDatatype(String parameter) {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = "_TYPE = " + parameter;
        Date d = null;
        mav.addObject(viewQuery, query);
        List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query, d);
        mav.addObject(viewItems, items);
        return mav;
    }

    private ModelAndView showByQuery(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = req.getParameter(paramSearchByQuery);
        String date = req.getParameter(paramSearchDate);
        mav.addObject(viewQuery, query);
        mav.addObject(viewDate, date);
        Date d = null;
        if (date != null) {
            try {
                d = processDate(date);
                List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query, d);
                mav.addObject(viewItems, items);
            } catch (ParseException ex) {
                mav.addObject(viewError, "Error parsing date");
            }
        }
        return mav;
    }

    private ModelAndView showEmpty() {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = "";
        mav.addObject(viewQuery, query);
        List<MapStoreExtendedItem<DataObject>> items = new ArrayList<MapStoreExtendedItem<DataObject>>();
        mav.addObject(viewItems, items);
        return mav;
    }

    private Date processDate(String date) throws ParseException {
        //TODO: internacionalizar
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.parse(date);
    }
    private static String parameterId = "id";
    private static String parameterVersion = "version";
    private static String viewDataTypes = "datatypes"; //List of datatypes without predefined types
    private static String errors = "error"; //List of errors encountered while parsinf object
    private static String dataobject = "dataobject"; //DataObject to process
    private static String viewEditingItem = "editItem"; //List of datatypes without predefined types

    @RequestMapping(value = "/object/newDataObject.html")
    public ModelAndView viewNewDataObject() {
        ModelAndView mav = new ModelAndView("/object/editNewObject");
        List<DataType> datatypes = serviceDataType.getAll();
        for (Iterator<DataType> it = datatypes.iterator(); it.hasNext();) {
            DataType dt = it.next();
            if (DataTypeConstant.BASICTYPES.contains(dt.getName())) {
                it.remove();
            }
        }
        mav.addObject(viewDataTypes, datatypes);
        return mav;
    }

    @RequestMapping(value = "/object/editDataObject.html")
    public ModelAndView viewEditDataObject(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter("id"));
        ModelAndView mav = new ModelAndView("/object/editObject");
        mav.addAllObjects(serviceDataObject.showObject(id, req.getLocale()));
        return mav;
    }

    @RequestMapping(value = "/object/getFormForNewObject.html")
    public void getFormForNew(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String datatype = req.getParameter("datatype");
        DataType dt = serviceDataType.getDataType(datatype);
        res.getWriter().write(DataObjectControllerHelperForNewObjectForm.createFormForNewObject(dt));
    }

    @RequestMapping(value = "/object/insertObject.html")
    public ModelAndView createObject(MultipartHttpServletRequest req) {
        List<String> errorsList = new ArrayList<String>();
        Map<String, Object> params = new HashMap<String, Object>();
        DataObject object = processForm(req, errorsList, params);

        ModelAndView mav;
        if (!errorsList.isEmpty()) {
            //Hay errores.Generar vista cargada con los datos correctos
            mav = new ModelAndView("/object/editNewObject2");
            mav.addAllObjects(params);
            List<DataType> datatypes = serviceDataType.getAll();
            for (Iterator<DataType> it = datatypes.iterator(); it.hasNext();) {
                DataType dt = it.next();
                if (DataTypeConstant.BASICTYPES.contains(dt.getName())) {
                    it.remove();
                }
            }
            mav.addObject(viewDataTypes, datatypes);
            mav.addObject(errors, errorsList);
        } else {
            //Añadir a BBDD
            serviceDataObject.createDataObject(object);
            //Mostrar vista
            String name = object.getDataType().getName();
            mav = new ModelAndView(new RedirectView("/object/objectList.html?id=" + name, true));
            System.out.println("NO HAY ERRORES");
        }
        return mav;
    }

    @RequestMapping(value = "/object/updateObject.html")
    public ModelAndView updateObject(MultipartHttpServletRequest req) {
        List<String> errorsList = new ArrayList<String>();
        Map<String, Object> params = new HashMap<String, Object>();
        DataObject object = processForm(req, errorsList, params);
        ModelAndView mav;

        if (!errorsList.isEmpty()) {
            //Hay errores.Generar vista cargada con los datos correctos
            mav = new ModelAndView("/object/editObject");
            mav.addAllObjects(params);
            List<DataType> datatypes = serviceDataType.getAll();
            for (Iterator<DataType> it = datatypes.iterator(); it.hasNext();) {
                DataType dt = it.next();
                if (DataTypeConstant.BASICTYPES.contains(dt.getName())) {
                    it.remove();
                }
            }
            mav.addObject(viewDataTypes, datatypes);
            mav.addObject(errors, errorsList);

        } else {
            //Añadir a BBDD
            serviceDataObject.updateDataObject(object);
            //Mostrar vista
            String name = object.getDataType().getName();
            mav = new ModelAndView(new RedirectView("/object/objectList.html?id=" + name, true));
            System.out.println("NO HAY ERRORES");
        }
        return mav;
    }

    private DataObject processForm(MultipartHttpServletRequest req, List<String> error, Map<String, Object> data) {
        String datatype = req.getParameter("datatype");
        String nombre = req.getParameter("name");
        if (esNuloOVacio(nombre)) {
            error.add("No name has been defined");
        }
        DataType dt = serviceDataType.getDataType(datatype);
        DataObject object = new DataObject(nombre, dt);
        data.put("NAME", nombre);
        data.put("TYPE", datatype);
        Map<String, DataType> atributos = dt.getAttributes();
        List<String> attrib = new ArrayList();
        data.put("ATTRIB", attrib);
        for (String property : atributos.keySet()) {
            attrib.add(property);
            DataType dtProperty = atributos.get(property);
            Object value = null;
            if (DataTypeConstant.STRINGTYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "STRING");
                value = processFormString(req, property, data);
            } else if (DataTypeConstant.INTEGERTYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "INTEGER");
                value = processFormInteger(req, property, error, data);
            } else if (DataTypeConstant.LONGTYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "LONG");
                value = processFormLong(req, property, error, data);
            } else if (DataTypeConstant.DOUBLETYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "DOUBLE");
                value = processFormDouble(req, property, error, data);
            } else if (DataTypeConstant.FLOATTYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "FLOAT");
                value = processFormFloat(req, property, error, data);
            } else if (DataTypeConstant.DATETYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "DATE");
                value = processFormDate(req, property, error, data);
            } else if (DataTypeConstant.LISTTYPE.equals(dtProperty.getName())) {
                data.put("TYPE_" + property, "LIST");
                value = processFormList(req, property, dt, error, object, data);
            } else if (DataTypeConstant.MAPTYPE.equals(dtProperty.getName())) {
                value = processFormMap(data, property, dt, req, error);
            } else if (DataTypeConstant.FILETYPE.equals(dtProperty.getName())) {
                value = processFormFile(data, property, req, error);
            } else {
                data.put("TYPE_" + property, dtProperty.getName());
                String parameter = req.getParameter(property);
                if (!esNuloOVacio(parameter)) {
                    DataObject obj = serviceDataObject.getById(Integer.parseInt(parameter));
                    if (dtProperty.getName().equals(obj.getDataType().getName())) {
                        //Si se apunta a si mismo
                        if (obj.getName().equals(object.getName()) && obj.getDataType().equals(object.getDataType())) {
                            value = object;
                        } else {
                            value = obj;
                        }
                        data.put(property, obj.getDataType().getName() + "|" + parameter + "|0|" + obj.getDataType().getName() + ": " + obj.getName());
                    } else {
                        error.add("Property " + property + " has a non valid value. Must be a " + dtProperty.getName());
                    }
                }
            }
            if (value != null) {
                object.put(property, value);
            }
        }
        return object;
    }

    private boolean esNuloOVacio(String s) {
        return (s == null || "".equals(s));
    }

    @RequestMapping(value = "/object/showObject.html")
    public ModelAndView showObject(HttpServletRequest req) {
        String verStr = req.getParameter("version");
        String idStr = req.getParameter("id");
        if (esNuloOVacio(verStr) || esNuloOVacio(idStr)) {
            return showEmpty();
        }
        ModelAndView mav = new ModelAndView("/object/showObject");
        mav.addAllObjects(serviceDataObject.showObject(Integer.parseInt(idStr), Integer.parseInt(verStr), req.getLocale()));
        return mav;
    }

    @RequestMapping(value = "/object/popupObject.html")
    public ModelAndView popUpForObject(HttpServletRequest req) {
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String q = "";
        if (!esNuloOVacio(type)) {
            q = "_TYPE = " + type;
        }
        if (!esNuloOVacio(query)) {
            if (!esNuloOVacio(q)) {
                q += " AND ";
            }
            q += query;
        }
        ModelAndView mav = new ModelAndView("/object/popupObject");
        if (!esNuloOVacio(q)) {
            List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(q, null);
            mav.addObject("items", items);
        }
        mav.addObject("type", type);
        mav.addObject("query", q);
        return mav;
    }

    @RequestMapping(value = "/object/popupObjectList.html")
    public ModelAndView popUpForObjectList(HttpServletRequest req) {
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String q = "";
        if (!esNuloOVacio(type)) {
            q = "_TYPE = " + type;
        }
        if (!esNuloOVacio(query)) {
            if (!esNuloOVacio(q)) {
                q += " AND ";
            }
            q += query;
        }
        ModelAndView mav = new ModelAndView("/object/popupObjectList");
        if (!esNuloOVacio(q)) {
            List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(q, null);
            mav.addObject("items", items);
        }
        mav.addObject("type", type);
        mav.addObject("query", q);
        return mav;
    }

    @RequestMapping(value = "/object/popupMapSearch.html")
    public ModelAndView popUpForMapObject(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("/object/popupMapObject");
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String q = "";
        if (!esNuloOVacio(type)) {
            q = "_TYPE = " + type;
            mav.addObject("type", type);
        }
        if (!esNuloOVacio(query)) {
            if (!esNuloOVacio(q)) {
                q += " AND ";
            }
            q += query;
            mav.addObject("query", query);
        }
        if (!esNuloOVacio(q)) {
            mav.addObject("items", serviceDataObject.find(q, null));
        }
        return mav;
    }

    private MapStoreFileItem processFormFile(Map<String, Object> data, String property, MultipartHttpServletRequest req, List<String> error) {
        data.put("TYPE_" + property, DataTypeConstant.FILETYPE);
        MapStoreFileItem fileItem = null;
        MultipartFile file = null;
        if (file != null && file.getSize() > 0) {
            data.put(property, DataTypeConstant.FILETYPE + "|" + file.getOriginalFilename());
            try {
                fileItem = new MapStoreFileItem(file.getOriginalFilename(), file.getBytes());
            } catch (IOException ex) {
                error.add("Error processing file " + file.getOriginalFilename() + " on property " + property);
            }
        }
        return fileItem;

    }
}

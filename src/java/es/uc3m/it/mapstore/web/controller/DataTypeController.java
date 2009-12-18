/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.web.services.DataTypeService;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import es.uc3m.it.mapstore.web.util.JSTLConstants;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author Pablo
 */
@Controller
public class DataTypeController {
    private DataTypeService service;

    @Autowired
    public DataTypeController(DataTypeService service) {
        this.service = service;
    }

    public DataTypeService getService() {
        return service;
    }

    public void setService(DataTypeService service) {
        this.service = service;
    }

    @RequestMapping(value= "/datatype/list.html")
    public ModelAndView listAllDatatypes() {
        ModelAndView mav = new ModelAndView("datatype/listDatatypes");
        List<DataType> datatypes = service.getAll();
        Collections.sort(datatypes, new DatatypesSorter());
        mav.addObject("datatypes", datatypes);
        return mav;
    }

    @RequestMapping(value= "/datatype/newDataType.html")
    public ModelAndView viewNewDatatype() {
        ModelAndView mav = prepareDatatypeEditView();
        return mav;
    }

    @RequestMapping(value= "/datatype/editDatatype.html")
    public ModelAndView viewDatatype(HttpServletRequest req) {
        ModelAndView mav = prepareDatatypeEditView();
        DataType dt = service.getDataType(req.getParameter("id"));
        mav.addObject("name", dt.getName());
        mav.addObject("properties", dt.getAttributes());
        mav.addObject("edit", true);
        return mav;
    }

    @RequestMapping(value= "/datatype/updateDataType.html")
    public ModelAndView updateDatatype(HttpServletRequest req) {
        ModelAndView mav = null;
        try {
            DataType dt = validateForm(req.getParameterMap());
            service.updateDataType(dt);
        } catch (IllegalArgumentException ex) {
            String error = ex.getMessage();
            mav = prepareDatatypeEditView();
            DataType dt = validateFormWithErrors(req.getParameterMap());
            mav.addObject("name", dt.getName());
            mav.addObject("properties", dt.getAttributes());
            mav.addObject("edit", true);
            mav.addObject("error",error);
        }
        if (mav == null) mav = new ModelAndView(new RedirectView("/datatype/list.html"));
        return mav;
    }
    @RequestMapping(value= "/datatype/createDataType.html")
    public ModelAndView createDatatype(HttpServletRequest req) {
        ModelAndView mav = null;
        try {
            DataType dt = validateForm(req.getParameterMap());
            service.createDataType(dt);
        } catch (IllegalArgumentException ex) {
            String error = ex.getMessage();
            mav = prepareDatatypeEditView();
            DataType dt = validateFormWithErrors(req.getParameterMap());
            mav.addObject("name", dt.getName());
            mav.addObject("properties", dt.getAttributes());
            mav.addObject("error",error);
        }
        if (mav == null) mav = new ModelAndView(new RedirectView("/datatype/list.html"));
        return mav;
    }

    private ModelAndView prepareDatatypeEditView() {
        ModelAndView mav = new ModelAndView("datatype/editDataType");
        List<DataType> datatypes = service.getAll();
        Collections.sort(datatypes, new DatatypesSorter());
        mav.addObject("datatypes", datatypes);
        mav.addObject("constant", new JSTLConstants(DataTypeConstant.class));
        return mav;
    }

    private DataType validateForm(Map parameterMap) throws IllegalArgumentException {
        List<DataType> datatypes = service.getAll();
        Map<String,DataType> aux = new HashMap<String, DataType>();
        for (DataType dt : datatypes) {
            aux.put(dt.getName(), dt);
        }
        String name = ((String[])parameterMap.get("name"))[0];
        if (esNuloOVacio(name)) throw new IllegalArgumentException("Datatype name has not been defined");
        Map<String,DataType> atrib = new HashMap<String, DataType>();
        if (parameterMap.get("pk") == null) throw new IllegalArgumentException("Primary key has not been defined");
        String pk = ((String[])parameterMap.get("pk"))[0];
        String pkValue = ((String[])parameterMap.get(pk))[0];
        int indexExtra = 0;
        for (Object a : parameterMap.keySet()) {
            String propName = (String)a;
            if (propName.startsWith("propertyName")) {
                String nombreAtributo = ((String[])parameterMap.get(a))[0];
                if (esNuloOVacio(nombreAtributo)) throw new IllegalArgumentException("Property name has not been definied");
                String propType = propName.replaceAll("propertyName", "propertyType");
                String tipoAtributo = ((String[])parameterMap.get(propType))[0];
                DataType dt = aux.get(tipoAtributo);
                if (dt == null) throw new IllegalArgumentException("Property "+ nombreAtributo+" has an invalid Datatype");
                atrib.put(nombreAtributo, dt);
                //Process Map and List types
                if (DataTypeConstant.LISTTYPES.contains(dt.getName())) {
                    String propMapKey = propName.replaceAll("propertyName", "propertyMapKeyType");
                    String key = ((String[])parameterMap.get(propMapKey))[0];
                    dt = aux.get(key);
                    dt.setMapKeyDataType(nombreAtributo, dt);
                    indexExtra++;
                } else if  (DataTypeConstant.MAPTYPES.contains(dt.getName())) {
                    String propMapKey = propName.replaceAll("propertyName", "propertyMapKeyType");
                    String key = ((String[])parameterMap.get(propMapKey))[0];
                    dt = aux.get(key);
                    dt.setMapKeyDataType(nombreAtributo, dt);
                    indexExtra++;
                    String propMapValue = propName.replaceAll("propertyName", "propertyMapValueType");
                    String value = ((String[])parameterMap.get(propMapValue))[0];
                    dt = aux.get(value);
                    dt.setMapValueDataType(nombreAtributo, dt);
                    indexExtra++;
                }

            }
        }
        if (atrib.size()*2+indexExtra+2 != parameterMap.size()) throw new IllegalArgumentException("Not all the parameters can be processed");
        DataType pkDT = atrib.get(pkValue);
        if (!pkDT.getName().equals(DataTypeConstant.STRINGTYPE)) throw new IllegalArgumentException("Primary key must be a String property");
        DataType dt = new DataType(name);
        dt.setPk(pkValue);
        dt.setAttributes(atrib);
        return dt;
    }

    private DataType validateFormWithErrors(Map parameterMap){
        List<DataType> datatypes = service.getAll();
        Map<String,DataType> aux = new HashMap<String, DataType>();
        for (DataType dt : datatypes) {
            aux.put(dt.getName(), dt);
        }
        String name = ((String[])parameterMap.get("name"))[0];
        Map<String,DataType> atrib = new HashMap<String, DataType>();
        String pkValue = null;
        if (parameterMap.get("pk") != null) {
            String pk = ((String[])parameterMap.get("pk"))[0];
            pkValue = ((String[])parameterMap.get(pk))[0];
        }
        for (Object a : parameterMap.keySet()) {
            String propName = (String)a;
            if (propName.startsWith("propertyName")) {
                String nombreAtributo = ((String[])parameterMap.get(a))[0];
                String propType = propName.replaceAll("propertyName", "propertyType");
                String tipoAtributo = ((String[])parameterMap.get(propType))[0];
                DataType dt = aux.get(tipoAtributo);
                atrib.put(nombreAtributo, dt);
                //Process Map and List types
                if (DataTypeConstant.LISTTYPES.contains(dt.getName())) {
                    String propMapKey = propName.replaceAll("propertyName", "propertyMapKeyType");
                    String key = ((String[])parameterMap.get(propMapKey))[0];
                    dt = aux.get(key);
                    dt.setMapKeyDataType(nombreAtributo, dt);
                } else if  (DataTypeConstant.MAPTYPES.contains(dt.getName())) {
                    String propMapKey = propName.replaceAll("propertyName", "propertyMapKeyType");
                    String key = ((String[])parameterMap.get(propMapKey))[0];
                    dt = aux.get(key);
                    dt.setMapKeyDataType(nombreAtributo, dt);
                    String propMapValue = propName.replaceAll("propertyName", "propertyMapValueType");
                    String value = ((String[])parameterMap.get(propMapValue))[0];
                    dt = aux.get(value);
                    dt.setMapValueDataType(nombreAtributo, dt);
                }
            }
        }
        DataType dt = new DataType(name);
        dt.setPk(pkValue);
        dt.setAttributes(atrib);
        return dt;
    }


    private boolean esNuloOVacio(String s) {
        return (s == null || "".equals(s));
    }

    private class DatatypesSorter implements Comparator<DataType> {
        public int compare(DataType o1, DataType o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}

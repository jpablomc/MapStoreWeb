/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.web.services.DataTypeService;
import es.uc3m.it.mapstore.web.beans.DataType;
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
        ModelAndView mav = new ModelAndView("datatype/editDataType");
        List<DataType> datatypes = service.getAll();
        Collections.sort(datatypes, new DatatypesSorter());
        mav.addObject("datatypes", datatypes);
        return mav;
    }

    @RequestMapping(value= "/datatype/editDatatype.html")
    public ModelAndView viewDatatype(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("datatype/editDataType");
        List<DataType> datatypes = service.getAll();
        DataType dt = service.getDataType(req.getParameter("id"));
        Collections.sort(datatypes, new DatatypesSorter());
        mav.addObject("datatypes", datatypes);
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
            mav = new ModelAndView("datatype/editDataType");
            List<DataType> datatypes = service.getAll();
            DataType dt = validateFormWithErrors(req.getParameterMap());
            Collections.sort(datatypes, new DatatypesSorter());
            mav.addObject("datatypes", datatypes);
            mav.addObject("name", dt.getName());
            mav.addObject("properties", dt.getAttributes());
            mav.addObject("edit", true);
            mav.addObject("error",error);
        }
        if (mav == null) mav = listAllDatatypes();
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
            mav = new ModelAndView("datatype/editDataType");
            List<DataType> datatypes = service.getAll();
            DataType dt = validateFormWithErrors(req.getParameterMap());
            Collections.sort(datatypes, new DatatypesSorter());
            mav.addObject("datatypes", datatypes);
            mav.addObject("name", dt.getName());
            mav.addObject("properties", dt.getAttributes());
            mav.addObject("error",error);
        }
        if (mav == null) mav = listAllDatatypes();
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
            }
        }
        if (atrib.size()*2+1 != parameterMap.size()) throw new IllegalArgumentException("Not all the parameters can be processed");
        DataType dt = new DataType(name);
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
        for (Object a : parameterMap.keySet()) {
            String propName = (String)a;
            if (propName.startsWith("propertyName")) {
                String nombreAtributo = ((String[])parameterMap.get(a))[0];
                String propType = propName.replaceAll("propertyName", "propertyType");
                String tipoAtributo = ((String[])parameterMap.get(propType))[0];
                DataType dt = aux.get(tipoAtributo);
                atrib.put(nombreAtributo, dt);
            }
        }
        DataType dt = new DataType(name);
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

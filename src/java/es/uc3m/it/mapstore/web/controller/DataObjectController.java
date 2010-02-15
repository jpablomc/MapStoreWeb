/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import es.uc3m.it.mapstore.bean.MapStoreExtendedItem;
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value= "/object/objectList.html")
    public ModelAndView showObjects(HttpServletRequest req) {
        //Three cases: filter by datatype, by query or no search
        ModelAndView mav;
        if (req.getParameter(paramSearchByDatatype) != null && req.getParameter(paramSearchByQuery) == null) mav = showAllByDatatype(req.getParameter(paramSearchByDatatype));
        else if (req.getParameter(paramSearchByDatatype) == null && req.getParameter(paramSearchByQuery) != null) mav = showByQuery(req);
        else if (req.getParameter(paramSearchByDatatype) == null && req.getParameter(paramSearchByQuery) == null) mav = showEmpty();
        else throw new IllegalArgumentException("User must provide parameter " + paramSearchByDatatype + " or " + paramSearchByQuery);
        return mav;
    }

    private ModelAndView showAllByDatatype(String parameter) {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = "_TYPE = " + parameter;
        Date d = null;
        mav.addObject(viewQuery, query);
        List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query,d);
        mav.addObject(viewItems,items);
        return mav;
    }

    private ModelAndView showByQuery(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = req.getParameter(paramSearchByQuery);
        String date = req.getParameter(paramSearchDate);
        mav.addObject(viewQuery, query);
        mav.addObject(viewDate, date);
        Date d = null;
        if (date != null) try {
            d = processDate(date);
            List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query,d);
            mav.addObject(viewItems,items);
        } catch (ParseException ex) {
            mav.addObject(viewError, "Error parsing date");
        }
        return mav;
    }

    private ModelAndView showEmpty() {
        ModelAndView mav = new ModelAndView("/object/objectList");
        String query = "";
        mav.addObject(viewQuery, query);
        List<MapStoreExtendedItem<DataObject>> items = new ArrayList<MapStoreExtendedItem<DataObject>>();
        mav.addObject(viewItems,items);
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

    @RequestMapping(value= "/object/newDataObject.html")
    public ModelAndView viewNewDataObject() {
        ModelAndView mav = new ModelAndView("/object/editNewObject");
        List<DataType> datatypes = serviceDataType.getAll();
        for (Iterator<DataType> it = datatypes.iterator();it.hasNext();) {
            DataType dt = it.next();
            if (DataTypeConstant.BASICTYPES.contains(dt.getName())) it.remove();
        }
        mav.addObject(viewDataTypes,datatypes);
        return mav;
    }

    @RequestMapping(value= "/object/editDataObject.html")
    public ModelAndView viewEditDataObject(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("/object/editObject");
        if (req.getParameter(parameterId) == null || req.getParameter(parameterVersion) == null)
            throw new IllegalArgumentException("User must provide parameter " + parameterId + " and " + parameterVersion);
        int id = Integer.parseInt(req.getParameter(parameterId));
        int version = Integer.parseInt(req.getParameter(parameterVersion));
        MapStoreExtendedItem<DataObject> editItem = serviceDataObject.getById(id);
        if (version != editItem.getVersion()) mav.addObject(viewError,"There is a newer version. Data is obtained from version " + version);
        return mav;
    }

    @RequestMapping(value= "/object/getFormForNewObject.html")
    public void getFormForNew(HttpServletRequest req,HttpServletResponse res) throws IOException {
        String datatype = req.getParameter("datatype");
        DataType dt = serviceDataType.getDataType(datatype);
        res.getWriter().write(DataObjectControllerHelper.createFormForNewObject(dt));
    }

    @RequestMapping(value= "/object/insertObject.html")
    public ModelAndView createObject(HttpServletRequest req) {
        List<String> errorsList = new ArrayList<String>();
        DataObject object = processForm(req, errorsList);
        ModelAndView mav;
        if (!errorsList.isEmpty()) {
            //Hay errores.Generar vista cargada con los datos correctos
            mav = new ModelAndView("/object/editNewObject");
            List<DataType> datatypes = serviceDataType.getAll();
            for (Iterator<DataType> it = datatypes.iterator();it.hasNext();) {
                DataType dt = it.next();
                if (DataTypeConstant.BASICTYPES.contains(dt.getName())) it.remove();
            }
            mav.addObject(viewDataTypes,datatypes);
            mav.addObject(errors,errorsList);
            mav.addObject(dataobject, object);
            System.out.println("HAY ERRORES");
            for (String aux : errorsList) {
                System.out.println(aux);
            }
        } else {
            //Añadir a BBDD
            serviceDataObject.createDataObject(object);
            //Mostrar vista
            String name = object.getDataType().getName();
            mav = new ModelAndView(new RedirectView("/object/objectList.html?id="+name,true));
            System.out.println("NO HAY ERRORES");
        }
        return mav;
    }

    private DataObject processForm(HttpServletRequest req, List<String> error) {
        String datatype = req.getParameter("datatype");
        String nombre = req.getParameter("name");
        if (esNuloOVacio(nombre)) error.add("No name has been defined");
        DataType dt = serviceDataType.getDataType(datatype);
        DataObject object = new DataObject(nombre, dt);
        Map<String,DataType> atributos = dt.getAttributes();
        for (String property : atributos.keySet()) {
            DataType dtProperty = atributos.get(property);
            Object value= null;
            if (DataTypeConstant.STRINGTYPE.equals(dtProperty.getName())) {
                //Caso String
                String parameter = req.getParameter(property);
                if (!esNuloOVacio(parameter)) value = parameter;
            } else if (DataTypeConstant.INTEGERTYPE.equals(dtProperty.getName())) {
                String parameter = req.getParameter(property);
                try {
                    if (!esNuloOVacio(parameter)) value = Integer.valueOf(parameter);
                } catch (NumberFormatException e) {
                    error.add("Property "+ property + " has a non valid value.Must be an integer");
                }
            } else if (DataTypeConstant.LONGTYPE.equals(dtProperty.getName())) {
                String parameter = req.getParameter(property);
                try {
                    if (!esNuloOVacio(parameter)) value = Long.valueOf(parameter);
                } catch (NumberFormatException e) {
                    error.add("Property "+ property + " has a non valid value.Must be a long");
                }
            } else if (DataTypeConstant.DOUBLETYPE.equals(dtProperty.getName())) {
                String parameter = req.getParameter(property);
                try {
                    if (!esNuloOVacio(parameter)) value = Double.valueOf(parameter);
                } catch (NumberFormatException e) {
                    error.add("Property "+ property + " has a non valid value. Must be a double");
                }
            } else if (DataTypeConstant.FLOATTYPE.equals(dtProperty.getName())) {
                String parameter = req.getParameter(property);
                try {
                    if (!esNuloOVacio(parameter)) value = Float.valueOf(parameter);
                } catch (NumberFormatException e) {
                    error.add("Property "+ property + " has a non valid value. Must be a float");
                }
            } else if (DataTypeConstant.DATETYPE.equals(dtProperty.getName())) {
                String parameter = req.getParameter(property);
                if (!esNuloOVacio(parameter)) {
                    String pattern = "dd/MM/yyyy";
                    if (Locale.US.equals(req.getLocale())) {
                        pattern = "MM/dd/yyyy";
                    }
                    if (parameter.length()> 10) {
                        pattern += " HH:mm";
                    }
                    SimpleDateFormat df = new SimpleDateFormat(pattern);
                    try {
                        value = df.parse(parameter);
                    } catch (ParseException ex) {
                        error.add("Property "+ property + " has a non valid value. Must be a date");
                    }
                }
            }
            object.put(property, value);
        }
        return object;
    }

    private boolean esNuloOVacio(String s) {
        return (s == null || "".equals(s));
    }
    
    @RequestMapping(value= "/object/showObject.html")
    public ModelAndView showObject(HttpServletRequest req) {
        String verStr = req.getParameter("version");
        String idStr = req.getParameter("id");
        if (esNuloOVacio(verStr) || esNuloOVacio(idStr)) {
            return showEmpty();
        }
        ModelAndView mav = new ModelAndView("/object/showObject");
        mav.addObject("data",serviceDataObject.showObject(Integer.parseInt(idStr), Integer.parseInt(verStr)));;
        return mav;
    }


    @RequestMapping(value="/object/popupObject.html")
    public ModelAndView popUpForObject(HttpServletRequest req) {
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String q = "";
        if (!esNuloOVacio(type)) q = "_TYPE = " + type;
        if (!esNuloOVacio(query)) {
            if (!esNuloOVacio(q)) q += " AND ";
            q += query;
        }
        ModelAndView mav = new ModelAndView("/object/popupObject");
        if (!esNuloOVacio(q)) {
            List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(q,null);
            mav.addObject("items", items);
        }
        mav.addObject("type", type);
        mav.addObject("query", q);
        return mav;
    }

    @RequestMapping(value="/object/popupObjectList.html")
    public ModelAndView popUpForObjectList(HttpServletRequest req) {
        String type = req.getParameter("type");
        String query = req.getParameter("query");
        String q = "";
        if (!esNuloOVacio(type)) q = "_TYPE = " + type;
        if (!esNuloOVacio(query)) {
            if (!esNuloOVacio(q)) q += " AND ";
            q += query;
        }
        ModelAndView mav = new ModelAndView("/object/popupObjectList");
        if (!esNuloOVacio(q)) {
            List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(q,null);
            mav.addObject("items", items);
        }
        mav.addObject("type", type);
        mav.addObject("query", q);
        return mav;
    }

    @RequestMapping(value="/object/popupMap.html")
    public ModelAndView popUpForMap(HttpServletRequest req) {
        ModelAndView mav = new ModelAndView("/object/popupMap");

        String typeKey = req.getParameter("typeKey");
        String queryKey = req.getParameter("queryKey");

        String typeValue = req.getParameter("typeValue");
        String queryValue = req.getParameter("queryValue");
        
        boolean isBasicKey = false;
        boolean isBasicValue = false;
        
        String query = "";

        if (!esNuloOVacio(typeKey)) {
            if (DataTypeConstant.DATETYPE.equals(typeKey) ||
                    DataTypeConstant.STRINGTYPE.equals(typeKey) ||
                    DataTypeConstant.INTEGERTYPE.equals(typeKey) ||
                    DataTypeConstant.LONGTYPE.equals(typeKey) ||
                    DataTypeConstant.DOUBLETYPE.equals(typeKey) ||
                    DataTypeConstant.FLOATTYPE.equals(typeKey)) {
                isBasicKey=true;
            } else {
                query = "_TYPE = " + typeKey;
            }
        }
        if (!isBasicKey) {
            if(!esNuloOVacio(queryKey)) {
                if(query.length()>0) query += " AND ";
                query += queryKey;
            }
            if (query.length()> 0 ) {
                List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query,null);
                mav.addObject("itemsKey", items);
            }
        }


        query = "";

        if (!esNuloOVacio(typeValue)) {
            if (DataTypeConstant.DATETYPE.equals(typeValue) ||
                    DataTypeConstant.STRINGTYPE.equals(typeValue) ||
                    DataTypeConstant.INTEGERTYPE.equals(typeValue) ||
                    DataTypeConstant.LONGTYPE.equals(typeValue) ||
                    DataTypeConstant.DOUBLETYPE.equals(typeValue) ||
                    DataTypeConstant.FLOATTYPE.equals(typeValue)) {
                isBasicValue=true;
            } else {
                query = "_TYPE = " + typeValue;
            }
        }
        if (!isBasicValue) {
            if(!esNuloOVacio(queryValue)) {
                if(query.length()>0) query += " AND ";
                query += queryValue;
            }
            if (query.length()> 0 ) {
                List<MapStoreExtendedItem<DataObject>> items = serviceDataObject.find(query,null);
                mav.addObject("itemsValue", items);
            }
        }

        mav.addObject("isKeyBasic",isBasicKey);
        mav.addObject("isValueBasic",isBasicValue);
               
        mav.addObject("typeKey", typeKey);
        mav.addObject("queryKey", queryKey);
        mav.addObject("typeValue", typeValue);
        mav.addObject("queryValue", queryValue);
        return mav;
    }

}

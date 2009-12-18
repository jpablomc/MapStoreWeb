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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        String query = "(_TYPE = " + DataType.class.getName() + " AND _NAME = " + parameter + ") -> {[DATATYPE],[<-],1} AND _TYPE = " + DataObject.class.getName();
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

    public void getFormForNew(HttpServletRequest req,HttpServletResponse res) throws IOException {
        String datatype = req.getParameter("datatype");
        DataType dt = serviceDataType.getDataType(datatype);
        res.getWriter().write(DataObjectControllerHelper.createFormForNewObject(dt));
    }



}

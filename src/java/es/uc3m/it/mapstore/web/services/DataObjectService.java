/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.bean.MapStoreExtendedItem;
import es.uc3m.it.mapstore.bean.MapStoreItem;
import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import es.uc3m.it.mapstore.web.beans.DataObject;
import es.uc3m.it.mapstore.web.services.exception.NonUniqueResultException;
import es.uc3m.it.util.ReflectionUtils;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private MapStoreExtendedItem<DataObject> getByDatatypeNameDate(String datatype, String name,Date d) {
        String query = "_TYPE = "+ datatype + " AND _NAME = " + name;
        MapStoreSession s = MapStoreSession.getSession();
        List<MapStoreExtendedItem<DataObject>> results = s.queryExtended(DataObject.class, query, d);
        if (results.size() != 1) throw new NonUniqueResultException("Query: ("+ query + ") returned " + results.size()+" results");
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

    public MapStoreExtendedItem<DataObject> getById(int id) {
        MapStoreSession s = MapStoreSession.getSession();
        return s.recoverExtendedById(id, DataObject.class);
    }


    public void createDataObject(DataObject object) {
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        s.save(object);
        s.commit();
        s.close();
    }

    public void updateDataObject(DataObject object) {
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        s.update(object);
        s.commit();
        s.close();
    }

    public Map<Object,Object> showObject(int id,int version) {
        Map<Object,Object> data = new HashMap<Object,Object>();
        MapStoreSession s = MapStoreSession.getSession();
        MapStoreItem raw = s.recoverRawByIdVersion(id, version);
        DataObject object = s.recoverByIdVersion(id, version, DataObject.class);
        MapStoreItem lastVersion = s.recoverRawById(id);
        object.getDataType().getAttributes(); //Inicializacion
        data.put("root",object);
        data.put("lastVersion",lastVersion);
        data.put(object, raw);

        for (String attrib : object.keySet()) {
            Object value = object.get(attrib);
            if (value instanceof DataObject) { //Es una relacion simple recuperamos el objeto y lo emparejamos al valor
                data.put(object,raw.getProperty(MapStoreItem.NONPROCESSABLE + attrib));
            } else if (value instanceof Collection) {
                Class c = ReflectionUtils.determineGenericType((Collection)value);
                if (DataObject.class.isAssignableFrom(c)) { //Es una lista de referencias
                    //Tenemos que recuperar la lista
                    String[] tmp = ((String) raw.getProperty(MapStoreItem.NONPROCESSABLE + attrib)).split("_");
                    raw = s.recoverRawByIdVersion(Integer.valueOf(tmp[0]), Integer.valueOf(tmp[1]));
                    //Procesar los elementos

                } //En otro caso es una lista devalores
            } else if (value instanceof Map) {

            } else if (value.getClass().isArray()) {

            }
        }
        return data;
    }

}

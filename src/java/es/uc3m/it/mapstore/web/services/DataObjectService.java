/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.bean.MapStoreExtendedItem;
import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import es.uc3m.it.mapstore.web.beans.DataObject;
import es.uc3m.it.mapstore.web.services.exception.NonUniqueResultException;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pablo
 */
@Service
public class DataObjectService {
    private List<DataObject> getAllByDatatype(String datatype) {
        String query = "_TYPE = "+ DataObjectService.class.getName() +" _DATATYPE = " + datatype;
        MapStoreSession s = MapStoreSession.getSession();        
        List<DataObject> results = s.query(DataObject.class, query, null);
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
        String fullName = datatype + "_" + name;
        MapStoreSession s = MapStoreSession.getSession();
        DataObject result = s.findByNameType(fullName, DataObject.class);
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
        String fullName = datatype + "_" + name;
        String query = "_TYPE = "+ DataObjectService.class.getName() +" _DATATYPE = " + datatype + "_NAME = " + fullName;
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

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.bean.MapStoreItem;
import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.exception.MapStoreRunTimeException;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author Pablo
 */
@Service
public class DataTypeService {

    public DataTypeService() {
        try {
        initBasicDatatypes();
        }catch(RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void initBasicDatatypes() {
        List<DataType> toCreate = new ArrayList<DataType>();
        for (String aux : DataTypeConstant.BASICTYPES) {
            if (getDataType(aux) == null) {
                toCreate.add(new DataType(aux));
            }
        }
        if (!toCreate.isEmpty()) createDataType(toCreate);
    }

    private void createDataType(List<DataType> list) {
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        for (DataType dt: list) {
            s.save(dt);
        }
        try {
            s.commit();
        } catch (MapStoreRunTimeException ex) {
            s.rollback();
            throw ex;
        } finally{
            s.close();
        }
    }


    public void createDataType(DataType dt){
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        try {
            s.save(dt);
            s.commit();
        } catch (MapStoreRunTimeException ex) {
            Logger.getLogger(DataTypeService.class.getName()).log(Level.SEVERE, ex.getMessage(),ex);
            s.rollback();
            throw ex;
        } finally {
            s.close();
        }
    }

    public void updateDataType(DataType dt){
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        s.update(dt);
        try {
            s.commit();
        } catch (MapStoreRunTimeException ex) {
            s.rollback();
            throw ex;
        } finally {
            s.close();
        }
    }

    public void deleteDataType(DataType dt){
        MapStoreSession s = MapStoreSession.getSession();
        if (canBeDeleted(dt)) {
            s.beginTransaction();
            try {
                s.delete(dt);
                s.commit();
            } catch (MapStoreRunTimeException ex) {
                s.rollback();
                throw ex;
            } finally {
                s.close();
            }
        }
    }

    public List<DataType> getAll() {
        MapStoreSession s = MapStoreSession.getSession();
        return s.findByType(DataType.class,DataType.TYPE_NAME);
    }

    public DataType getDataType(String nombre) {
        MapStoreSession s = MapStoreSession.getSession();
        return s.findByNameType(nombre, DataType.TYPE_NAME, DataType.class);
    }

    private boolean canBeDeleted(DataType dt) {
        boolean result = true;
        if (DataTypeConstant.BASICTYPES.contains(dt.getName())) result = false;
        else {
            MapStoreSession s = MapStoreSession.getSession();
            List<MapStoreItem> items = s.findByType(dt.getName());
            if (!items.isEmpty()) result = false;
        }
        return result;
    }


}

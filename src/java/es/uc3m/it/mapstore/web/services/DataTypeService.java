/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import es.uc3m.it.mapstore.web.beans.DataType;
import es.uc3m.it.mapstore.exception.MapStoreRunTimeException;
import es.uc3m.it.mapstore.web.beans.DataTypeConstant;
import java.util.ArrayList;
import java.util.List;
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
        }
    }


    public void createDataType(DataType dt){
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        try {
            s.save(dt);
            s.commit();
        } catch (MapStoreRunTimeException ex) {
            s.rollback();
            throw ex;
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
        }
    }

    public void deleteDataType(DataType dt){
        MapStoreSession s = MapStoreSession.getSession();
        s.beginTransaction();
        s.delete(dt);
        try {
            s.commit();
        } catch (MapStoreRunTimeException ex) {
            s.rollback();
            throw ex;
        }
    }

    public List<DataType> getAll() {
        MapStoreSession s = MapStoreSession.getSession();
        return s.findByType(DataType.class);
    }

    public DataType getDataType(String nombre) {
        MapStoreSession s = MapStoreSession.getSession();
        return s.findByNameType(nombre, DataType.class);
    }


}

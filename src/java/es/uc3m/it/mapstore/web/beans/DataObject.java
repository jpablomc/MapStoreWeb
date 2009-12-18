/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * This object will represent a generic object on database.
 * It delegates all the functions on an inner map.
 * Defines special properties like Datatype and Name
 *
 *
 * @author Pablo
 */
public class DataObject implements Map<String,Object>{

    private static String DATATYPE = "_DATATYPE";
    
    private Map<String,Object> delegate;

    public DataObject(DataType dt) {
        delegate = new TreeMap<String,Object>();
        delegate.put(DATATYPE, dt);
    }

    public DataObject() {
        delegate = new TreeMap<String,Object>();
    }

    public Collection<Object> values() {
        return delegate.values();
    }

    public int size() {
        return delegate.size();
    }

    public Object remove(Object key) {
        return delegate.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        delegate.putAll(m);
    }

    public Object put(String key, Object value) {
        return delegate.put(key, value);
    }

    public Set<String> keySet() {
        return delegate.keySet();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public Object get(Object key) {
        return delegate.get(key);
    }

    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    public Set<Entry<String, Object>> entrySet() {
        return delegate.entrySet();
    }

    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    public void clear() {
        delegate.clear();
    }

    public String getName() {
        DataType dt = getDataType();
        String pk = dt.getPk();
        return (String)delegate.get(pk);
    }

    public DataType getDataType() {
        return (DataType) delegate.get(DATATYPE);
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.beans;
import es.uc3m.it.mapstore.bean.annotations.Name;
import es.uc3m.it.mapstore.bean.annotations.Type;
import java.util.Map;
import java.util.TreeMap;
/**
 *
 * Esta clase representa la estructura del objeto
 *
 * @author Pablo
 */
public class DataType {

    public final static String TYPE_NAME = "DataType";

    @Name(order=0)
    private String name;
    @Type
    private String type;
    private Map<String,DataType> attributes;
    private Map<String,DataType> extraData;
    private String pk;

    public DataType() {
        attributes = new TreeMap<String, DataType>();
        extraData = new TreeMap<String, DataType>();
        type = TYPE_NAME;
    }

    public DataType(String name) {
        this.name = name;
        type = TYPE_NAME;
        attributes = new TreeMap<String, DataType>();
        extraData = new TreeMap<String, DataType>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Map<String, DataType> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, DataType> attributes) {
        this.attributes = attributes;
    }

    public Map<String, DataType> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, DataType> extraData) {
        this.extraData = extraData;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    private static String MAP_KEY_PREFIX = "MAPKEY_";
    private static String MAP_VALUE_PREFIX = "MAPVALUE_";
    private static String LIST_PREFIX = "LIST_";

    public void setMapKeyDataType(String property, DataType dt) {
        extraData.put(MAP_KEY_PREFIX+ property, dt);
    }

    public void setMapValueDataType(String property, DataType dt) {
        extraData.put(MAP_VALUE_PREFIX+ property, dt);
    }

    public void setListDataType(String property, DataType dt) {
        extraData.put(LIST_PREFIX+ property, dt);
    }

    public DataType getMapKeyDataType(String property) {
        return extraData.get(MAP_KEY_PREFIX+property);
    }

    public DataType getMapValueDataType(String property) {
        return extraData.get(MAP_VALUE_PREFIX+property);
    }

    public DataType getListDataType(String property) {
        return extraData.get(LIST_PREFIX+property);
    }

    @Override
    public boolean equals(Object obj) {
        boolean toReturn = false;
        if (obj instanceof DataType) {
            DataType other = (DataType)obj;
            toReturn = this.getName().equals(other.getName());
        }
        return toReturn;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }


}

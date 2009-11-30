/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.beans;
import es.uc3m.it.mapstore.bean.annotations.Name;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.relaxng.datatype.Datatype;
/**
 *
 * Esta clase representa la estructura del objeto
 *
 * @author Pablo
 */
public class DataType {    
    @Name(order=0)
    private String name;
    private Map<String,DataType> attributes;

    public DataType() {
        attributes = new HashMap<String, DataType>();
    }

    public DataType(String name) {
        this.name = name;
        attributes = new HashMap<String, DataType>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, DataType> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, DataType> attributes) {
        this.attributes = attributes;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.beans;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Pablo
 */
public class DataTypeConstant {
    public final static String STRINGTYPE = "String";
    public final static String INTEGERTYPE = "Integer";
    public final static String LONGTYPE = "Long";
    public final static String FLOATTYPE = "Float";
    public final static String DOUBLETYPE = "Double";
    public final static String DATETYPE = "Date";
    public final static String LISTTYPE = "List";
    public final static String ARRAYTYPE = "Array";
    public final static String MAPTYPE = "Map";

    private final static String[] basicTypes = {STRINGTYPE,INTEGERTYPE,LONGTYPE,FLOATTYPE,DOUBLETYPE,DATETYPE,LISTTYPE,ARRAYTYPE,MAPTYPE};

    public static List<String> BASICTYPES;

    static {
        BASICTYPES = Arrays.asList(basicTypes);
    }
}

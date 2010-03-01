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
    public final static String FILETYPE = "File";

    private final static String[] basicTypes = {STRINGTYPE,INTEGERTYPE,LONGTYPE,FLOATTYPE,DOUBLETYPE,DATETYPE,LISTTYPE,ARRAYTYPE,MAPTYPE,FILETYPE};
    private final static String[] listTypes = {LISTTYPE,ARRAYTYPE};
    private final static String[] mapTypes = {MAPTYPE};

    public final static List<String> BASICTYPES = Arrays.asList(basicTypes);
    public final static List<String> LISTTYPES = Arrays.asList(listTypes);
    public final static List<String> MAPTYPES = Arrays.asList(mapTypes);

    public final static String LIST_PREFIX = "LIST_";
    public final static String MAP_KEY__PREFIX = "MAPKEY_";
    public final static String MAP_VALUE_PREFIX = "MAPVALUE_";
}

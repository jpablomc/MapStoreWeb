/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author Pablo
 */
public class MapStoreContextAware implements ApplicationContextAware {
    public static ApplicationContext ctx;

    public static <T> T getBean(Class<T> clazz) {
        Map beans = ctx.getBeansOfType(clazz);
        return (T)beans.values().iterator().next();
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        ctx = arg0;
    }


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Pablo
 */
public class AjaxController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String clazz = req.getParameter("clazz");
            String methodName = req.getParameter("method");
            Class c = Class.forName(clazz);
            Method m = c.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            Object t = MapStoreContextAware.getBean(c);
            m.invoke(t, req, resp);
        } catch (IllegalAccessException ex) {
            throw new ServletException(ex);
        } catch (IllegalArgumentException ex) {
            throw new ServletException(ex);
        } catch (InvocationTargetException ex) {
            throw new ServletException(ex);
        } catch (NoSuchMethodException ex) {
            throw new ServletException(ex);
        } catch (SecurityException ex) {
            throw new ServletException(ex);
        } catch (ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }
    
}

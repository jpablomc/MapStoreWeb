/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.test;

import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Pablo
 */
@Controller
public class GetAll {
    @RequestMapping(value= "/test/list.html")
    public void getAll() {
        MapStoreSession s = MapStoreSession.getSession();
        s.getAll();
    }
}

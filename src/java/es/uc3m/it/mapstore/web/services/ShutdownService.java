/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services;

import es.uc3m.it.mapstore.db.impl.MapStoreSession;
import org.springframework.stereotype.Service;



/**
 *
 * @author Pablo
 */
@Service
public class ShutdownService {
    public void shutdown() {
        MapStoreSession s = MapStoreSession.getSession();
        s.shutdown();
    }
}

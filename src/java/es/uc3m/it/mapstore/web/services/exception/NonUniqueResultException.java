/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uc3m.it.mapstore.web.services.exception;

import es.uc3m.it.mapstore.exception.MapStoreRunTimeException;

/**
 *
 * @author Pablo
 */
public class NonUniqueResultException extends MapStoreRunTimeException{
    public NonUniqueResultException(String msg) {
        super(msg);
    }

    public NonUniqueResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonUniqueResultException(Throwable cause) {
        super(cause);
    }
}

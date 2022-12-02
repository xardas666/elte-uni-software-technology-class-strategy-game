package hu.elte.csapat4.logics;

import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class GameException extends Exception {

    public GameException(String message) {
        super(message);
        log.log(Level.SEVERE, message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
        log.log(Level.SEVERE, message, cause);
    }
}

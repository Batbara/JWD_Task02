package by.tc.task02.dao;

public class DAOException extends Exception {
    public DAOException (String message, Throwable e){
        super(message,e);
    }
}

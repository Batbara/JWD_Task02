package by.tc.task02.service;

public class ServiceException extends Exception {
    public ServiceException(String message, Throwable e){
        super(message,e);
    }
}

package by.tc.task02.main;

import by.tc.task02.entity.Entity;
import by.tc.task02.service.EntityService;
import by.tc.task02.service.ServiceException;
import by.tc.task02.service.ServiceFactory;

public class Main {
    public static void main (String [] args){
        ServiceFactory factory = ServiceFactory.getInstance();
        EntityService service = factory.getEntityService();
        Entity entity;
        try {
            entity =service.parseSource("test.xml");
            System.out.println(entity);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }
    }
}

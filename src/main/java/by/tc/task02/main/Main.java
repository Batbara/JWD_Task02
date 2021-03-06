package by.tc.task02.main;

import by.tc.task02.entity.Entity;
import by.tc.task02.service.EntityService;
import by.tc.task02.service.ServiceException;
import by.tc.task02.service.ServiceFactory;

public class Main {
    public static void main(String[] args) {
        ServiceFactory factory = ServiceFactory.getInstance();
        EntityService service = factory.getEntityService();

        Entity entity;
        try {
            entity = service.parseSource("task02.xml");
            if (entity != null) {
                EntityPrinter.printEntity(entity);
            }
            else {
                System.out.println("Nothing to show!");
            }
        } catch (ServiceException e) {
            System.err.println(e.getMessage());

            int errorExitStatus = 1;
            System.exit(errorExitStatus);
        }
    }

}

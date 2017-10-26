package by.tc.task02.service.impl;

import by.tc.task02.dao.DAOException;
import by.tc.task02.dao.DAOFactory;
import by.tc.task02.dao.EntityDAO;
import by.tc.task02.entity.Entity;
import by.tc.task02.service.EntityService;
import by.tc.task02.service.ServiceException;
import by.tc.task02.service.validation.SourceValidator;

public class EntityServiceImpl implements EntityService{
    public Entity parseSource(String sourceName) throws ServiceException {

        if(!SourceValidator.isSourceNameValid(sourceName)){
            return null;
        }
        DAOFactory factory = DAOFactory.getInstance();
        EntityDAO entityDAO = factory.getEntityDAO();

        Entity entity = null;
        try {
            entity = entityDAO.parseSource(sourceName);
        } catch (DAOException e) {
            throw new ServiceException("DAOException!",e);
        }
        return entity;
    }
}

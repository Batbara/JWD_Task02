package by.tc.task02.service;

import by.tc.task02.entity.Entity;

public interface EntityService {

    Entity parseSource(String sourceName) throws ServiceException;
}

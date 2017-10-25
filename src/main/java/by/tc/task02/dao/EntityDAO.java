package by.tc.task02.dao;

import by.tc.task02.entity.Entity;

public interface EntityDAO {

    Entity parseSource(String sourceName) throws DAOException;
}

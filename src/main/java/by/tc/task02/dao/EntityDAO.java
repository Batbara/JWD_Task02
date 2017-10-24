package by.tc.task02.dao;

import by.tc.task02.entity.Entity;
import by.tc.task02.entity.Source;

public interface EntityDAO {

    Entity parseSource(String sourceName) throws DAOException;
}

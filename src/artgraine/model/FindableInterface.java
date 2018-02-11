package artgraine.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface FindableInterface<T, DAO> {
    public DAO findAll() throws SQLException;

    public DAO findOneById(int id) throws SQLException;

    public T getOneResult() throws SQLException;

    public Map getOneResultAsArray() throws SQLException;

    public List<T> getResults() throws SQLException;

    public List<Map<String, String>> getResultsAsArray() throws SQLException;
}

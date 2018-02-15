package artgraine.model;

import java.sql.SQLException;

public interface PersistableInterface<T> {

    public int save(T entity)throws SQLException;

    public int deleteOneById(Long id) throws SQLException;
}

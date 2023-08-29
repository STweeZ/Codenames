package fr.univartois.ili.jai.persistance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTDG<T extends Persistable> implements GenericTDG<T> {

    private static final String NOT_PERSISTED_IN_DB = "Object not persisted in database!";
    private static final String ALREADY_PERSISTED_IN_DB = "Object already persisted in database!";

    @Override
    public final T findById(long id) throws SQLException {
        if (id == 0) {
            throw new IllegalArgumentException("No object with ID 0!");
        }
        return findByIdIntoDB(id);
    }

    protected abstract T findByIdIntoDB(long id) throws SQLException;

    @Override
    public final T insert(T t) throws SQLException {
        if (t.getId() != 0) {
            throw new IllegalArgumentException(ALREADY_PERSISTED_IN_DB);
        }
        var fromDB = insertIntoDB(t);
        if (t.getId() == 0) {
            throw new IllegalStateException("No generated ID");
        }
        return fromDB;
    }

    protected abstract T insertIntoDB(T t) throws SQLException;

    @Override
    public T update(T t) throws SQLException {
        if (t.getId() == 0) {
            throw new IllegalArgumentException(NOT_PERSISTED_IN_DB);
        }
        return updateIntoDB(t);
    }

    protected abstract T updateIntoDB(T t) throws SQLException;

    @Override
    public T refresh(T t) throws SQLException {
        if (t.getId() == 0) {
            throw new IllegalArgumentException(NOT_PERSISTED_IN_DB);
        }
        return refreshIntoDB(t);
    }

    protected abstract T refreshIntoDB(T t) throws SQLException;

    @Override
    public T delete(T t) throws SQLException {
        if (t.getId() == 0) {
            throw new IllegalArgumentException(NOT_PERSISTED_IN_DB);
        }
        return deleteFromDB(t);
    }

    protected abstract T deleteFromDB(T t) throws SQLException;

    @Override
    public List<T> selectWhere(String clauseWhereWithJoker, Object... args) throws SQLException {
        List<T> result = new ArrayList<>();
        try (PreparedStatement pst = TDGRegistry.getConnection()
                .prepareStatement(getWherePrefix() + clauseWhereWithJoker)) {
            var index = 1;
            for (Object arg : args) {
                pst.setObject(index++, arg);
            }
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(findById(rs.getLong(1)));
                }
            }
        }
        return result;
    }

    protected abstract String getWherePrefix();
}

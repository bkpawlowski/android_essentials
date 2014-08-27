package pl.pawlowski.bartek.supplib.persistence.DAO;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

import pl.pawlowski.bartek.supplib.persistence.DTO.AbstractEntity;

/**
 * Created by Bartek Garet Pawlowski on 2014-08-27.
 */
public class AbstractDAO<EntityClass extends AbstractEntity, PrimaryKeyClass> extends BaseDaoImpl<EntityClass, PrimaryKeyClass> {
    protected AbstractDAO(Class<EntityClass> dataClass) throws SQLException {
        super(dataClass);
    }

    protected AbstractDAO(ConnectionSource connectionSource, Class<EntityClass> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    protected AbstractDAO(ConnectionSource connectionSource, DatabaseTableConfig<EntityClass> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
    }
}

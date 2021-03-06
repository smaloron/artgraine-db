package artgraine.database;

import org.flywaydb.core.Flyway;


import javax.sql.DataSource;
import java.sql.Connection;

public class DataBaseMigrator {

    private Connection dbConnection;

    public void migrate(){
        Flyway flyway = new Flyway();
        //flyway.setDataSource((DataSource) dbConnection);
        flyway.setDataSource("jdbc:derby:artgraine.db;create=true","","");

        flyway.setLocations("filesystem:db/migration");
        /*
        flyway.setBaselineOnMigrate(true);
        flyway.setCleanOnValidationError(true);
        flyway.setValidateOnMigrate(true);
*/
        flyway.migrate();
    }
}

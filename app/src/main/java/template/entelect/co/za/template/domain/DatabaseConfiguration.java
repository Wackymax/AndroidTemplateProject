package template.entelect.co.za.template.domain;

import java.util.ArrayList;
import java.util.List;

import template.entelect.co.za.template.common.ApplicationSettings;
import za.co.cporm.model.CPOrmConfiguration;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class DatabaseConfiguration  implements CPOrmConfiguration {
    @Override
    public String getDatabaseName() {
        return ApplicationSettings.ENVIRONMENT == ApplicationSettings.Environment.PROD ? "database.db" :
                "database-" + ApplicationSettings.ENVIRONMENT + ".db" ;
    }

    @Override
    public int getDatabaseVersion() {
        return 1;
    }

    @Override
    public boolean recreateDatabaseOnFailedUpgrade() {
        return ApplicationSettings.ENVIRONMENT != ApplicationSettings.Environment.QA ;
    }

    @Override
    public boolean isQueryLoggingEnabled() {
        return ApplicationSettings.ENVIRONMENT == ApplicationSettings.Environment.QA;
    }

    @Override
    public String upgradeResourceDirectory() {
        return "sql-scripts";
    }

    @Override
    public List<Class<?>> getDataModelObjects() {
        List<Class<?>> domainObjects = new ArrayList<>();

        return domainObjects;
    }
}

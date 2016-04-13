package models.db;

import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.ServerConfig.DbUuid;
import com.avaje.ebean.event.ServerConfigStartup;

/**
 * @author Michael Vaughan
 */
public class AppServerConfigStartup implements ServerConfigStartup {

    @Override
    public void onStart(ServerConfig serverConfig) {
        serverConfig.setDbUuid(DbUuid.BINARY);
    }
}

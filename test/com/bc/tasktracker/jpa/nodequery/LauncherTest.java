package com.bc.tasktracker.jpa.nodequery;

import com.bc.jpa.EntityManagerFactoryCreatorImpl;
import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceContextEclipselinkOptimized;
import com.bc.jpa.functions.GetClassLoaderForPersistenceUri;
import com.bc.sql.MySQLDateTimePatterns;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Josh
 */
public class LauncherTest {

    private static final Logger logger = Logger.getLogger(LauncherTest.class.getName());
    
    private LauncherTest() { }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            try(final InputStream in = classLoader
                            .getResourceAsStream("META-INF/rust_logging.properties")) {
            
                LogManager.getLogManager().readConfiguration(in);
            }
            
            Logger.getLogger(RenameUppercaseSlaveColumnsThenTables.class.getPackage().getName())
                    .addHandler(WindowHandler.getInstance());

            final URL persistenceUrl = classLoader.getResource("META-INF/persistence.xml");
            
            logger.fine(() -> "Persistence URL: " + persistenceUrl);
            
            final Function<String, Properties> getProps = (persistenceUnit) -> {
                logger.finer(() -> "\n\tFetching properties for persistence unit: " + persistenceUnit);                
                    final Properties properties = new Properties();
                    if("bctasktrackerPUmaster".equals(persistenceUnit)) {
                        properties.setProperty("javax.persistence.jdbc.url", "jdbc:mysql://europa.ignitionserver.net:3306/loosebox_naftasktrackerweb");
                        properties.setProperty("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
                        properties.setProperty("javax.persistence.jdbc.user", "loosebox_nafuser");
                        properties.setProperty("javax.persistence.jdbc.password", "7ApacheLaunchers");
                    }
                    return properties;
            };
            final PersistenceContext jpaContext = new PersistenceContextEclipselinkOptimized(
                    persistenceUrl.toURI(), 
                    new EntityManagerFactoryCreatorImpl(
                            new GetClassLoaderForPersistenceUri().apply(persistenceUrl.toURI().toString()),
                            getProps
                    ), 
                    new MySQLDateTimePatterns()
            );
            
            logger.fine(() -> "Initialized: " + PersistenceContext.class.getSimpleName());
            
            final Class masterEntityType = com.bc.tasktracker.jpa.entities.master.Appointment.class;
            final Class slaveEntityType = com.bc.tasktracker.jpa.entities.slave.Appointment.class;

//            new RenameUppercaseSlaveColumnsThenTables(jpaContext).apply(masterEntityType, slaveEntityType);
            
//            new CreateTablesDerby(jpaContext).apply(slaveEntityType);

        }catch(Exception e) {
            logger.log(Level.WARNING, "", e);
        }
    }
}

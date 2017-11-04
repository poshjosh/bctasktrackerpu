package com.bc.tasktracker.jpa;

import com.bc.functions.FindExceptionInChain;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.sql.script.SqlLineParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 20, 2017 4:36:19 PM
 */
public class CreateTablesDerby {

    private static final Logger logger = Logger.getLogger(CreateTablesDerby.class.getName());
    
    private final JpaContext jpaContext;

    public CreateTablesDerby(JpaContext jpaContext) {
        this.jpaContext = Objects.requireNonNull(jpaContext);
    }
    
    public List<Integer> apply(Class slaveEntityType) {
        
        try{
            
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            
            final String sqlFname = "META-INF/bctasktracker_drop_then_create_tables_derby.sql";
            
            logger.fine(() -> "Executing queries from SQL file: " + sqlFname);
            
            final InputStream in = classLoader.getResourceAsStream(sqlFname);
            
            final List<String> sqlLines;
            
            try(Reader reader = new BufferedReader(new InputStreamReader(in, "utf-8"))) {

                sqlLines = new SqlLineParser().parse(reader);
            }    
            
            final List<Integer> updateCounts = new ArrayList(sqlLines.size());
            
            for(String sqlLine : sqlLines) {
                final Integer updateCount = jpaContext.executeTransaction(
                        jpaContext.getEntityManager(slaveEntityType), (emgr) -> { 
                                return executeUpdate(emgr, sqlLine);
                        }
                );
                logger.fine(() -> "Update count: " + updateCount + ", query: " + formatAsMsg(sqlLine));
            }
            
            return updateCounts;
            
        }catch(Exception e) {
            
            logger.log(Level.WARNING, "", e);
            
            return Collections.EMPTY_LIST;
        }
    }
    
    private int executeUpdate(EntityManager em, String sqlLine) {
        int updateCount = -1;
        try{
            updateCount = em.createNativeQuery(sqlLine).executeUpdate();
        }catch(Exception e) {

            final Optional<Throwable> optThrowable = new FindExceptionInChain()
                    .apply(e, (t) -> t instanceof java.sql.SQLException);
            if(optThrowable.isPresent()) {
                final java.sql.SQLException sqlex = (java.sql.SQLException)optThrowable.get();
                final int errCode = sqlex.getErrorCode();
                final String errMsg;
                switch(errCode) {
                    case 20_000: errMsg = "Table already exists"; break;
                    case 30_000: errMsg = "A lock could not be obtained within the time requested"; break;
                    default: errMsg = "Encountered: " + sqlex.getClass().getName() + 
                            " while attempting to execute query:\n" + formatAsMsg(sqlLine); break;
                }
                logger.warning(errMsg);
            }else{
                logger.warning(() -> "Encountered: " + e.getClass().getName() + 
                        " while attempting to execute query:\n" + formatAsMsg(sqlLine));
                logger.finer(() -> e.toString());
            }
        }
        return updateCount;
    }
    
    private String formatAsMsg(String sqlLine) {
        final String msg;
        final int nl = sqlLine.indexOf('\n');
        if(nl != -1) {
            msg = sqlLine.substring(0, nl);
        }else{
            msg = sqlLine.length() < 100 ? sqlLine : sqlLine.substring(0, nl);
        }
        return msg;
    }
}

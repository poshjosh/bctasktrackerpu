/*
 * Copyright 2017 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.tasktracker.jpa;

import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceContextEclipselinkOptimized;
import com.bc.jpa.paging.PaginatedList;
import com.bc.jpa.search.QuerySearchResults;
import com.bc.jpa.search.SearchResults;
import com.bc.jpa.sync.MasterSlavePersistenceContext;
import com.bc.jpa.sync.impl.MasterSlavePersistenceContextImpl;
import com.bc.node.NodeFormat;
import com.bc.sql.MySQLDateTimePatterns;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2017 10:45:27 AM
 */
public class SyncMasterToSlave {
    
    public static void main(String... args) {
        
        final String masterPersistenceUnit = "bctasktrackerPUmaster";
        final String slavePersistenceUnit = "bctasktrackerPUslave";
        
        final Class entityType = com.bc.tasktracker.jpa.entities.master.Unit.class;
        
        final int pageSize = 20;
        
        final Path workingDir = Paths.get(System.getProperty("user.home"), "naftasktracker");
        final Path derbyDbPath = Paths.get(workingDir.toString(), "derby_db");
        System.setProperty("derby.system.home", derbyDbPath.toString());            

        try{
            
//            final URI persistenceConfigUri = Thread.currentThread().getContextClassLoader()
//                    .getResource("META-INF/persistence.xml").toURI();
//file:/C:/Users/Josh/Documents/NetBeansProjects/bctasktrackerpu/build/classes/META-INF/persistence.xml
            final URI persistenceConfigUri = Paths.get(System.getProperty("user.home"), "Documents", 
                    "NetBeansProjects", "bctasktrackerpu", "test", "META-INF", "persistence.xml").toUri();

            final PersistenceContext jpa = new PersistenceContextEclipselinkOptimized(
                    persistenceConfigUri, new MySQLDateTimePatterns());
            
            if(!jpa.isMetaDataLoaded()) {
                jpa.loadMetaData();
            }
            
System.out.println(new NodeFormat().format(jpa.getMetaData().getNode()));

            final EntityManager masterEm = jpa.getEntityManager(masterPersistenceUnit);
            
            try{
                
                final TypedQuery typedQuery = masterEm.createQuery(masterEm.getCriteriaBuilder().createQuery(entityType));
//                        .setHint(QueryHints.READ_ONLY, HintValues.TRUE)
//                        .setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheThenDatabase);

                final SearchResults masterSearchResults = new QuerySearchResults(
                        typedQuery, pageSize, false
                );

System.out.println("Found " + masterSearchResults.getSize() + " results for entity type: " + entityType.getName());

                final PaginatedList masterPages = masterSearchResults.getPages();

                final MasterSlavePersistenceContext masterSlave = new MasterSlavePersistenceContextImpl(
                        jpa.getContext(masterPersistenceUnit), jpa.getContext(slavePersistenceUnit)
                );
                
                for(Object master : masterPages) {

System.out.println(LocalDateTime.now()+"- - - - - - - Master: " + master);  
                    final Object slave = masterSlave.apply(master);
System.out.println(LocalDateTime.now()+"- - - - - - -  Slave: " + slave);  
                    jpa.getDao(slavePersistenceUnit).begin().persistAndClose(slave);
System.out.println(LocalDateTime.now()+"- - - - - - -Persisted");  
                }
                
            }finally{
                if(masterEm.isOpen()) {
                    masterEm.close();
                }
            }
        }catch(Exception e) {
            
            e.printStackTrace();
        }
    }
}

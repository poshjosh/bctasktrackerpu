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

import com.bc.jpa.context.JpaContext;
import com.bc.jpa.context.JpaContextImpl;
import com.bc.jpa.metadata.JpaMetaData;
import com.bc.node.Node;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import org.junit.Test;

/**
 *
 * @author Josh
 */
public class JpaMetaDataTest {

    private final JpaContext jpa;
    
    public JpaMetaDataTest() throws MalformedURLException { 
        final String parent = "file:/C:/Users/Josh/Documents/NetBeansProjects/bctasktrackerpu/test";
        final URI uri = URI.create(parent + "/META-INF/persistence.xml");
        this.jpa = new JpaContextImpl(uri, null);
    }

    @Test
    public void test() throws SQLException {
        
        final JpaMetaData metaData = jpa.getMetaData();
        
        final Set<String> puNames = metaData.getPersistenceUnitNames();

        final String masterPuName = 
                puNames.stream().filter((puName) -> puName.contains("master"))
                        .findFirst().get();
System.out.println("\nMaster persistence unit name: " + masterPuName);

        final Consumer<String> printDbNames = new PrintDatabaseNames(jpa);
        
        printDbNames.accept(masterPuName);

        final String slavePuName = 
                puNames.stream().filter((puName) -> puName.contains("slave"))
                        .findFirst().get();
System.out.println("\n Slave persistence unit name: " + slavePuName);
        
        printDbNames.accept(slavePuName);
        
        final Node<String> persistenceNode = metaData.getNode();
        
long tb4 = System.currentTimeMillis();
long mb4 = com.bc.util.Util.availableMemory();

        final Node<String> unitidColumn = persistenceNode.
                findFirstChild(slavePuName, null, "APP", "UNIT", "UNITID").orElse(null);
        
System.out.println("\nFound column: " + unitidColumn + 
        ", consumed time:" + (System.currentTimeMillis() - tb4) + 
        ", memory: " + com.bc.util.Util.usedMemory(mb4));        

tb4 = System.currentTimeMillis();
mb4 = com.bc.util.Util.availableMemory();

        Node<String> taskTable = persistenceNode.
                findFirstChild(slavePuName, null, "APP", "TASK").orElse(null);
        
System.out.println("\nFound table: " + taskTable + 
        ", consumed time:" + (System.currentTimeMillis() - tb4) + 
        ", memory: " + com.bc.util.Util.usedMemory(mb4));        

tb4 = System.currentTimeMillis();
mb4 = com.bc.util.Util.availableMemory();

        taskTable = persistenceNode.
                findFirstChild(slavePuName, null, "APP", "task").orElse(null);
        
System.out.println("\nFound table: " + taskTable + 
        ", consumed time:" + (System.currentTimeMillis() - tb4) + 
        ", memory: " + com.bc.util.Util.usedMemory(mb4));        

        for(String puName : puNames) {
            
            final Set<Class> puClasses = metaData.getEntityClasses(puName);

            for(Class puClass : puClasses) {

tb4 = System.currentTimeMillis();
mb4 = com.bc.util.Util.availableMemory();
    
                final String tableName = metaData.getTableName(puClass);

                final String [] columnNames = metaData.getColumnNames(puClass); 
                
System.out.println("\nFor entity type: " + puClass.getName() + 
        ", Table name: " + tableName +
        "\nColumns: " + Arrays.toString(columnNames) +
        "\nconsumed time:" + (System.currentTimeMillis() - tb4) + 
        ", memory: " + com.bc.util.Util.usedMemory(mb4));        
            }
        }
    }
}

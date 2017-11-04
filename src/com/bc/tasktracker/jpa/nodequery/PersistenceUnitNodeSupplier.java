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

package com.bc.tasktracker.jpa.nodequery;

import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.metadata.PersistenceNodeBuilder;
import com.bc.jpa.metadata.PersistenceUnitMetaData;
import com.bc.node.Node;
import com.bc.node.NodeValueTest;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 16, 2017 4:32:31 PM
 */
public class PersistenceUnitNodeSupplier implements Function<Class, Node<String>> {

    private final PersistenceContext jpaContext;

    public PersistenceUnitNodeSupplier(PersistenceContext jpaContext) {
        this.jpaContext = Objects.requireNonNull(jpaContext);
    }
    
    @Override
    public Node<String> apply(Class entityType) {
        
        final Set<String> puNames = jpaContext.getMetaData().getPersistenceUnitNames();
        
        for(String puName : puNames) {

            final PersistenceUnitMetaData puMeta = jpaContext.getContext(puName).getMetaData(); 
            
            final String tableName = puMeta.getTableName(entityType);
            
            if(tableName == null) {
                continue;
            }
            
            final Node<String> puNode = puMeta.getNode();
            
            final Predicate<Node<String>> tableNodeTest = 
                    new NodeValueTest(PersistenceNodeBuilder.NODE_LEVEL_TABLE, tableName, false);
            puNode.findFirstChild(tableNodeTest).orElseThrow(
                    () ->  new IllegalArgumentException("Could not ascertain persistence unit for entity type: " + entityType)
            );
            
            return puNode;
        }
        
        throw new IllegalArgumentException("Unexpected entity type: " + entityType.getName());
    }
}

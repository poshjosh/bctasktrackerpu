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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.junit.Test;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 12, 2017 2:18:33 PM
 */
public class JpaContextTest {

    @Test
    public void test() throws URISyntaxException {
        
//        final URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/persistence.xml");
        
        final URI uri = URI.create("file:/C:/Users/Josh/Documents/NetBeansProjects/bctasktrackerpu/test/META-INF/persistence.xml");
        
        final JpaContext jpa = new JpaContextImpl(uri, new Class[0]);
        
        final JpaMetaData metaData = jpa.getMetaData();
        
        this.test(metaData, com.bc.tasktracker.jpa.entities.master.Task.class);
                
        this.test(metaData, com.bc.tasktracker.jpa.entities.slave.Task.class);
    }
    
    private void test(JpaMetaData metaData, Class type) {
        final String [] masterNames = metaData.getColumnNames(com.bc.tasktracker.jpa.entities.master.Task.class);
        System.out.println("Type: " + type + "\nNames: " + Arrays.toString(masterNames));
    }
}

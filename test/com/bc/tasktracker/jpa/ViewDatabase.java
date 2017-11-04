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

import com.bc.jpa.EntityUpdater;
import com.bc.jpa.context.PersistenceContext;
import com.bc.jpa.context.PersistenceContextEclipselinkOptimized;
import com.bc.jpa.search.BaseSearchResults;
import com.bc.jpa.search.SearchResults;
import com.bc.node.NodeFormat;
import com.bc.sql.MySQLDateTimePatterns;
import java.awt.Dimension;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 1, 2017 9:44:24 PM
 */
public class ViewDatabase {

    public static void main(String... args) {
        
        final String persistenceUnit = "bctasktrackerPUslave";
        
        final Class entityType = com.bc.tasktracker.jpa.entities.master.Unit.class;
        
        final int maxResultsToDisplay = 20;
        
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
            
            jpa.getContext(persistenceUnit).loadMetaData();
            
            System.out.println(new NodeFormat().format(jpa.getMetaData(false).getMetaData(persistenceUnit).getNode()));

            final SearchResults searchResults = new BaseSearchResults(
                    jpa.getDao(persistenceUnit).forSelect(entityType)
            );
            
            final int searchResultsSize = searchResults.getSize();

            final String [] columnNames = jpa.getContext(persistenceUnit)
                    .getMetaData(true).getColumnNames(entityType);

            final EntityUpdater updater = jpa.getEntityUpdater(persistenceUnit, entityType);

            final TableModel tableModel = new AbstractTableModel() {
                @Override
                public String getColumnName(int column) {
                    return columnNames[column];
                }
                @Override
                public int getRowCount() {
                    return searchResultsSize < maxResultsToDisplay ? searchResultsSize : maxResultsToDisplay;
                }
                @Override
                public int getColumnCount() {
                    return columnNames.length;
                }
                @Override
                public Object getValueAt(int rowIndex, int columnIndex) {
                    final Object entity = searchResults.get(rowIndex);
                    return updater.getValue(entity, columnNames[columnIndex]);
                }
            };

            final Dimension dimension = new Dimension(700, 500);

            final JTable table = new JTable(tableModel);
//            table.setSize(dimension);

            final JScrollPane scrolls = new JScrollPane(table);
//            scrolls.setSize(dimension);

            final JFrame frame = new JFrame("Displaying rows of type: " + entityType);
            frame.setSize(dimension);
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.getContentPane().add(scrolls);

            frame.setVisible(true);

        }catch(Exception e) {
            
            e.printStackTrace();
        }
    }
}

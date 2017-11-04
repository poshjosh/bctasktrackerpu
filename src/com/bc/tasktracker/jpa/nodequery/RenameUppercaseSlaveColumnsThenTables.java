package com.bc.tasktracker.jpa.nodequery;

import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.metadata.PersistenceNodeBuilder;
import com.bc.node.Node;
import com.bc.node.NodeFormat;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 17, 2017 2:22:06 PM
 */
public class RenameUppercaseSlaveColumnsThenTables {

    private static final Logger logger = Logger.getLogger(RenameUppercaseSlaveColumnsThenTables.class.getName());

    private final PersistenceUnitContext puContext;
    
    public RenameUppercaseSlaveColumnsThenTables(PersistenceUnitContext puContext) {
        this.puContext = Objects.requireNonNull(puContext);
    }

    public void apply(Class masterEntityType, Class slaveEntityType) {
        try{
            
            final Predicate<Node<String>> nodeValueIsUpperCaseTest = (node) -> {
                try{
                    return node.getValueOrDefault(null).toUpperCase().equals(node.getValueOrDefault(null));
                }catch(RuntimeException e) {
                    logger.log(Level.WARNING, "Unexpected Exception", e);
                    throw new RuntimeException(e.getLocalizedMessage());
                }    
            };
            
            final Function<Class, Node<String>> puNodeSupplier = 
                    new PersistenceUnitNodeSupplier(puContext.getPersistenceContext());

            final Node<String> masterPuNode = puNodeSupplier.apply(masterEntityType);
            logger.finer(() -> "MASTER PERSISTENCE UNIT NODE\n" + new NodeFormat().format(masterPuNode));            

            final Node<String> slavePuNode = puNodeSupplier.apply(slaveEntityType);
            logger.finer(() -> "SLAVE PERSISTENCE UNIT NODE\n" + new NodeFormat().format(slavePuNode));

            logger.fine(() -> "Persistence Unit Node to visit: " + slavePuNode);

            final QueryBuilder queryBuilder = new QueryBuilder(
                    puContext.getPersistenceContext(), 
                    Objects.requireNonNull(slavePuNode.getValueOrDefault(null)), 
                    false);

            final Predicate<Node<String>> tableFilter = new NodeLevelTest<String>(PersistenceNodeBuilder.NODE_LEVEL_TABLE)
                    .and(nodeValueIsUpperCaseTest)
                    .and((node) -> !node.getValueOrDefault(null).startsWith("SYS"));
            
            final Predicate<Node<String>> columnFilter = new NodeLevelTest<String>(PersistenceNodeBuilder.NODE_LEVEL_COLUMN)
                    .and(nodeValueIsUpperCaseTest).and((node) -> {
                        final Node<String> parent = node.getParentOrDefault(null);
                        return parent != null && tableFilter.test(parent);
                    });
            
            new ExecuteQueryForNodes(
                    puContext, 
                    slaveEntityType,
                    columnFilter,
                    new QueryRenameColumn(queryBuilder, masterPuNode)
            ).run();

            new ExecuteQueryForNodes(
                    puContext, 
                    slaveEntityType,
                    tableFilter,
                    new QueryRenameTable(queryBuilder, masterPuNode)
            ).run();
            
        }catch(Exception e) {
            logger.log(Level.WARNING, "", e);
        }
    }
}

package org.apache.pig;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.mapreduce.Job;
import org.apache.pig.classification.InterfaceAudience;
import org.apache.pig.classification.InterfaceStability;

/**
 * This interface defines how a loader can support predicate pushdown.
 * If a given loader implements this interface, pig will pushdown predicates based on
 * type of operations supported by the loader on given set of fields.
 * @since Pig 0.14
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public interface PredicatePushdown {
    /**
     * Find what fields of the data can support predicate pushdown.
     * @param location Location as returned by 
     * {@link LoadFunc#relativeToAbsolutePath(String, org.apache.hadoop.fs.Path)}
     * 
     * @param job The {@link Job} object - this should be used only to obtain 
     * cluster properties through {@link Job#getConfiguration()} and not to set/query
     * any runtime job information.  
     * 
     * @return array of field names of the partition keys. Implementations 
     * should return null to indicate that there are no fields that support predicate pushdown
     *
     * @throws IOException if an exception occurs while retrieving predicate fields
     */
    String[] getPredicateFields(String location, Job job) 
            throws IOException;
    
    /**
     * Push down expression to the loader
     * 
     * @param predicate expression to be filtered by the loader.
     * @throws IOException
     */
    void setPredicate(Expression predicate) throws IOException;
    
    /**
     * Indicate operations on fields supported by the loader for predicate pushdown
     * 
     * @return List of operations supported by the predicate pushdown loader
     */
    List<Expression.OpType> getSupportedExpressions();
    
    /**
     * Indicate whether predicate expression pushed is considered just a hint (best-effort) by the loader.
     * set return value to true, if predicate expression is not true for all the 
     * tuples returned by the loader. In which case, pig will re-evaluate the expression to ensure filtering.
     * 
     * set return value to false, if predicate expression is guaranteed to be true for all the
     * tuples returned by the loader. In this case, pig will remove this expression from pig's plan to avoid re-evaluation.
     * 
     * @return true to set expression as hint, 
     * false to indicate that expression is not a hint, and must be guaranteed by the loader
     */
    boolean isExpressionHint();
}

/**
 * An interface to compute the similarity between two items
 */

package alg.np.similarity.metric;

public interface SimilarityMetric 
{
	/**
	 * computes the similarity between items
	 * @param X - the id of the first item 
	 * @param Y - the id of the second item
	 */
	public double getItemSimilarity(final Integer X, final Integer Y);
}

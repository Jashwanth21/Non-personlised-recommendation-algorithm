/**
 * Compute the similarity between two items based on the Cosine between item genome scores
 */ 

package alg.np.similarity.metric;

import java.util.Set;

import profile.Profile;
import util.reader.DatasetReader;

public class GenomeMetric implements SimilarityMetric
{
	private DatasetReader reader; // dataset reader
	
	/**
	 * constructor - creates a new GenomeMetric object
	 * @param reader - dataset reader
	 */
	public GenomeMetric(final DatasetReader reader)
	{
		this.reader = reader;
	}
	
	/**
	 * computes the similarity between items
	 * @param X - the id of the first item 
	 * @param Y - the id of the second item
	 */
	public double getItemSimilarity(final Integer X, final Integer Y)
	{
		// calculate similarity using weighted Jaccard
		Profile XGenome = reader.getItem(X).getGenomeScores();
		Profile YGenome = reader.getItem(Y).getGenomeScores();
		Set<Integer> commonIds = XGenome.getCommonIds(YGenome);
		double num = 0, den = 0;
		for(Integer id : commonIds)
		{
			num = num + Math.min(XGenome.getValue(id), YGenome.getValue(id));
			den = den + Math.max(XGenome.getValue(id), YGenome.getValue(id));
		}
		return (den > 0 ) ? num / den : 0;
	}
}

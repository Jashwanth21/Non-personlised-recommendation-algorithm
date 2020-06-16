/**
 * Compute the similarity between two items based on the Cosine between item ratings
 */ 

package alg.np.similarity.metric;

import java.util.Map;
import java.util.Set;

import profile.Profile;
import util.reader.DatasetReader;

public class RatingMetric implements SimilarityMetric
{
	private DatasetReader reader; // dataset reader

	/**
	 * constructor - creates a new RatingMetric object
	 * @param reader - dataset reader
	 */
	public RatingMetric(final DatasetReader reader)
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
		// calculate similarity using Cosine
		double dprod = 0;
		Map<Integer, Profile> itemProfiles = reader.getItemProfiles();
		Profile Xrating = itemProfiles.get(X);
		Profile Yrating = itemProfiles.get(Y);
		double norX, norY;
		norX = Xrating.getNorm();
		norY = Yrating.getNorm();
		Set<Integer> commonIds = Xrating.getCommonIds(Yrating);
		for(Integer id : commonIds)
			dprod = dprod + Xrating.getValue(id) * Yrating.getValue(id);
		double den = (norX * norY);
		return (den>0) ? dprod/den : 0;
	}
}

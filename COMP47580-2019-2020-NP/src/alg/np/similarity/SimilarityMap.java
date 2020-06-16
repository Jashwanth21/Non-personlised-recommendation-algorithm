/**
 * This class computes and stores the pairwise similarities between all items
 */

package alg.np.similarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import profile.Profile;
import alg.np.similarity.metric.SimilarityMetric;
import util.reader.DatasetReader;

public class SimilarityMap 
{
	private Map<Integer,Profile> simMap; // stores item-item similarities

	/**
	 * constructor - creates a new SimilarityMap object
	 */
	public SimilarityMap()
	{
		simMap = new HashMap<Integer,Profile>();
	}

	/**
	 * constructor - creates a new SimilarityMap object
	 * @param reader - dataset reader
	 * @param metric - similarity metric
	 */
	public SimilarityMap(final DatasetReader reader, final SimilarityMetric metric)
	{		
		simMap = new HashMap<Integer,Profile>();

		// get the set of item ids
		Set<Integer> itemIds = reader.getItems().keySet();

		// compute pairwise similarities between item profiles
		for(Integer id1: itemIds)
			for(Integer id2: itemIds)
				if(id2 < id1) {
					if(metric instanceof alg.np.similarity.metric.GenreMetric ||
							metric instanceof alg.np.similarity.metric.GenomeMetric ||
							metric instanceof alg.np.similarity.metric.RatingMetric) { // similarities are symmetric
						double sim = metric.getItemSimilarity(id1, id2);
						if (sim > 0) {
							setSimilarity(id1, id2, sim);
							setSimilarity(id2, id1, sim);
						}
					} else if (metric instanceof alg.np.similarity.metric.IncConfidenceMetric) { // similarities are not symmetric
						double sim = metric.getItemSimilarity(id1, id2);
						if(sim > 1) setSimilarity(id1, id2, sim);
						
						sim = metric.getItemSimilarity(id2, id1);
						if(sim > 1) setSimilarity(id2, id1, sim);
					} else {
						System.out.println("Error - invalid similarity metric");
						System.exit(1);
					}
				}
	}

	/**
	 * @returns the numeric IDs of the profiles
	 */
	public Set<Integer> getIds()
	{
		return simMap.keySet();
	}

	/**
	 * @returns the similarity profile
	 * @param the numeric ID of the profile
	 */
	public Profile getSimilarities(Integer id)
	{
		return simMap.get(id);
	}

	/**
	 * @returns the similarity between two profiles
	 * @param the numeric ID of the first profile
	 * @param the numeric ID of the second profile
	 */
	public double getSimilarity(final Integer id1, final Integer id2)
	{
		if(simMap.containsKey(id1))
			return (simMap.get(id1).contains(id2) ? simMap.get(id1).getValue(id2).doubleValue() : 0);
		else 
			return 0;
	}

	/**
	 * adds the similarity between two profiles to the map
	 * @param the numeric ID of the first profile
	 * @param the numeric ID of the second profile
	 */
	public void setSimilarity(final Integer id1, final Integer id2, final double sim)
	{
		Profile profile = simMap.containsKey(id1) ? simMap.get(id1) : new Profile(id1);
		profile.addValue(id2, new Double(sim));
		simMap.put(id1, profile);
	}

	/**
	 * @returns a string representation of all similarity values
	 */
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		for(Integer id: simMap.keySet())
			buf.append(simMap.get(id).toString());

		return buf.toString();
	}
}
package alg.np;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import alg.np.similarity.SimilarityMap;
import alg.np.similarity.metric.SimilarityMetric;
import profile.Profile;
import util.ScoredThingDsc;
import util.reader.DatasetReader;

public class NonPersonalisedRecommender {
	private SimilarityMap simMap; // similarity map - stores all item-item similarities

	/**
	 * constructor - creates a new NonPersonalisedRecommender object
	 * @param reader - dataset reader
	 * @param metric - the item-item similarity metric
	 */
	public NonPersonalisedRecommender(final DatasetReader reader, final SimilarityMetric metric) {
		this.simMap = new SimilarityMap(reader, metric);
	}

	/**
	 * @returns the recommendations based on the target item
	 * @param itemId - the target item ID
	 */
	public List<Integer> getRecommendations(final Integer itemId)
	{	
		// create a list to store recommendations
		List<Integer> recs = new ArrayList<Integer>();

		SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); // store all similarities in order of descending similarity in a sorted set

		Profile profile = simMap.getSimilarities(itemId); // get the item similarity profile
		if(profile != null)
		{
			for(Integer id: profile.getIds()) // iterate over each item in the profile
			{
				double sim = profile.getValue(id);
				if(sim > 0)
					ss.add(new ScoredThingDsc(sim, id));
			}
		}

		// save all recommended items in descending order of similarity in the list
		for(Iterator<ScoredThingDsc> iter = ss.iterator(); iter.hasNext(); )
		{
			ScoredThingDsc st = iter.next();
			Integer id = (Integer)st.thing;
			recs.add(id);
		}

		return recs;
	}
}

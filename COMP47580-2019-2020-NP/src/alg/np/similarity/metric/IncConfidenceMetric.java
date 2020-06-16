/**
 * Compute the similarity between two items based on increase in confidence
 */ 

package alg.np.similarity.metric;

import java.util.Map;
import java.util.Set;

import profile.Profile;
import util.reader.DatasetReader;

public class IncConfidenceMetric implements SimilarityMetric
{
	private static double RATING_THRESHOLD = 4.0; // the threshold rating for liked items 
	private DatasetReader reader; // dataset reader

	/**
	 * constructor - creates a new IncConfidenceMetric object
	 * @param reader - dataset reader
	 */
	public IncConfidenceMetric(final DatasetReader reader)
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
		// calculate similarity using conf(X => Y) / conf(!X => Y)
		double supX = 0, supNotX = 0, supXAndY = 0, supNotXAndY = 0;
		double confXY = 0, confNotXY = 0;
		int CX = 0, CnX = 0, CXAndY = 0, CnXAndY = 0;
		Map<Integer, Profile> itemProfiles = reader.getItemProfiles();
		Profile Xinc = itemProfiles.get(X);
		Profile Yinc = itemProfiles.get(Y);
		int nX = Xinc.getSize();
		int nY = Yinc.getSize();
		int nXandY = Xinc.getCommonIds(Yinc).size();
		int nXY = nX + nY - nXandY;
		Set<Integer> X_ids = Xinc.getIds();
		Set<Integer> XY_ids = Xinc.getCommonIds(Yinc);
		for (Integer id : X_ids)
		{
			if (Xinc.getValue(id) >= RATING_THRESHOLD)
				CX++;
			else
				CnX++;
		}

		for (Integer id : XY_ids) {
			if (Xinc.getValue(id) >= RATING_THRESHOLD && Yinc.getValue(id) >= RATING_THRESHOLD)
				CXAndY++;
			else if (Xinc.getValue(id) < RATING_THRESHOLD && Yinc.getValue(id) >= RATING_THRESHOLD)
				CnXAndY++;
		}
		supX = (nX > 0) ? (CX * 1.0) / nX : 0;
		supNotX = (nX > 0) ? (CnX * 1.0) / nX : 0;
		supXAndY = (nXY > 0) ? (CXAndY * 1.0) / nXY : 0;
		supNotXAndY = (nXY > 0) ? (CnXAndY * 1.0) / nXY : 0;
		confXY = (supX > 0.0) ? supXAndY / supX : 0.0;
		confNotXY = (supNotX > 0.0) ? supNotXAndY / supNotX : 0.0;
		return (confNotXY > 0.0) ? confXY / confNotXY : 0.0;

	}
}

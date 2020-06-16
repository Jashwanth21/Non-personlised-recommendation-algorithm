/**
 * A class to evaluate a non-personalised recommender algorithm
 */

package util.np.evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alg.np.NonPersonalisedRecommender;
import util.Item;
import util.reader.DatasetReader;

public class Evaluator 
{		
	private NonPersonalisedRecommender alg; // non-personalised recommender algorithm
	private DatasetReader reader; // dataset reader
	private int k; // the number of recommendations to be made

	/**
	 * constructor - creates a new Evaluator object
	 * @param alg - the non-personalised recommender algorithm
	 * @param reader - dataset reader
	 * @param k - the number of recommendations to be made
	 */
	public Evaluator(final NonPersonalisedRecommender alg, final DatasetReader reader, final int k)
	{
		this.alg = alg;
		this.reader = reader;
		this.k = k;
	}

	/**
	 * @return the coverage which is given by the percentage of target 
	 * items for which at least one recommendation can be made
	 */
	public double getCoverage() {
		int nitems = 0;

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			List<Integer> recs = alg.getRecommendations(itemId);
			if (recs.size() > 0) 
				nitems++;
		}

		return (items.keySet().size() > 0) ? nitems * 1.0 / items.keySet().size() : 0;
	}

	/**
	 * @return the percentage of movies in the dataset which appear at 
	 * least once in the top-k recommendations made over all target items
	 */
	public double getRecommendationCoverage() {
		Set<Integer> allRecs = new HashSet<Integer>();

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			List<Integer> recs = alg.getRecommendations(itemId);
			for (int i = 0; i < recs.size() && i < k; i++)
				allRecs.add(recs.get(i));
		}
		
		return (items.keySet().size() > 0) ? allRecs.size() * 1.0 / (items.keySet().size()) : 0;
	}
	
	/**
	 * For a given target item, the percentage of items in the system
	 * which are capable of being recommended (i.e. those items which
	 * which have a similarity greater than 0 with the target items).
	 * The value returned is the average percentage over all target items.
	 */
	public double getItemSpaceCoverage() {
		double meanCoverage = 0;
		int nitems = 0;

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			List<Integer> recs = alg.getRecommendations(itemId);
			if (recs.size() > 0) {
				meanCoverage += (items.keySet().size() - 1 > 0) ? recs.size() * 1.0 / (items.keySet().size() - 1) : 0;
				nitems++;
			}
		}

		return (nitems > 0) ? meanCoverage / nitems : 0;
	}

	/**
	 * The popularity of a recommended movie is given by the percentage 
	 * of users in the system which have rated the movie.
	 * For a given target movie, the popularity of the top-k recommendations 
	 * made is calculated by taking the average of the popularity over each 
	 * recommended movie. 
	 * The value returned is the average popularity over all target movies.
	 */
	public double getRecommendationPopularity() {
		double meanPopularity = 0;
		int nitems = 0;

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			double popularity = 0;
			int counter = 0;
			List<Integer> recs = alg.getRecommendations(itemId);
			for (int i = 0; i < recs.size() && i < k; i++) {
				popularity += reader.getItemProfiles().get(recs.get(i)).getSize() * 1.0 / reader.getUserProfiles().size();
				counter++;
			}

			if (counter > 0) {
				meanPopularity += popularity / counter;
				nitems++;
			}
		}

		return (nitems > 0) ? meanPopularity / nitems : 0;
	}

	/**
	 * The relevance of a recommended movie is given by the mean of 
	 * the ratings the item received in the training set.
	 * For a given target item, the relevance of the top-k recommendations 
	 * made is calculated by taking the average of the mean rating over each 
	 * recommended item. 
	 * The value returned is the average relevance over all target items.
	 */
	public double getRecommendationRelevance() {
		double meanRelevance = 0;
		int nitems = 0;

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			double relevance = 0;
			int counter = 0;
			List<Integer> recs = alg.getRecommendations(itemId);
			for (int i = 0; i < recs.size() && i < k; i++) {
				relevance += reader.getItemProfiles().get(recs.get(i)).getMeanValue();
				counter++;
			}

			if (counter > 0) {
				meanRelevance += relevance / counter;
				nitems++;
			}
		}

		return (nitems > 0) ? meanRelevance / nitems : 0;
	}


	/**
	 * prints to standard output the top-k reommendations made for an item
	 * @param item - the target item
	 */
	public void printRecs(Item item) {
		Map<Integer,Item> items = reader.getItems();

		System.out.println("Title: " + item.getName());
		List<Integer> recs = alg.getRecommendations(item.getId());
		for (int i = 0; i < recs.size() && i < k; i++)
			System.out.println("\tRec " + (i + 1) + ": " + items.get(recs.get(i)).getName());
		System.out.println();
	}

	/**
	 * @param alg1
	 * @param alg2
	 * @return the number of common recommendations made by alg1 and alg2 (averaged over all target items)
	 */
	public double getNumberCommonRecs(NonPersonalisedRecommender alg1, NonPersonalisedRecommender alg2) {
		int sumCommon = 0;

		Map<Integer,Item> items = reader.getItems();
		for (Integer itemId: items.keySet()) {
			List<Integer> recs1 = alg1.getRecommendations(itemId);
			List<Integer> recs2 = alg2.getRecommendations(itemId);

			int count = 0;
			for (int i = 0; i < recs1.size() && i < k; i++)
				for (int j = 0; j < recs2.size() && j < k; j++)
					if (recs1.get(i) == recs2.get(j)) {
						count++;
						break;
					}

			sumCommon += count;
		}

		return (items.size() > 0) ? sumCommon * 1.0 / items.size() : 0;
	}
}

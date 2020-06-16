/**
 * A class to display the recommendations made by a non-personalised recommender algorithm
 */

package alg.np;

import java.io.File;
import java.util.Map;

import alg.np.similarity.metric.GenomeMetric;
import alg.np.similarity.metric.GenreMetric;
import alg.np.similarity.metric.IncConfidenceMetric;
import alg.np.similarity.metric.RatingMetric;
import alg.np.similarity.metric.SimilarityMetric;
import util.Item;
import util.np.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteNP {
	public static void main(String[] args)
	{		
		// set the paths and filenames of the item file, genome scores file, train file and test file ...
		String folder = "ml-20m-2019-2020";
		String itemFile = folder + File.separator + "movies-sample.txt";
		String itemGenomeScoresFile = folder + File.separator + "genome-scores-sample.txt";
		String trainFile = folder + File.separator + "train.txt";
		String testFile = folder + File.separator + "test.txt";	

		////////////////////////////////////////////////////////////////////////////
		// configure the non-personalised recommender algorithms and run experiments
		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);
		SimilarityMetric metric = new GenreMetric(reader); // run this class using different similarity metrics
		NonPersonalisedRecommender alg = new NonPersonalisedRecommender(reader, metric);
		
		// display the top-k recommendations for five items
		int k = 3; // the number of recommendations to be made for each target item
		Evaluator eval = new Evaluator(alg, reader, k);
		Map<Integer,Item> items = reader.getItems();
		int[] itemIds = {3668, 3639, 3578, 87232, 3681};
		for (int itemId: itemIds) {
			Item item = items.get(itemId);
			eval.printRecs(item);
		}
	}
}

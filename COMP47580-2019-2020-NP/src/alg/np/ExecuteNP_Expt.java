/**
 * A class to execute various non-personalised recommender algorithms.
 * Please do not edit this class.
 */

package alg.np;

import java.io.File;

import alg.np.similarity.metric.GenomeMetric;
import alg.np.similarity.metric.GenreMetric;
import alg.np.similarity.metric.IncConfidenceMetric;
import alg.np.similarity.metric.RatingMetric;
import alg.np.similarity.metric.SimilarityMetric;
import util.np.evaluator.Evaluator;
import util.reader.DatasetReader;

public class ExecuteNP_Expt {
	public static void main(String[] args)
	{	
		////////////////////////////////////////
		// *** please do not edit this class ***
		
		// set the paths and filenames of the item file, genome scores file, train file and test file ...
		String folder = "ml-20m-2019-2020";
		String itemFile = folder + File.separator + "movies-sample.txt";
		String itemGenomeScoresFile = folder + File.separator + "genome-scores-sample.txt";
		String trainFile = folder + File.separator + "train.txt";
		String testFile = folder + File.separator + "test.txt";	

		////////////////////////////////////////////////////////////////////////////
		// configure the non-personalised recommender algorithms and run experiments
		DatasetReader reader = new DatasetReader(itemFile, itemGenomeScoresFile, trainFile, testFile);

		// create an array of similarity metrics
		SimilarityMetric[] metrics = {
				new GenreMetric(reader),
				new GenomeMetric(reader),
				new RatingMetric(reader),
				new IncConfidenceMetric(reader)
		};

		System.out.println("k,algorithm,relevance,coverage,rec. coverage,item space coverage,rec. popularity");
		int k = 10; // the number of recommendations to be made for each target item
		String[] algorithms = {"Genre", "Genome", "Rating", "IncConfidence"};

		for (int i = 0; i < metrics.length; i++) {
			// create a NonPersonalisedRecommender object using the current similarity metric (metrics[i])
			NonPersonalisedRecommender alg = new NonPersonalisedRecommender(reader, metrics[i]);
			// create an Evaluator object using the current non-personalised recommender algorithm
			Evaluator eval = new Evaluator(alg, reader, k);
			// display results for the current non-personalised recommender algorithm
			System.out.println(k + "," + algorithms[i] + "," + 
					eval.getRecommendationRelevance() + "," + 
					eval.getCoverage() + "," + 
					eval.getRecommendationCoverage() + "," +
					eval.getItemSpaceCoverage() + "," +
					eval.getRecommendationPopularity());
		}
	}
}

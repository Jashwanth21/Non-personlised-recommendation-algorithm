/**
 * Compute the similarity between two items based on genres
 */

package alg.np.similarity.metric;

import java.util.Set;

import util.reader.DatasetReader;

public class GenreMetric implements SimilarityMetric
{	
	private DatasetReader reader; // dataset reader
	
	/**
	 * constructor - creates a new GenreMetric object
	 * @param reader - dataset reader
	 */
	public GenreMetric(final DatasetReader reader)
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
		// calculate similarity using overlap coefficient
		
		// get the genres sets for items X and Y
		Set<String> genresX = reader.getItem(X).getGenres();
		Set<String> genresY = reader.getItem(Y).getGenres();
		
		// get the number of common genres between items X and Y
		int count = 0;
		for (String s: genresX)
			if (genresY.contains(s))
				count++;
		
		// calculate the return the overlap coefficient - if division by zero occurs, return zero 
		int denom = Math.min(genresX.size(), genresY.size());
		return (denom > 0 ) ? count * 1.0 / denom : 0;
	}
}

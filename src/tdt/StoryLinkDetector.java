/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * This class is used to calculate the similarity between two stories. After
 * creating an instance, use the doStoryLinkDetection() firstly to detect the
 * links among stories of the corpus, so that you could use the other methods
 * normally, e.g., getCosineSimilarity().
 * 
 * @author Zewei Wu
 */
public class StoryLinkDetector {
	// ---------- PROTECTED -------------------------------------------------
	/**
	 * Preparing for the similarity calculation, e.g., calculating tfidf's.
	 * 
	 * @param corpus
	 *            The 'tfidf' member of the corpus would be set here.
	 * @param wordIDToStoryIndices
	 *            Used to help calculate the idf of 'tfidf'.
	 */
	protected static void doStoryLinkDetection(Vector<Story> corpus,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices,
		String tfidfFile, boolean isToLoadTfidf) {
		prepareTFIDF(corpus, wordIDToStoryIndices, tfidfFile, isToLoadTfidf);
	}

	/**
	 * @param story1
	 * @param story2
	 * @param threshold
	 * @return true if the similarity is above the threshold.
	 */
	protected static boolean isTwoStoriesSimilar(Story story1, Story story2,
		double threshold) {
		double similarity = getSimilarity(story1, story2);
		return similarity >= threshold;
	}

	/**
	 * For now, it simply calls the getCosineSimilarity(...)
	 * 
	 * @param story1
	 * @param story2
	 * @return the similarity between this two stories.
	 */
	protected static double getSimilarity(Story story1, Story story2) {
		return getCosineSimilarity(story1, story2);
	}

	/**
	 * @param story1
	 * @param story2
	 * @return the cosine similarity between two stories, using the tf-idf
	 *         vectors in them.
	 */
	protected static double getCosineSimilarity(Story story1, Story story2) {
		double similarity = 0.0;
		double innerProduct = 0.0;
		double squareSum1 = 0.0;
		double squareSum2 = 0.0;

		HashMap<Integer, Double> tfidf1 = story1.getTfidf();
		HashMap<Integer, Double> tfidf2 = story2.getTfidf();

		for (Entry<Integer, Double> entry : tfidf1.entrySet()) {
			int key = entry.getKey();
			double value = entry.getValue();
			if (tfidf2.containsKey(key)) {
				innerProduct += value * tfidf2.get(key);
				squareSum1 += value * value;
				squareSum2 += tfidf2.get(key) * tfidf2.get(key);
			}
		}

		similarity = innerProduct / Math.sqrt(squareSum1 * squareSum2);

		return similarity;
	}

	// ---------- PRIVATE -------------------------------------------------
	/**
	 * Calculating tfidf's of stories in corpus
	 * 
	 * @param corpus
	 * @param wordIDToStoryIndices
	 * @throws IOException
	 */
	private static void prepareTFIDF(Vector<Story> corpus,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices,
		String tfidfFile, boolean isToLoadTfidf) {
		if (isToLoadTfidf) { // load tfidf from tfidfFile
			loadTFIDF(corpus, tfidfFile);
		} else { // save tfidf to tfidfFile
			setTFIDFOfCorpus(corpus, wordIDToStoryIndices);
			saveTFIDF(corpus, tfidfFile);
		}
	}

	/**
	 * Set 'tfidf' for all stories in corpus.
	 * 
	 * @param corpus
	 * @param storiesIndexWithCertainWord
	 */
	private static void setTFIDFOfCorpus(Vector<Story> corpus,
		HashMap<Integer, HashSet<Integer>> storiesIndexWithCertainWord) {
		System.out.println("Calculating tfidf......");
		for (int count = 0; count < corpus.size(); ++count) {
			if (count % 100 == 0)
				System.out.println(count + " / " + corpus.size());
			corpus.get(count).setTfidfBasedOnCorpus(corpus,
				storiesIndexWithCertainWord);
		}
		System.out.println("Done.");

	}

	/**
	 * Save the tfidf's of corpus to tfidfFile
	 * 
	 * @param corpus
	 * @param tfidfFile
	 */
	private static void saveTFIDF(Vector<Story> corpus, String tfidfFile) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(tfidfFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			for (Story curStory : corpus) {
				HashMap<Integer, Double> tfidf = curStory.getTfidf();
				for (Entry<Integer, Double> pair : tfidf.entrySet()) {
					osw.append(pair.getKey() + ":" + pair.getValue() + " ");
				}
				osw.append("\n");
			}
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Load the tfidf's of corpus from tfidfFile
	 * 
	 * @param corpus
	 * @param tfidfFile
	 * @throws IOException
	 */
	private static void loadTFIDF(Vector<Story> corpus, String tfidfFile) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(tfidfFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			HashMap<Integer, Double> tfidf;
			int i = 0;
			while ((line = br.readLine()) != null) {
				tfidf = new HashMap<Integer, Double>();
				String[] pairs = line.split(" ");
				for (String pair : pairs) {
					String[] i2d = pair.split(":");
					tfidf.put(Integer.parseInt(i2d[0]),
						Double.parseDouble(i2d[1]));
				}
				corpus.get(i).setTfidf(tfidf);
			}
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

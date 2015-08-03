/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class Main {

	/**
	 * @param args
	 *            Unused.
	 */
	public static void main(String[] args) {
		Vector<Story> corpus = new Vector<Story>();
		Glossary glossary = new Glossary();
		Vector<Story> actualFirstStories = new Vector<Story>();
		// Used to record the files list containing a certain word.
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices =
			new HashMap<Integer, HashSet<Integer>>();

		//		String tknDir = "Dataset/mttkn/";
		//		String bndDir = "Dataset/mttkn_bnd/";
		String sgmDir = "Dataset/sgm/";
		String ansFile = "Dataset/answer.txt";

		System.out.println("====== Data Preprocessing Start ======");
		DataPreprocessor dataPreprocessor = new DataPreprocessor();
		//	dataPreprocessor.doDataPreprocessing(corpus, glossary,
		//		wordIDToStoryIndices, actualFirstStories, tknDir, bndDir, ansFile);
		dataPreprocessor.doDataPreprocessing(corpus, glossary,
			wordIDToStoryIndices, actualFirstStories, sgmDir, ansFile);
		System.out.println("====== Data Preprocessing End ======");

		System.out.println();
		System.out.println("corpus.size() = " + corpus.size());
		assert (corpus.size() > 0);
		System.out.println("glossary.size() = " + glossary.size());
		assert (glossary.size() > 0);
		System.out.println("wordIDToStoryIndices.size() = "
			+ wordIDToStoryIndices.size());
		assert (wordIDToStoryIndices.size() > 0);
		System.out.println("actualFirstStories.size() = "
			+ actualFirstStories.size());
		assert (actualFirstStories.size() > 0);
		System.out.println();

		System.out.println("====== Story Link Detection Start ======");
		StoryLinkDetector.doStoryLinkDetection(corpus, wordIDToStoryIndices,
			"Dataset/tfidf.dat", false);
		System.out.println("====== Story Link Detection End ======");

		System.out.println("====== Topic Detection Start ======");
		TopicDetector topicDetector = new TopicDetector();
		int numOfTopics = topicDetector.doTopicDetection(corpus);
		System.out.println("====== Topic Detection End ======");

		System.out.println();
		System.out.println("numOfTopics = " + numOfTopics);
		assert (numOfTopics > 0);
		System.out.println();

		System.out.println("====== First Story Detection Start ======");
		Vector<Story> firstStories =
			FirstStoryDetector.doFirstStoryDetection(corpus, numOfTopics);
		System.out.println("====== First Story Detection End ======");

		System.out.println();
		System.out.println("firstStories.size() = " + firstStories.size());
		assert (firstStories.size() == numOfTopics);
		System.out.println();

		System.out.println("====== Presentation Start ======");
		String resultFile = "Dataset/result.dat";
		Presentator.doPresentation(firstStories, corpus, glossary, numOfTopics,
			resultFile);
		System.out.println("====== Presentation End ======");

		System.out.println("====== Evaluation Start ======");
		double normCdet =
			Evaluator.doEvaluation(corpus, actualFirstStories, firstStories);
		System.out.println("normCdet = " + normCdet);
		System.out.println("====== Evaluation End ======");
	}
}
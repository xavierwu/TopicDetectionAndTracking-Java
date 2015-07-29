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
	 */
	public static void main(String[] args) {
		Vector<Story> corpus = new Vector<Story>();
		Glossary glossary = new Glossary();
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices =
			new HashMap<Integer, HashSet<Integer>>();

		String tknDir = "Dataset/mttkn/";
		String bndDir = "Dataset/mttkn_bnd/";

		DataPreprocessor dataPreprocessor = new DataPreprocessor();
		dataPreprocessor.doDataPreprocessing(corpus, glossary,
			wordIDToStoryIndices, tknDir, bndDir);

		System.out.println("corpus.size() = " + corpus.size());
		assert (corpus.size() > 0);
		System.out.println("glossary.size() = " + glossary.size());
		assert (glossary.size() > 0);
		System.out.println("wordIDToStoryIndices.size() = "
			+ wordIDToStoryIndices.size());
		assert (wordIDToStoryIndices.size() > 0);

		StoryLinkDetector storyLinkDetector = new StoryLinkDetector();
		storyLinkDetector.doStoryLinkDetection(corpus, wordIDToStoryIndices);

		TopicDetector topicDetector = new TopicDetector();
		int numOfTopics = topicDetector.doTopicDetection(corpus);

		System.out.println("numOfTopics = " + numOfTopics);
		assert (numOfTopics > 0);

		FirstStoryDetector firstStoryDetector = new FirstStoryDetector();
		Vector<Story> firstStories = firstStoryDetector.doFirstStoryDetection();

		System.out.println("firstStories.size() = " + firstStories.size());
		assert (firstStories.size() == numOfTopics);

		Evaluator evaluator = new Evaluator();
		evaluator.doEvaluation();

		Presentator presentator =
			new Presentator(firstStories, corpus, glossary, numOfTopics);
		presentator.doPresentation();
	}
}
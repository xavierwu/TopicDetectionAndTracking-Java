/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class Presentator {
	/**
	 * @param firstStories
	 * @param corpus
	 * @param glossary
	 * @param numOfTopics
	 */
	protected static void doPresentation(Vector<Story> firstStories,
		Vector<Story> corpus, Glossary glossary, int numOfTopics) {
		printClusters(corpus, glossary, numOfTopics, false);
	}

	/**
	 * @param firstStories
	 * @param corpus
	 * @param glossary
	 * @param numOfTopics
	 * @param resultFile
	 */
	protected static void doPresentation(Vector<Story> firstStories,
		Vector<Story> corpus, Glossary glossary, int numOfTopics,
		String resultFile) {
		printClustersToFile(corpus, glossary, numOfTopics, false, resultFile);
	}

	/**
	 * @param firstStories
	 * @param glossary
	 * @param isPrintItAll
	 */
	protected static void printFirstStories(Vector<Story> firstStories,
		Glossary glossary, boolean isPrintItAll) {
		for (Story curStory : firstStories) {
			System.out.println(curStory.getTopicID());
			if (isPrintItAll)
				System.out.println(curStory.toString(glossary));
			else
				System.out.println(curStory.getTimeStamp());
		}
	}

	/**
	 * @param corpus
	 * @param glossary
	 * @param numOfTopics
	 * @param isPrintItAll
	 */
	protected static void printClusters(Vector<Story> corpus,
		Glossary glossary, int numOfTopics, boolean isPrintItAll) {
		for (int curTopic = 0; curTopic < numOfTopics; ++curTopic) {
			System.out.println(curTopic);
			for (Story curStory : corpus) {
				if (curStory.getTopicID() == curTopic) {
					if (isPrintItAll)
						System.out.println(curStory.toString(glossary));
					else
						System.out.println(curStory.getTimeStamp());
				}
			}
		}
	}

	/**
	 * @param corpus
	 * @param glossary
	 * @param numOfTopics
	 * @param isPrintItAll
	 * @param resultFile
	 */
	protected static void printClustersToFile(Vector<Story> corpus,
		Glossary glossary, int numOfTopics, boolean isPrintItAll,
		String resultFile) {
		try {
			FileOutputStream fos = new FileOutputStream(resultFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			for (int curTopic = 0; curTopic < numOfTopics; ++curTopic) {
				osw.append(String.valueOf(curTopic) + "\n");
				for (Story curStory : corpus) {
					if (curStory.getTopicID() == curTopic) {
						if (isPrintItAll)
							osw.append(curStory.toString(glossary) + "\n");
						else
							osw.append(curStory.getTimeStamp() + "\n");
					}
				}
			}
			osw.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

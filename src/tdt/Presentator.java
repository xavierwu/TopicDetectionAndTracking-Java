/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class Presentator {
	protected static void doPresentation(Vector<Story> firstStories,
		Vector<Story> corpus, Glossary glossary, int numOfTopics) {
		printClusters(corpus, glossary, numOfTopics, false);
	}

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
}

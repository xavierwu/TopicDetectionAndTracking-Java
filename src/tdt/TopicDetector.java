/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * TODO: class TopicDetector: Zhaoqi
 * 
 * @author Zhaoqi Wang
 */
class TopicDetector {

	/**
	 * 
	 */
	TopicDetector() {

	}

	/**
	 * 
	 * @param corpus
	 * @return
	 */
	int doTopicDetection(Vector<Story> corpus) {
		int numOfTopics = 7;

		KMeans(corpus, numOfTopics);

		return numOfTopics;
	}

	/**
	 * 
	 * @param corpus
	 * @param numOfTopics
	 */
	void KMeans(Vector<Story> corpus, int numOfTopics) {
		Vector<Story> means = new Vector<Story>();

		initMeans(means, corpus, numOfTopics);

		int loopCnt = 10;

		while (loopCnt > 0) {
			for (int i = 0; i < corpus.size(); i++) {
				cluster(corpus.get(i), means, numOfTopics);
			}

			for (int i = 0; i < numOfTopics; i++) {
				means.set(i, getMean(corpus, i));
			}

			loopCnt--;
		}
	}

	/**
	 * 
	 * @param corpus
	 * @param topicID
	 * @return
	 */
	private Story getMean(Vector<Story> corpus, int topicID) {
		// TODO Auto-generated method stub
		Story mean = new Story();
		int storyNumOfTopic = 0;
		HashMap<Integer, Double> tfidfOfMean = new HashMap<Integer, Double>();

		for (int i = 0; i < corpus.size(); i++) {
			if (corpus.get(i).getTopicID() == topicID) {
				storyNumOfTopic++;

				HashMap<Integer, Double> tfidf = corpus.get(i).getTfidf();

				for (Entry<Integer, Double> entry : tfidf.entrySet()) {		
					System.out.println("key:" + entry.getKey());
					System.out.println("value:" + entry.getValue());

					try {
						if (tfidfOfMean.containsKey(entry.getKey())) {
							tfidfOfMean.put(
									entry.getKey(),
									tfidfOfMean.get(entry.getKey())
											+ entry.getValue());
						} else {
							tfidfOfMean.put(entry.getKey(), entry.getValue());
						}
					} catch (NullPointerException e) {
						tfidfOfMean.put(entry.getKey(), entry.getValue());
					}
					mean.setTfidf(tfidfOfMean);
				}
			}
		}

		// HashMap<Integer, Double> tfidfOfMean = mean.getTfidf();

		for (Map.Entry<Integer, Double> entry : tfidfOfMean.entrySet()) {
			tfidfOfMean.put(entry.getKey(), entry.getValue() / storyNumOfTopic);
		}

		mean.setTfidf(tfidfOfMean);

		return mean;
	}

	/**
	 * 
	 * @param story
	 * @param means
	 * @param numOfTopics
	 */
	private void cluster(Story story, Vector<Story> means, int numOfTopics) {
		// TODO Auto-generated method stub
		double maxSimilarity = 0;

		for (int i = 0; i < numOfTopics; i++) {
			double similarity = StoryLinkDetector.getSimilarity(story,
					means.get(i));

			if (similarity > maxSimilarity) {
				maxSimilarity = similarity;
				story.setTopicID(i);
			}
		}
	}

	/**
	 * 
	 * @param means
	 * @param corpus
	 * @param numOfTopics
	 */
	private void initMeans(Vector<Story> means, Vector<Story> corpus,
			int numOfTopics) {
		// TODO Auto-generated method stub
		for (int i = 0; i < numOfTopics; i++) {
			means.add(corpus.get(i));
		}
	}
}

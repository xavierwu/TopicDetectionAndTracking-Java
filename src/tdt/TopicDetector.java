/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * This class is used to classify the corpus. Give every story an unique topic
 * ID. This is the core step of the whole project. We will try to find the best
 * algorithm for the purpose of classification.
 * 
 * @author Zhaoqi Wang
 */
class TopicDetector {

	/**
	 * Constructor.
	 * Nothing to be initialized for the time being.
	 */
	TopicDetector() {

	}

	/**
	 * Function to be called by other class, for the purpose of encapsulation.
	 * It uses certain methods to label these stories, make these stories have
	 * an unique topic ID. After the function being called, the whole corpus 
	 * will be classified.
	 * @param corpus
	 * @return topic number, which is set by certain method.
	 */
	int doTopicDetection(Vector<Story> corpus) {
		/* 
		 * Topic number, set to 7 for the time being. 
		 * We will try to make it can be set automatically. 
		 */
		int numOfTopics = 7;

		KMeans(corpus, numOfTopics);

		return numOfTopics;
	}

	/**
	 * K-means clustering.
	 * It aims to partition n stories into k clusters, in which each story
	 * belongs to the the cluster with nearest mean.
	 * Special Notice: the second parameter numOfTopics can't be zero.
	 * @param corpus
	 * @param numOfTopics
	 */
	private void KMeans(Vector<Story> corpus, int numOfTopics) {
		Vector<Story> means = new Vector<Story>();	// Collection of centroids.

		initMeans(means, corpus, numOfTopics);

		int loopCnt = 10;	// Iteration counter of k-means.

		while (loopCnt > 0) {
			/* Assignment step. */
			for (int i = 0; i < corpus.size(); i++) {
				cluster(corpus.get(i), means, numOfTopics);
			}

			/* Update step. */
			for (int i = 0; i < numOfTopics; i++) {
				means.set(i, getMean(corpus, i));
			}

			loopCnt--;
		}
	}

	/**
	 * Update step of k-means clustering.
	 * Function to get the mean of stories who have the same topic ID.
	 * The new centroid may be used in the assignment step.
	 * Notice: topicID must be provided.
	 * @param corpus
	 * @param topicID
	 * @return a new centroid of a certain topic.
	 */
	private Story getMean(Vector<Story> corpus, int topicID) {
		Story mean = new Story();	// Centroid of this topic.
		int storyNumOfTopic = 0;	// story number of this topic.
		
		/* TF-IDF of this centroid. */
		HashMap<Integer, Double> tfidfOfMean = new HashMap<Integer, Double>();

		/* 
		 * For every story in this corpus, we have to check if it has the same
		 * topic ID as we want. If do, we add it to the mean of the cluster.
		 */
		for (int i = 0; i < corpus.size(); i++) {
			if (corpus.get(i).getTopicID() == topicID) {
				storyNumOfTopic++;

				HashMap<Integer, Double> tfidf = corpus.get(i).getTfidf();

				
				/* Every entry has to be checked. */
				for (Entry<Integer, Double> entry : tfidf.entrySet()) {		
					try {
						if (tfidfOfMean.containsKey(entry.getKey())) {
							tfidfOfMean.put(entry.getKey(),
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

		/* Cause we only did the addition before, so mean has to be calculated here. */ 
		for (Map.Entry<Integer, Double> entry : tfidfOfMean.entrySet()) {
			tfidfOfMean.put(entry.getKey(), entry.getValue() / storyNumOfTopic);
		}

		mean.setTfidf(tfidfOfMean);

		return mean;
	}

	/**
	 * Assignment step of k-means clustering.
	 * It assigns each story an unique topic ID, which may be used in the 
	 * update step of the next loop.
	 * @param story
	 * @param means
	 * @param numOfTopics
	 */
	private void cluster(Story story, Vector<Story> means, int numOfTopics) {
		/* max similarity between this piece of story and means. */
		double maxSimilarity = 0;

		for (int i = 0; i < numOfTopics; i++) {
			double similarity = 
					StoryLinkDetector.getSimilarity(story, means.get(i));

			if (similarity > maxSimilarity) {
				maxSimilarity = similarity;
				story.setTopicID(i);
			}
		}
	}

	/**
	 * Initialization of means.
	 * Set to be the foremost stories, for the time being.
	 * @param means
	 * @param corpus
	 * @param numOfTopics
	 */
	private void initMeans(Vector<Story> means, Vector<Story> corpus,
			int numOfTopics) {
		for (int i = 0; i < numOfTopics; i++) {
			means.add(corpus.get(i));
		}
	}
}

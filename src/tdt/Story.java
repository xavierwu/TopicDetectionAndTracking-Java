/**
 * Created on: Jul 29, 2015
 */

package tdt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

/**
 * @author Zewei Wu
 */
public class Story {
	// ---------- FIELDS ------------------------------------------------------
	/**
	 * Default value of 'topicID', meaning the story is clustered.
	 */
	private final static int DEFAULT_TOPIC_ID = -1;
	/**
	 * Default value of 'storyID', meaning the story is just a tmp story.
	 */
	//	private final static int DEFAULT_STORY_ID = -1;

	/**
	 * It seems to be of no use?
	 */
	//	private int storyID = DEFAULT_STORY_ID;
	/**
	 * the index of each plain word, refer to the glossary
	 */
	private Vector<Integer> words;
	/**
	 * yyyymmdd.hhmm.XXXX
	 */
	private String timeStamp;
	/**
	 * Indicate the topicID of this story.
	 */
	private int topicID = DEFAULT_TOPIC_ID;
	/**
	 * Before using, make sure setWordsCount is invoked.
	 */
	private HashMap<Integer, Integer> wordsCount;
	/**
	 * Before using, make sure setTermFrequency() is invoked.
	 */
	private HashMap<Integer, Double> termFrequency;
	/**
	 * <word id, tfidf>. Before using, make sure setTFIDFBasedOnCorpus() is
	 * invoked
	 */
	private HashMap<Integer, Double> tfidf;

	// ---------- CONSTRUCTORS ------------------------------------------------
	/**
	 * UNSUGGESTED: Default constructor, used only for temporary story.
	 */
	public Story() {
		this.words = new Vector<Integer>();
		this.timeStamp = null;
	}

	/**
	 * @param timestamp
	 */
	public Story(String timestamp) {
		this.words = new Vector<Integer>();
		this.timeStamp = timestamp;
	}

	/**
	 * @param words
	 * @param timeStamp
	 */
	public Story(Vector<Integer> words, String timeStamp) {
		//		this.storyID = storyID;
		this.words = words;
		this.timeStamp = timeStamp;
	}

	// ---------- GETTERS & SETTERS--------------------------------------------
	/**
	 * @return the storyID
	 */
	//	public int getStoryID() {
	//		return storyID;
	//	}

	/**
	 * @param storyID
	 *            the storyID to set
	 */
	//	public void setStoryID(int storyID) {
	//		this.storyID = storyID;
	//	}

	/**
	 * @return the words
	 */
	public Vector<Integer> getWords() {
		return words;
	}

	/**
	 * @param words
	 *            the words to set
	 */
	//	public void setWords(Vector<Integer> words) {
	//		this.words = words;
	//	}

	/**
	 * @param index
	 * @return a word in 'words'
	 */
	public int getWord(int index) {
		return this.words.get(index);
	}

	/**
	 * Set a word in words
	 * 
	 * @param index
	 * @param value
	 */
	//	public void setWord(int index, int value) {
	//		this.words.set(index, value);
	//	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the topicID
	 */
	public int getTopicID() {
		return topicID;
	}

	/**
	 * @param topicID
	 *            the topicID to set
	 */
	public void setTopicID(int topicID) {
		this.topicID = topicID;
	}

	/**
	 * Before using this function, make sure the initWordsCount() was called.
	 * 
	 * @return the wordsCount
	 */
	public HashMap<Integer, Integer> getWordsCount() {
		return wordsCount;
	}

	/**
	 * USE initWordsCount() INSTEAD. Set the wordsCount directly.
	 * 
	 * @param wordsCount
	 *            the wordsCount to set
	 */
	public void setWordsCount(HashMap<Integer, Integer> wordsCount) {
		this.wordsCount = wordsCount;
	}

	/**
	 * @return the termFrequency
	 */
	public HashMap<Integer, Double> getTermFrequency() {
		return termFrequency;
	}

	/**
	 * WARNING: USE initTermFrequency() INSTEAD.
	 * 
	 * @param termFrequency
	 *            the termFrequency to set
	 */
	public void setTermFrequency(HashMap<Integer, Double> termFrequency) {
		this.termFrequency = termFrequency;
	}

	/**
	 * @return the tfidf
	 */
	public HashMap<Integer, Double> getTfidf() {
		return tfidf;
	}

	/**
	 * @param tfidf
	 *            the tfidf to set
	 */
	public void setTfidf(HashMap<Integer, Double> tfidf) {
		this.tfidf = tfidf;
	}

	// ---------- BOOLEAN -----------------------------------------------------
	/**
	 * @param wordID
	 * @return true if this story contains a certain word.
	 */
	public boolean containsWordID(int wordID) {
		if (!this.wordsCount.isEmpty()) {
			return this.wordsCount.containsKey(wordID);
		} else {
			// Option 1: first build up the wordsCount, then use wordsCount.find()
			// Option 2: sort the words, then find (using the binary search)
			for (int curWordID : this.words) {
				if (curWordID == wordID)
					return true;
			}
			return false;
		}
	}

	/**
	 * @return true if the story is already clustered.
	 */
	public boolean isClustered() {
		return this.topicID != Story.DEFAULT_TOPIC_ID;
	}

	// ---------- INITIALIZATION ----------------------------------------------
	/**
	 * Initialize the wordsCount
	 */
	public void initWordsCount() {
		this.wordsCount = new HashMap<Integer, Integer>();
		for (int curWordID : this.words) {
			if (this.wordsCount.containsKey(curWordID)) {
				this.wordsCount.put(curWordID,
					this.wordsCount.get(curWordID) + 1);
			} else {
				this.wordsCount.put(curWordID, 1);
			}
		}

	}

	/**
	 * Initialize the termFrequency
	 */
	public void initTermFrequency() {
		if (this.wordsCount == null || this.wordsCount.isEmpty())
			this.initWordsCount();

		this.termFrequency = new HashMap<Integer, Double>();

		double length = this.words.size();
		for (Entry<Integer, Integer> pair : this.wordsCount.entrySet()) {
			this.termFrequency.put(pair.getKey(), pair.getValue() / length);
		}
	}

	/**
	 * Initialize the tfidf.
	 */
	public void initTfidf() {
		this.tfidf = new HashMap<Integer, Double>();
	}

	// ---------- OTHERS ------------------------------------------------------
	/**
	 * Add a word to the words
	 */
	public void addWord(int wordID) {
		this.words.add(wordID);
	}

	/**
	 * calculate tfidf of this story, based on the corpus.
	 * 
	 * @param corpus
	 * @param wordIDToStoryIndices
	 */
	public void setTfidfBasedOnCorpus(Vector<Story> corpus,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices) {
		if (this.termFrequency == null || this.termFrequency.isEmpty())
			this.initTermFrequency();

		this.tfidf = new HashMap<Integer, Double>();

		for (Entry<Integer, Double> pair : this.termFrequency.entrySet()) {
			int wordID = pair.getKey();
			double tf = pair.getValue();
			if (wordIDToStoryIndices.containsKey(wordID)) {
				double idf = 0.0;
				double storiesWithWord = 0.0;
				storiesWithWord = wordIDToStoryIndices.get(wordID).size();
				idf = Math.log(corpus.size() / storiesWithWord);
				this.tfidf.put(wordID, tf * idf);
			}
		}
	}

	/**
	 * return time stamp and words
	 */
	public String toString(Glossary glossary) {
		String result = "";
		result += this.timeStamp;
		for (int wordID : this.words) {
			if (glossary.containsWordID(wordID)) {
				result += " " + glossary.getWord(wordID);
			}
		}
		return result;
	}
	
	
}

/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.HashMap;

/**
 * @author Zitong Wang
 */
public class Glossary {

	public Glossary() {

	}

	public int size() {
		return glossaryStringToInt.size();
	}

	public boolean containsWordID(int wordID) {
		return glossaryIntToString.containsKey(wordID);
	}

	public String getWord(int wordID) {
		return glossaryIntToString.get(wordID);
	}

	public Integer getWordID(String word) {
		return glossaryStringToInt.get(word);
	}

	public void insertWord(String word) {
		if (!glossaryStringToInt.containsKey(word)) {
			int index = size();
			glossaryIntToString.put(index, word);
			glossaryStringToInt.put(word, index);
		}
	}

	private HashMap<Integer, String> glossaryIntToString =
		new HashMap<Integer, String>();

	private HashMap<String, Integer> glossaryStringToInt =
		new HashMap<String, Integer>();

}

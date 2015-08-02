/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

/**
 * @author Zitong Wang
 */
class DataPreprocessor {

	final int MAX_FILES = 999999;

	int numOfStories = 0;

	/**
	 * Preprocess the data
	 */
	DataPreprocessor() {
	}

	/**
	 * Read from sgm files, set the 'corpus' and 'glossary', and do some other
	 * preprocessing.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param actualFirstStories
	 * @param sgmDir
	 * @param ansFile
	 */
	public void doDataPreprocessing(Vector<Story> corpus, Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices,
		Vector<Story> actualFirstStories, String sgmDir, String ansFile) {
		readCorpus(corpus, glossary, wordIDToStoryIndices, sgmDir);
		readAnswer(actualFirstStories, ansFile);
	}

	/**
	 * Read from files, set the 'corpus' and 'glossary', and do some other
	 * preprocessing.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param tknDir
	 * @param bndDir
	 */
	public void doDataPreprocessing(Vector<Story> corpus, Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices,
		Vector<Story> actualFirstStories, String tknDir, String bndDir,
		String ansFile) {
		readCorpus(corpus, glossary, wordIDToStoryIndices, tknDir, bndDir);
		readAnswer(actualFirstStories, ansFile);
	}

	/**
	 * TODO: readCorpus(...) using sgm files, set the 'corpus' and 'glossary'.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param sgmDir
	 */
	public void readCorpus(Vector<Story> corpus, Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String sgmDir) {

	}

	/**
	 * Read from files, set the 'corpus' and 'glossary'.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param tknDir
	 * @param bndDir
	 */
	public void readCorpus(Vector<Story> corpus, Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String tknDir,
		String bndDir) {
		System.out.println("Please choose");
		System.out.println("1. Read from the specific file");
		System.out.println("2. Read from files in the directory");

		Scanner in = new Scanner(System.in);
		String choice = in.nextLine();

		while (true) {
			if (choice.equals("1")) {
				readCorpusFromFile(corpus, glossary, wordIDToStoryIndices,
					tknDir, bndDir);
				break;
			} else if (choice.equals("2")) {
				readCorpusFromDirectory(corpus, glossary, wordIDToStoryIndices,
					tknDir, bndDir);
				break;
			} else {
				System.out.println("Invalid input, please input again!");
				choice = in.nextLine();
			}
		}

		in.close();
	}

	/**
	 * Read from the specific file, set the 'corpus' and 'glossary'.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param tknDir
	 * @param bndDir
	 */
	public void readCorpusFromFile(Vector<Story> corpus, Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String tknDir,
		String bndDir) {
		LOOP: while (true) {
			// the id of the first and the last words of a story
			Vector<Integer> Brecid = new Vector<Integer>();
			Vector<Integer> Erecid = new Vector<Integer>();

			String bndFile, tknFile;
			Scanner in = new Scanner(System.in);

			System.out.println("Please input the file name of bnd file");
			bndFile = in.nextLine();
			bndFile = bndDir + bndFile;

			System.out.println("Please input the file name of tkn file");
			tknFile = in.nextLine();
			tknFile = tknDir + tknFile;

			readBndFile(corpus, bndFile, Brecid, Erecid);

			readTknFile(corpus, tknFile, Brecid, Erecid, glossary,
				wordIDToStoryIndices);

			System.out.println("Continue?(Y/N)");
			String choice = in.nextLine();

			while (true) {
				if (choice.equals("Y") || choice.equals("y")) {
					in.close();
					continue LOOP;
				} else if (choice.equals("N") || choice.equals("n")) {
					in.close();
					break LOOP;
				} else {
					System.out.println("Invalid input, please input again!");
					choice = in.nextLine();
				}
			}
		}
	}

	/**
	 * Read from files in the directory, set the 'corpus' and 'glossary'.
	 * 
	 * @param corpus
	 * @param glossary
	 * @param wordIDToStoryIndices
	 * @param tknDir
	 * @param bndDir
	 */
	public void readCorpusFromDirectory(Vector<Story> corpus,
		Glossary glossary,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices, String tknDir,
		String bndDir) {
		File directoryBnd = new File(bndDir);
		File[] fileBnd = directoryBnd.listFiles();
		Scanner in = new Scanner(System.in);

		// the id of the first and the last words of a story
		Vector<Integer> Brecid = new Vector<Integer>();
		Vector<Integer> Erecid = new Vector<Integer>();

		int numOfFileToBeRead = 0;
		int numOfFilesRead = 0;

		System.out
			.println("Input the number of files want to be read (0 represents all)");
		numOfFileToBeRead = in.nextInt();
		in.close();

		if (numOfFileToBeRead == 0) {
			numOfFileToBeRead = MAX_FILES;
		}

		if (fileBnd.length == 0) {
			System.out.println("No bnd file found!");
		} else {
			for (int i = 0; i < fileBnd.length
				&& numOfFilesRead < numOfFileToBeRead; i++) {
				System.out.println(fileBnd[i].getName() + "\tfound!");

				readBndFile(corpus, fileBnd[i].getAbsolutePath(), Brecid,
					Erecid);

				numOfFilesRead++;
			}
		}

		numOfFilesRead = 0;
		File directoryTkn = new File(tknDir);
		File[] fileTkn = directoryTkn.listFiles();

		if (fileTkn.length == 0) {
			System.out.println("No bnd file found!");
		} else {
			for (int i = 0; i < fileTkn.length
				&& numOfFilesRead < numOfFileToBeRead; i++) {
				System.out.println(fileTkn[i].getName() + "\tfound!");

				readTknFile(corpus, fileTkn[i].getAbsolutePath(), Brecid,
					Erecid, glossary, wordIDToStoryIndices);

				numOfFilesRead++;
			}
		}
	}

	/**
	 * Read from bnd files to get the begin and the end of a story.
	 * 
	 * @param corpus
	 * @param bndFile
	 * @param Brrecid
	 * @param Erecid
	 */
	public void readBndFile(Vector<Story> corpus, String bndFile,
		Vector<Integer> Brecid, Vector<Integer> Erecid) {

		File file = new File(bndFile);
		assert (file.exists());

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// the first line is title, and it is of no use
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for each line, if split by space, we can get 5 strings:
		// 1. "<BOUNDARY", useless
		// 2. "docno=CNA + timestamp", the timestamp should be retrieved
		// 3. "doctype=NEWS", useless maybe
		// 4. "Brecid=?", very important
		// 5. "Erecid=?>", very important
		String newLine;
		try {
			while ((newLine = reader.readLine()) != null) {
				String timestamp;

				// because they are not only Brecid and Erecid, so they are
				// called
				// as follows
				String BrecidWithRedundancy, ErecidWithRedundancy;

				// the follows are real Brecid and Erecid
				int BrecidInt, ErecidInt;

				String temp[] = newLine.split(" ");
				assert (temp.length == 5);

				timestamp = temp[1];
				BrecidWithRedundancy = temp[3];
				ErecidWithRedundancy = temp[4];

				// retrieve the timestamp
				timestamp = timestamp.substring(9, timestamp.length() - 9);

				BrecidInt =
					Integer.parseInt(BrecidWithRedundancy.split("=")[1]);

				String ErecidTemp = ErecidWithRedundancy.split("=")[1];
				ErecidInt =
					Integer.parseInt(ErecidTemp.substring(0,
						ErecidTemp.length() - 1));

				Story newStroy = new Story(timestamp);
				corpus.addElement(newStroy);

				Brecid.add(BrecidInt);
				Erecid.add(ErecidInt);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("read bnd file done!");
	}

	/**
	 * Read from tkn files, get the words for each story and set the glossary.
	 * 
	 * @param corpus
	 * @param tknFile
	 * @param Brrecid
	 * @param Erecid
	 * @param glossay
	 * @param wordIDToStoryIndices
	 */
	public void readTknFile(Vector<Story> corpus, String tknFile,
		Vector<Integer> Brecid, Vector<Integer> Erecid, Glossary glossay,
		HashMap<Integer, HashSet<Integer>> wordIDToStoryIndices) {
		File file = new File(tknFile);
		assert (file.exists());

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// the first line is title, and it is of no use
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for each line, if simply use fin, we can get 4 strings:
		// 1. "<W", useless
		// 2. "recid=?", since the recid increases by one, so we can just count
		// to know the value of "?"
		// 3. "tr=Y", it is of no use currently
		// 4. word, very important
		int recid = 1;

		boolean beginOfAStroy = true;

		String newLine;
		try {
			while ((newLine = reader.readLine()) != null) {
				String word;

				// this means a new tkn file is read
				if (recid > Erecid.get(numOfStories)) {
					numOfStories++;
					beginOfAStroy = true;
				}

				if (Brecid.get(numOfStories) == 1 && beginOfAStroy) {
					recid = 1;
					beginOfAStroy = false;
				}

				String temp[] = newLine.split(" ");
				assert (temp.length == 4);

				word = temp[3];
				word = processWord(word);

				glossay.insertWord(word);

				int wordID = glossay.getWordID(word);
				corpus.get(numOfStories).addWord(wordID);

				try {
					wordIDToStoryIndices.get(wordID).add(numOfStories);
				} catch (NullPointerException e) {
					wordIDToStoryIndices.put(wordID, new HashSet<Integer>());
					wordIDToStoryIndices.get(wordID).add(numOfStories);
				}

				recid++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		numOfStories++;

		System.out.println("read tkn file done!");
	}

	/**
	 * Process the word, remove punctuations and convert all the letters to
	 * lowercase
	 * 
	 * @param word
	 */
	public static String processWord(String word) {
		word = word.toLowerCase();
		word = word.replaceAll("[^a-z0-9._]", "");

		return word;
	}

	/**
	 * TODO: readAnswer(...)
	 * 
	 * @param actualFirstStories
	 * @param ansFile
	 */
	private void readAnswer(Vector<Story> actualFirstStories, String ansFile) {

	}
}

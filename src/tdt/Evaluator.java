/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.Arrays;
import java.util.Vector;

/**
 * This class is used to evaluate the performance of the first story detection.
 * 
 * @author Peng Kang, Zewei Wu.
 */
public class Evaluator {

	/**
	 * @param corpus
	 * @param actualFirstStories
	 * @param firstStories
	 * @return normCdet, 0~1
	 */
	protected static double doEvaluation(Vector<Story> corpus,
		Vector<Story> actualFirstStories, Vector<Story> firstStories) {
		return doEvaluation_v2(corpus, actualFirstStories, firstStories);
	}

	/**
	 * @author Zewei Wu
	 * @param corpus
	 * @param actualFirstStories
	 * @param firstStories
	 * @return
	 */
	private static double doEvaluation_v2(Vector<Story> corpus,
		Vector<Story> actualFirstStories, Vector<Story> firstStories) {

		int numOfHits = 0;
		for (int i = 0; i < actualFirstStories.size(); ++i) {
			for (int j = 0; j < firstStories.size(); ++j) {
				String str1 = actualFirstStories.get(i).getTimeStamp();
				String str2 = firstStories.get(j).getTimeStamp();
				if (str1.compareTo(str2) == 0) {
					++numOfHits;
					break;
				}
			}
		}

		double PMiss =
			(actualFirstStories.size() - numOfHits)
				/ Double.valueOf(actualFirstStories.size());
		double CMiss = 1.0;
		double Ptarget = 0.02;

		double PFa =
			(firstStories.size() - numOfHits)
				/ Double.valueOf(firstStories.size());
		double CFa = 0.1;
		double Pnon_target = 1 - Ptarget;

		double Cdet = PMiss * (CMiss * Ptarget) + PFa * (CFa * Pnon_target);
		double norm_mini =
			(CMiss * Ptarget < CFa * Pnon_target) ? (CMiss * Ptarget)
				: (CFa * Pnon_target);
		double normCdet = Cdet / norm_mini;

		return normCdet;
	}

	/**
	 * @author Peng Kang
	 * @param corpus
	 * @param actualFirstStories
	 * @param firstStories
	 * @return
	 */
	@SuppressWarnings("unused")
	private static double doEvaluation_v1(Vector<Story> corpus,
		Vector<Story> actualFirstStories, Vector<Story> firstStories) {

		// Find out the newest and oldest news in the corpus
		// Eg: Timestamp - 20030408, with eight digits in the leftmost positions
		// yyyymmdd.hhmm.XXXX 
		String timeMin = "99999999";
		String timeMax = "00000000";
		for (Story tmp : corpus) {
			String str = tmp.getTimeStamp().substring(0, 8);
			if (str.compareTo(timeMax) > 0)
				timeMax = str;
			if (str.compareTo(timeMin) < 0)
				timeMin = str;
		}

		// sort the actualFirstStories by timestamp
		String[] answer = new String[250];
		for (int j = 0; j < actualFirstStories.size(); j++) {
			answer[j] =
				actualFirstStories.get(j).getTimeStamp().substring(0, 8);
		}
		Arrays.sort(answer);

		// the size of actualFirstStories is 250
		double count1 = 0, count2 = 0;
		for (int j = 0; j < answer.length; j++) {
			if (answer[j].compareTo(timeMin) <= 0)
				++count1;
			if (answer[j].compareTo(timeMax) <= 0)
				++count2;
		}

		// the number of new topics in the testing datasets
		double newTopics = count2 - count1 + 1;

		// the number of old topics in the testing datasets
		double oldTopics = corpus.size() - newTopics;

		// the number of new topics which are not detected
		// Maybe too strict
		double detectedNewTopics = 0;
		for (int i = 0; i < firstStories.size(); i++) {
			String str2 = firstStories.get(i).getTimeStamp().substring(0, 8);
			for (int j = 0; j < actualFirstStories.size(); j++) {
				String str4 =
					actualFirstStories.get(j).getTimeStamp().substring(0, 8);
				if (str2.compareTo(str4) == 0)
					detectedNewTopics++;
			}
		}

		// Calculate Cdet & Norm_Cdet
		double undetectedNewTopics = newTopics - detectedNewTopics;
		double PMiss = undetectedNewTopics / newTopics;
		double CMiss = 1.0;
		double Ptarget = 0.02;

		// the number of some old topics we mistake for new topics
		double mistakenTopics = firstStories.size() - detectedNewTopics;
		double PFa = mistakenTopics / oldTopics;
		double CFa = 0.1;
		double Pnon_target = 1 - Ptarget;

		double norm_mini =
			(CMiss * Ptarget < CFa * Pnon_target) ? (CMiss * Ptarget)
				: (CFa * Pnon_target);
		double Cdet = PMiss * (CMiss * Ptarget) + PFa * (CFa * Pnon_target);
		double normCdet = Cdet / norm_mini;

		return normCdet;
	}
}

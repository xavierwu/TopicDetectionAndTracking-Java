/**
 * Created on: Jul 29, 2015
 */
package tdt;

import java.util.Arrays;
import java.util.Vector;

/**
 * class Evaluator: Peng
 * 
 * @author Peng Kang
 */
public class Evaluator {

	/**
	 * @param corpus
	 * @param actualFirstStories
	 * @param firstStories
	 * @return normCdet, 0~1
	 */
	public static double doEvaluation(Vector<Story> corpus,
		Vector<Story> actualFirstStories, Vector<Story> firstStories) {
		// Variables
		double normCdet = 0;
		double Cdet = 0;
		double PMiss = 0, PFa = 0;
		double CMiss = 1, CFa = 0.1, Ptarget = 0.02;
		double Pnon_target = 1 - Ptarget;
		double norm_mini =
			(CMiss * Ptarget < CFa * Pnon_target) ? CMiss * Ptarget : CFa
				* Pnon_target;

		int[] answer = new int[300];
		double count1 = 0, count2 = 0;
		double newTopics = 0;
		double oldTopics = 0;
		double dectectedNewTopics = 0, undectectedNewTopics = 0;
		double mistakenTopics = 0;

		// Eg: Timestamp - 20030408, with eight digits in the leftmost positions

		int timeMin = 0, timeMax = 0;

		// find the newest and latest news in the testing datasets
		for (int i = 0; i < corpus.size(); i++) {
			String str1 = corpus.get(i).getTimeStamp();
			String str2 = str1.substring(0, 8);
			int cmp1 = Integer.parseInt(str2);
			timeMax = timeMax > cmp1 ? timeMax : cmp1;
			timeMin = timeMin < cmp1 ? timeMin : cmp1;
		}

		// sort the actualFirstStories by timeline
		for (int j = 0; j < actualFirstStories.size(); j++) {
			String str3 = actualFirstStories.get(j).getTimeStamp();
			String str4 = str3.substring(0, 8);
			int cmp2 = Integer.parseInt(str4);
			answer[j] = cmp2;
		}
		Arrays.sort(answer);

		// the size of actualFirstStories is 250
		for (int j = 0; j < actualFirstStories.size(); j++) {
			if (timeMin >= answer[j])
				count1++;
			if (timeMax >= answer[j])
				count2++;
		}

		// the number of new topics in the testing datasets
		newTopics = count2 - count1 + 1;

		// the number of old topics in the testing datasets
		oldTopics = corpus.size() - newTopics;

		// the number of new topics which are not detected
		// Maybe too strict
		for (int i = 0; i < firstStories.size(); i++) {
			String str1 = firstStories.get(i).getTimeStamp();
			String str2 = str1.substring(0, 8);
			int cmp1 = Integer.parseInt(str2);
			for (int j = 0; j < actualFirstStories.size(); j++) {
				String str3 = actualFirstStories.get(j).getTimeStamp();
				String str4 = str3.substring(0, 8);
				int cmp2 = Integer.parseInt(str4);
				if (cmp1 == cmp2)
					dectectedNewTopics++;
			}
		}

		undectectedNewTopics = newTopics - dectectedNewTopics;

		assert (undectectedNewTopics >= 0);

		// the number of some old topics we mistake for new topics
		mistakenTopics = firstStories.size() - dectectedNewTopics;

		// Calculate Cdet & Norm_Cdet
		PMiss = undectectedNewTopics / newTopics;
		PFa = mistakenTopics / oldTopics;

		Cdet = CMiss * PMiss * Ptarget + CFa * PFa * Pnon_target;
		normCdet = Cdet / norm_mini;

		return normCdet;
	}
}

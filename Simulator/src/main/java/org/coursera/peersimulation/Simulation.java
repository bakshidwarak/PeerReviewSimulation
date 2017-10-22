package org.coursera.peersimulation;

import java.util.Scanner;

public class Simulation {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		int duration= scanner.nextInt();
		SimulationManager simulationManager= new SimulationManager(duration);
		int numLearners=scanner.nextInt();
		int count=0;
		while(count<numLearners){
			String learnerString=scanner.nextLine();
			Scanner lineScanner= new Scanner(learnerString);
			int learnerId=lineScanner.nextInt();
			int firstSubmission=lineScanner.nextInt();
			int firstSubmissionTrueScore=lineScanner.nextInt();;
			int reviewBias=lineScanner.nextInt();
			
			Learner learner=(new Learner()).setFirstSubmissionStartTick(firstSubmission).setFirstSubmissionTrueScore(firstSubmissionTrueScore).setReviewBias(reviewBias).setLearnerId(learnerId);
			simulationManager.registerLearner(learner);
		}
		
		simulationManager.simulatePeerReviews();

	}

}

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

			int learnerId=scanner.nextInt();
			int firstSubmission=scanner.nextInt();
			int firstSubmissionTrueScore=scanner.nextInt();;
			int reviewBias=scanner.nextInt();
			count++;
			Learner learner=new Learner();
			learner.setFirstSubmissionStartTick(firstSubmission).setFirstSubmissionTrueScore(firstSubmissionTrueScore).setReviewBias(reviewBias).setLearnerId(learnerId);
			simulationManager.registerLearner(learner);
		}
		
		simulationManager.simulatePeerReviews();

	}

}

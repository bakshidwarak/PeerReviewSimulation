package org.coursera.peersimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Learner {
    private static final int maxReviews = 3;
    private static final int MAX_SUBMISSIONS = 0;
    private int learnerId;
    private int firstSubmissionStartTick;
    private int firstSubmissionTrueScore;
    private int reviewBias;
    private State learnerState;
    private List<Integer> submissionTicks=new ArrayList<>();
    private int numOfSubmissions;
    private int numOfReviews;
    private int currentScore;
    private Submission submission;
    private int nextActTime;
    private int currentReviewScore;

    private Submission reviewingSubmission;
	private int totalscore;
	private int lastReviewTick=-1;

    public int getLearnerId() {
        return learnerId;
    }
    
    public Learner(){
    	
    }

    public void actAtPulse(SimulationManager manager, int tick) {

        if (tick == nextActTime) {
            if (learnerState == State.NOT_STARTED) {
                startWork();
                
                nextActTime = tick + 50;
            }
            if (learnerState == State.WORKING) {
                manager.submit(submission);
                nextActTime = tick + 1;
            }
            if (learnerState == State.WAITING_REVIEW) {
            	if(manager.isOutComeAvailable(this.submission)){
            		 if ( manager.isFailed(submission)) {
                         if (numOfSubmissions == MAX_SUBMISSIONS) {
                             learnerState = State.FAILED;
                         }
                         else {
                             int score = this.currentScore += 15;
                             currentScore = Math.min(score, 100);
                             startWork();
                             nextActTime = tick + 50;
                         }
                     }
            		 else
            		 {
            			 this.totalscore=manager.getTotalScore(submission);
            			 this.learnerState=State.GRADED;
            			 this.lastReviewTick=manager.getLastReviewTick(submission);
            		 }
            		
            	}
               
                
                else if (numOfReviews < maxReviews) {

                    Optional<Submission> submissionToReview = manager.getNextSubmissionToReview(this.getLearnerId());
                    submissionToReview.ifPresent(submission->startReviewing(submission));
                    nextActTime = tick + 20;
                }
            }
            if (learnerState == State.REVIEWING) {
                manager.turnInReview(reviewingSubmission, currentReviewScore,this.getLearnerId(),tick);
                nextActTime = tick + 1;
            }
        }

    }

    public int getNumOfReviews() {
        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }

    public Learner setLearnerId(int learnerId) {
        this.learnerId = learnerId;
        return this;
    }

    public int getFirstSubmissionStartTick() {
        return firstSubmissionStartTick;
    }

    public Learner setFirstSubmissionStartTick(int firstSubmissionStartTick) {
        this.firstSubmissionStartTick = firstSubmissionStartTick;
        this.nextActTime=firstSubmissionStartTick;
        return this;
    }

    public int getFirstSubmissionTrueScore() {
        return firstSubmissionTrueScore;
    }

    public Learner setFirstSubmissionTrueScore(int firstSubmissionTrueScore) {
        this.firstSubmissionTrueScore = firstSubmissionTrueScore;
        return this;
    }

    public int getReviewBias() {
        return reviewBias;
    }

    public Learner setReviewBias(int reviewBias) {
        this.reviewBias = reviewBias;
        return this;
    }

    public State getLearnerState() {
        return learnerState;
    }

    public Learner setLearnerState(State learnerState) {
        this.learnerState = learnerState;
        return this;
    }

    public List<Integer> getSubmissionTicks() {
        return submissionTicks;
    }

    public void setSubmissionTicks(List<Integer> submissionTicks) {
        this.submissionTicks = submissionTicks;
    }

    public int getNumOfSubmissions() {
        return numOfSubmissions;
    }

    public void setNumOfSubmissions(int numOfSubmissions) {
        this.numOfSubmissions = numOfSubmissions;
    }

    public void startWork() {
        this.learnerState = State.WORKING;

    }

    public Submission submit(int tick) {
        if (submission == null) {
            submission = new Submission();
        }
        this.learnerState = State.WAITING_REVIEW;
        this.submissionTicks.add(tick);
       
        numOfSubmissions++;
        return submission;

    }

    public void startReviewing(Submission submissionToReview) {
        this.learnerState = State.REVIEWING;
        this.reviewingSubmission = submissionToReview;
        numOfReviews++;
        int learnerScore = submissionToReview.getLearner().getCurrentScore();
        learnerScore += this.reviewBias;
        currentReviewScore = Math.min(100, learnerScore);

    }

    private int getCurrentScore() {
        // TODO Auto-generated method stub
        return currentScore;
    }

	@Override
	public String toString() {
		Integer submissionTi = submissionTicks.size()>0?submissionTicks.get(submissionTicks.size()-1):0;
		return learnerId + " " + submissionTi + " "
				+ numOfReviews + " " + totalscore + " " + lastReviewTick ;
	}
    
    

}

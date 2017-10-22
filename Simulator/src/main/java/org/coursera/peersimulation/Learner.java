package org.coursera.peersimulation;

import java.util.List;

public class Learner {
	private int learnerId;
	private int firstSubmissionStartTick;
	private int firstSubmissionTrueScore;
	private int reviewBias;
	private State learnerState;
	private List<Integer> submissionTicks;
	private int numOfSubmissions;
	private int numOfReviews;
	private int currentScore;
	public int getLearnerId() {
		return learnerId;
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
		this.learnerState=State.WORKING;
		
	}
	public void submit(int tick) {
		this.learnerState=State.WAITING_REVIEW;
		this.submissionTicks.add(tick);
		numOfSubmissions++;
		
	}
	public void startReviewing(Learner learner, int i) {
		this.learnerState=State.REVIEWING;
		numOfReviews++;
		int learnerScore=learner.getCurrentScore();
		learnerScore+=this.reviewBias;
		
		
	}
	private int getCurrentScore() {
		// TODO Auto-generated method stub
		return currentScore;
	}
	public void endReview(int i) {
		// TODO Auto-generated method stub
		
	}
	
	

}

package org.coursera.peersimulation;

import java.util.HashSet;
import java.util.Set;

public class Submission {
private Learner learner;
private int submissionTick;
private Set<Learner> reviewers= new HashSet<>(3);
boolean maxReviewersReached;
public Learner getLearner() {
	return learner;
}
public void setLearner(Learner learner) {
	this.learner = learner;
}
public int getSubmissionTick() {
	return submissionTick;
}
public void setSubmissionTick(int submissionTick) {
	this.submissionTick = submissionTick;
}
public Set<Learner> getReviewers() {
	return reviewers;
}
public void setReviewers(Set<Learner> reviewers) {
	this.reviewers = reviewers;
}

public int getLearnerId(){
	return learner.getLearnerId();
}
public boolean isMaxReviewersReached() {
    return reviewers.size()>=3;
}
public void setMaxReviewersReached(boolean maxReviewersReached) {
    this.maxReviewersReached = maxReviewersReached;
}

}

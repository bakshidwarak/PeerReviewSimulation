package org.coursera.peersimulation;

import java.util.Set;

public class Submission {
private Learner learner;
private int submissionTick;
private Set<Learner> reviewers;
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

}

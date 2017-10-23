package org.coursera.peersimulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;


public class SimulationManager {
    int simulationDuration;

    List<Learner>[] learnerTimeline;

    List<Learner> learners = new ArrayList<>();
    Queue<Learner> learnersReadyToReview = new LinkedList<>();

    @SuppressWarnings("unchecked")
    public SimulationManager(int duration) {
        this.simulationDuration = duration;
        learnerTimeline = new List[duration + 1];
    }

    PriorityQueue<Submission> submissionQueue = new PriorityQueue<>(
            Comparator.comparing(Submission::getSubmissionTick).thenComparing(Submission::getLearnerId));

    public void registerLearner(Learner learner) {
        learners.add(learner);
        addLearnerToTimeLine(learner, learner.getFirstSubmissionStartTick());

    }

    public void simulatePeerReviews() {
        for (int i = 0; i < learnerTimeline.length; i++) {
            List<Learner> currentActive = learnerTimeline[i];
            List<Learner> submittingUsers = currentActive.stream()
                    .filter(l -> (l.getLearnerState() == State.NOT_STARTED || l.getLearnerState() == State.FAILED))
                    .collect(Collectors.toList());
            for (Learner learner : submittingUsers) {
                learner.startWork();
                addLearnerToTimeLine(learner, i + 50);
            }

            List<Learner> workingUsers = currentActive.stream().filter(l -> l.getLearnerState() == State.WORKING)
                    .collect(Collectors.toList());
            for (Learner learner : workingUsers) {
                Submission submission =learner.submit(i);
                learnersReadyToReview.add(learner);
                
                submissionQueue.add(submission);
            }

            List<Learner> reviewSubmitters = currentActive.stream().filter(l -> l.getLearnerState() == State.REVIEWING)
                    .collect(Collectors.toList());
            for (Learner learner : reviewSubmitters) {
                learner.endReview(i);

            }
            Learner reviewer = null;
            while (!submissionQueue.isEmpty()) {
                reviewer = learnersReadyToReview.remove();
                Submission current = submissionQueue.remove();
                if (!current.getLearner().equals(reviewer) && !current.getReviewers().contains(reviewer)) {

                    current.getReviewers().add(reviewer);
                    reviewer.startReviewing(current, i);
                    addLearnerToTimeLine(reviewer, i + 20);
                    if (!current.maxReviewersReached) {
                        submissionQueue.add(current);
                    }
                }
                else {
                    if (!submissionQueue.isEmpty()) {
                        Submission next = submissionQueue.remove();
                        reviewer.startReviewing(next.getLearner(), i);

                    }
                    submissionQueue.add(current);

                }
            }

        }

    }

    private void addLearnerToTimeLine(Learner learner, int time) {
        if (time > simulationDuration)
            return;
        List<Learner> learnersAtTime = learnerTimeline[time];
        if (learnersAtTime == null) {
            learnersAtTime = new ArrayList<>();

        }
        learnersAtTime.add(learner);
        learnerTimeline[time] = learnersAtTime;

    }

    public void submit(Submission submission) {
        // TODO Auto-generated method stub
        
    }

    public Submission getNextSubmissionToReview(int learnerId) {
        // TODO Auto-generated method stub
        return null;
    }

    public void turnInReview(Submission reviewingSubmission, int currentReviewScore) {
        // TODO Auto-generated method stub
        
    }

    public boolean isOutComeAvailable(Submission submission) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isFailed(Submission submission) {
        // TODO Auto-generated method stub
        return false;
    }

}

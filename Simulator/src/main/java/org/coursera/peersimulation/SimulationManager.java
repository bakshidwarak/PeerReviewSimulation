package org.coursera.peersimulation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class SimulationManager {

	class ReviewerScore {
		int learnerId;
		int score;
		int tick;
		boolean isComplete;

		public ReviewerScore(int learnerId, int score) {
			super();
			this.learnerId = learnerId;
			this.score = score;
		}

	}

	private static final int MAX_REVIEWER_COUNT = 3;

	int simulationDuration;

	private HashMap<Integer, Set<Integer>> reviewerMap = new HashMap<>();
	BiPredicate<Submission, Integer> submitterSameAsReviewer = (current,
			id) -> current.getLearner().getLearnerId() == id;
	BiPredicate<Submission, Integer> hasNotPreviouslyReviewed = (current,
			id) -> !reviewerMap.get(current.getLearnerId()).contains(id);

	List<Learner> learners = new ArrayList<>();
	private HashMap<Integer, Set<ReviewerScore>> currentActiveSubmissionReviewers = new HashMap<>();

	Predicate<Submission> maxReviewersReached = (submission) -> (currentActiveSubmissionReviewers
			.get(submission.getLearnerId()).size() == MAX_REVIEWER_COUNT);

	public SimulationManager(int duration) {
		this.simulationDuration = duration;

	}

	PriorityQueue<Submission> submissionQueue = new PriorityQueue<>(
			Comparator.comparing(Submission::getSubmissionTick).thenComparing(Submission::getLearnerId));

	public void registerLearner(Learner learner) {
		learners.add(learner);

	}

	public void simulatePeerReviews() {
		for (int i = 1; i <= simulationDuration; i++) {
			final int tick = i;
			System.out.println(i);
			learners.forEach(l -> l.actAtPulse(this, tick));
		}
		learners.forEach(System.out::println);
	}

	public void submit(Submission submission) {
		this.reviewerMap.putIfAbsent(submission.getLearnerId(), new HashSet<>());
		currentActiveSubmissionReviewers.put(submission.getLearnerId(), new HashSet<>());

	}

	public Optional<Submission> getNextSubmissionToReview(int learnerId) {

		Submission current = null;
		List<Submission> submissionsToReque= new ArrayList<>();

		while (!submissionQueue.isEmpty()) {

			current = submissionQueue.remove();
			if (submitterSameAsReviewer.and(hasNotPreviouslyReviewed).test(current, learnerId)) {
				reviewerMap.get(current.getLearnerId()).add(learnerId);
				currentActiveSubmissionReviewers.computeIfAbsent(current.getLearnerId(), k -> new HashSet<>())
						.add(new ReviewerScore(learnerId, 0));
				submissionsToReque.add(current);
				break;
			}

		}

		submissionsToReque.stream().forEach(e->reQueueSubmission(e));
		return Optional.ofNullable(current);
	}

	private void reQueueSubmission(Submission current) {
		if (!maxReviewersReached.test(current)) {
			submissionQueue.add(current);
		}
	}

	public void turnInReview(Submission reviewingSubmission, int currentReviewScore, int reviewerid, int tick) {
		Set<ReviewerScore> currentReviewers = currentActiveSubmissionReviewers.get(reviewingSubmission.getLearnerId());
		currentReviewers.stream().filter(r -> r.learnerId == reviewerid && !r.isComplete).forEach(score -> {
			score.score = currentReviewScore;
			score.isComplete = true;
			score.tick = tick;
		});

	}

	public boolean isOutComeAvailable(Submission submission) {
		return currentActiveSubmissionReviewers.get(submission.getLearnerId()).stream()
				.filter(reviewer -> reviewer.isComplete).count() == MAX_REVIEWER_COUNT;

	}

	public boolean isFailed(Submission submission) {
		return currentActiveSubmissionReviewers.get(submission.getLearnerId()).stream()
				.filter(reviewer -> reviewer.isComplete).mapToInt(r -> r.score).sum() >= 240;
	}

	public int getTotalScore(Submission submission) {

		return currentActiveSubmissionReviewers.get(submission.getLearnerId()).stream()
				.filter(reviewer -> reviewer.isComplete).mapToInt(r -> r.score).sum();
	}

	public int getLastReviewTick(Submission submission) {
		Comparator<ReviewerScore> comparing = (Comparator.comparing(t -> t.tick));
		Optional<ReviewerScore> finalReviewer = currentActiveSubmissionReviewers.get(submission.getLearnerId()).stream()
				.filter(reviewer -> reviewer.isComplete).sorted(comparing.reversed()).findFirst();

		return (finalReviewer.isPresent()) ? finalReviewer.get().tick : -1;
	}

}

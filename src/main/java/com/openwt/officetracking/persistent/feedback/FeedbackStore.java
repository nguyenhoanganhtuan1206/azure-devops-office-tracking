package com.openwt.officetracking.persistent.feedback;

import com.openwt.officetracking.domain.feedback.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.openwt.officetracking.persistent.feedback.FeedbackEntityMapper.*;
import static org.apache.commons.collections4.IterableUtils.toList;

@Repository
@RequiredArgsConstructor
public class FeedbackStore {

    private final FeedBackRepository feedBackRepository;

    public List<Feedback> findFeedbackMenteeInCourseByAdmin(final UUID courseId, final UUID menteeId) {
        return toFeedbacks(toList(feedBackRepository.findByCourseIdAndMenteeId(courseId, menteeId)));
    }

    public List<Feedback> findFeedbackMenteeInCourseByMentor(final UUID courseId, final UUID mentorId, final UUID menteeId) {
        return toFeedbacks(toList(feedBackRepository.findByCourseIdAndMentorIdAndMenteeId(courseId, mentorId, menteeId)));
    }

    public List<Feedback> findFeedbackMenteeInCourseByCoach(final UUID courseId, final UUID coachId, final UUID menteeId) {
        return toFeedbacks(toList(feedBackRepository.findByCourseIdAndCoachIdAndMenteeId(courseId, coachId, menteeId)));
    }

    public Optional<Feedback> findById(final UUID feedbackId) {
        return feedBackRepository.findById(feedbackId)
                .map(FeedbackEntityMapper::toFeedback);
    }

    public Feedback save(final Feedback feedback) {
        return toFeedback(feedBackRepository.save(toFeedbackEntity(feedback)));
    }

    public void delete(final UUID feedbackId) {
        feedBackRepository.deleteById(feedbackId);
    }
}

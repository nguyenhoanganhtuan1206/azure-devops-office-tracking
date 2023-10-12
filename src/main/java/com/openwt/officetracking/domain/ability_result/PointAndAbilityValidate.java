package com.openwt.officetracking.domain.ability_result;

import com.openwt.officetracking.domain.overall_rating.OverallRating;
import com.openwt.officetracking.domain.review_status.ReviewStatus;
import lombok.experimental.UtilityClass;

import static com.openwt.officetracking.error.CommonError.supplyValidationError;

@UtilityClass
public class PointAndAbilityValidate {

    public static void validatePointAndAbilityResult(final PointAndAbilityResult pointAndAbilityResult, final ReviewStatus reviewStatus) {
        if (pointAndAbilityResult.getAbilityId() == null) {
            throw supplyValidationError("Something went wrong with ability. Please try again").get();
        }

        final Integer point = pointAndAbilityResult.getPoint();

        if (reviewStatus == ReviewStatus.SUBMITTED) {
            if (point == null) {
                throw supplyValidationError("Point cannot be empty. Please try again.").get();
            }

            if (point < 1 || point > 5) {
                throw supplyValidationError("Invalid point value. Point must be between 1 and 5").get();
            }
            return;
        }

        if (point != null && (point < 1 || point > 5)) {
            throw supplyValidationError("Invalid point value. Point must be between 1 and 5").get();
        }
    }

    public static OverallRating calculateOverallRating(final int total) {
        if (total < 30) {
            return OverallRating.FAIR;
        }

        if (total < 42) {
            return OverallRating.GOOD;
        }

        if (total < 54) {
            return OverallRating.VERY_GOOD;
        }

        return OverallRating.EXCELLENT;
    }
}

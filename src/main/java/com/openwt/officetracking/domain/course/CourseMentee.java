package com.openwt.officetracking.domain.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import java.util.UUID;

@Getter
@Setter
@Builder
@With
public class CourseMentee {

    private UUID menteeId;

    private String firstName;

    private String lastName;
}

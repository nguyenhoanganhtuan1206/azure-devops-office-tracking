package com.openwt.officetracking.persistent.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Table(name = "courses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String description;

    private Instant startAt;

    private Instant endAt;

    private Instant updatedAt;

    private Instant createdAt;
}

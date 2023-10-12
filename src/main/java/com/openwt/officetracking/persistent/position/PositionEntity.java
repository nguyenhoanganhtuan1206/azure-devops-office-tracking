package com.openwt.officetracking.persistent.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "positions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private boolean isTemporary;
}

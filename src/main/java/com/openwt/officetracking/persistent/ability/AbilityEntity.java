package com.openwt.officetracking.persistent.ability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "abilities")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AbilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private UUID abilityCategoryId;
}
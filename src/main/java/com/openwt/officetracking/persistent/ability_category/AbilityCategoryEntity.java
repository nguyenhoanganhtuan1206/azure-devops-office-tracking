package com.openwt.officetracking.persistent.ability_category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "ability_categories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AbilityCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
}
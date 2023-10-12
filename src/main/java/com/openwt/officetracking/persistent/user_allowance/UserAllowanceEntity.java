package com.openwt.officetracking.persistent.user_allowance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "user_allowances")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAllowanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID userId;

    private double annual_total;

    private double annual_taken;

    private double annual_booked;

    private double sick_remain;

    private double sick_booked;

    private double non_pair_taken;

    private double non_pair_booked;

    private double time_off_in_lieu;

    private double carry_over_allowance;
}

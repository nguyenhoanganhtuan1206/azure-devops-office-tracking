package com.openwt.officetracking.domain.tracking_history;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@With
public class OfficeAttendanceSummary {

    private int currentEmployees;

    private int checkedInEmployees;

    private int checkedOutEmployees;
}

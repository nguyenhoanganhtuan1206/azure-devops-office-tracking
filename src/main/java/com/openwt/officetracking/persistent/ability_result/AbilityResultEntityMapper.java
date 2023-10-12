package com.openwt.officetracking.persistent.ability_result;

import com.openwt.officetracking.domain.ability_result.AbilityResult;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class AbilityResultEntityMapper {

    public static AbilityResult toAbilityResult(final AbilityResultEntity abilityResultEntity) {
        return AbilityResult.builder()
                .id(abilityResultEntity.getId())
                .point(abilityResultEntity.getPoint())
                .abilityId(abilityResultEntity.getAbilityId())
                .createdAt(abilityResultEntity.getCreatedAt())
                .updatedAt(abilityResultEntity.getUpdatedAt())
                .courseAssignmentId(abilityResultEntity.getCourseAssignmentId())
                .isMentor(abilityResultEntity.isMentor())
                .createdAt(abilityResultEntity.getCreatedAt())
                .build();
    }

    public static List<AbilityResult> toAbilityResults(final List<AbilityResultEntity> abilityResultEntities) {
        return emptyIfNull(abilityResultEntities)
                .stream()
                .map(AbilityResultEntityMapper::toAbilityResult)
                .toList();
    }

    public static AbilityResultEntity toAbilityResultEntity(final AbilityResult abilityResult) {
        return AbilityResultEntity.builder()
                .id(abilityResult.getId())
                .point(abilityResult.getPoint())
                .abilityId(abilityResult.getAbilityId())
                .createdAt(abilityResult.getCreatedAt())
                .updatedAt(abilityResult.getUpdatedAt())
                .courseAssignmentId(abilityResult.getCourseAssignmentId())
                .isMentor(abilityResult.isMentor())
                .build();
    }

    public static List<AbilityResultEntity> toAbilityResultEntities(final List<AbilityResult> abilityResults) {
        return emptyIfNull(abilityResults)
                .stream()
                .map(AbilityResultEntityMapper::toAbilityResultEntity)
                .toList();
    }
}

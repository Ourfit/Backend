package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.user.data.entity.enums.GenderType;
import io.ourfit.api.domain.user.data.entity.enums.SkillLevelType;

/**
 * 나의 메이트 정보 DTO
 *
 * @param id 메이트의 사용자 ID
 * @param profileUrl 프로필 이미지 URL
 * @param nickname 닉네임
 * @param gender 성별
 * @param age 나이
 * @param skillLevelType 운동 실력
 */
public record MyMateInfoDto(
    long id,
    String profileUrl,
    String nickname,
    GenderType gender,
    int age,
    SkillLevelType skillLevelType) {}

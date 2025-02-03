package io.ourfit.api.domain.user.dto.internal;

/**
 * 사용자 선호하는 운동 정보 DTO
 *
 * @param code 운동 코드
 * @param name 운동 이름
 */
public record UserFavoriteWorkoutDto(String code, String name) {}

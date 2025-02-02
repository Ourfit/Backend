package io.ourfit.api.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SkillLevelType {
  BEGINNER("이제 막 운동을 시작한 단계! 운동에 조금씩 적응 중"),
  INTERMEDIATE("운동이 생활의 일부가 된 단계! 운동이 꽤 익숙해요"),
  ADVANCED("운동이 이제 완전 내 몸 같은 단계! 고난도 동작도 척척");

  private final String description;
}

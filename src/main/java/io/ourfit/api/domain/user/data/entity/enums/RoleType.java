package io.ourfit.api.domain.user.data.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
  SUPER_ADMIN(0, "최고 관리자"),
  ADMIN(1, "관리자"),
  MANAGER(2, "매니저"),
  USER(10, "사용자");

  /** 권한 레벨 */
  private final int level;

  /** 권한 이름 */
  private final String roleName;

  public boolean isSuperAdmin() {
    return this == SUPER_ADMIN;
  }

  public boolean isAdmin() {
    return this.level < USER.level;
  }
}

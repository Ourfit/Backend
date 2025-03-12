package io.ourfit.api.domain.auth.template;

public final class OAuth2ProfileContextHolder {

  private static final ThreadLocal<Boolean> IS_PRODUCTION = ThreadLocal.withInitial(() -> true);

  private OAuth2ProfileContextHolder() {}

  public static void setAsDevelop() {
    IS_PRODUCTION.set(false);
  }

  public static void setAsProduction() {
    IS_PRODUCTION.set(true);
  }

  public static boolean isProduction() {
    return IS_PRODUCTION.get();
  }

  public static void clear() {
    IS_PRODUCTION.remove();
  }
}

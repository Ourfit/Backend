package io.ourfit.api.global.advice;

import io.micrometer.common.util.StringUtils;
import io.ourfit.api.global.config.properties.WebhookProperties;
import io.ourfit.api.global.data.annotation.RequireAdminNotification;
import io.ourfit.api.global.data.dto.WebhookRequest;
import io.ourfit.api.global.utils.OurfitSpelParser;
import io.ourfit.api.infra.client.http.DiscordClient;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** {@link RequireAdminNotification} 어노테이션이 붙은 메소드의 실행 결과에 따라 관리자에게 알림을 보내는 Advice */
@Aspect
@Component
@Profile("prod")
@RequiredArgsConstructor
public class AdminNotifierAdvice {

  public static final String MESSAGE_PREFIX = "[관리자 알림] ";

  private final DiscordClient discordClient;
  private final WebhookProperties webhookProperties;

  @Around("@annotation(requireAdminNotify)")
  public Object handleNotification(
      ProceedingJoinPoint joinPoint, RequireAdminNotification requireAdminNotify) throws Throwable {
    Object methodResult = joinPoint.proceed();

    if (shouldNotify(joinPoint, requireAdminNotify, methodResult)) {
      WebhookProperties.Discord discordProperties = this.webhookProperties.discord();
      String message = MESSAGE_PREFIX.concat(requireAdminNotify.message());
      WebhookRequest request = new WebhookRequest(message);
      this.discordClient.send(discordProperties.serverId(), discordProperties.token(), request);
    }

    return methodResult;
  }

  private static boolean shouldNotify(
      ProceedingJoinPoint joinPoint,
      RequireAdminNotification requireAdminNotify,
      Object methodResult) {
    String condition = requireAdminNotify.condition();
    return StringUtils.isBlank(condition) || evaluateCondition(condition, joinPoint, methodResult);
  }

  private static boolean evaluateCondition(
      String expression, ProceedingJoinPoint joinPoint, Object methodResult) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    return OurfitSpelParser.evaluateExpression(
        expression, methodSignature.getParameterNames(), methodResult);
  }
}

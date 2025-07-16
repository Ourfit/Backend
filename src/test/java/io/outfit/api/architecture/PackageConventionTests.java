package io.outfit.api.architecture;

import static com.tngtech.archunit.lang.conditions.ArchConditions.beAnnotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@AnalyzeClasses(packages = "io.ourfit.api")
public class PackageConventionTests {

  @ArchTest
  static final ArchRule configPackageOnlyConfigurationOrProperties =
      classes()
          .that()
          .resideInAnyPackage("io.ourfit.api.global.config..")
          .and()
          .areTopLevelClasses()
          .should(
              beAnnotatedWith(Configuration.class)
                  .or(beAnnotatedWith(ConfigurationProperties.class)))
          .because("config 패키지는 전역 설정 전용이므로 @Configuration 또는 @ConfigurationProperties만 허용한다.");
}

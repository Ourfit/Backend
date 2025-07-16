package io.outfit.api.architecture;

import static com.tngtech.archunit.lang.conditions.ArchConditions.beAnnotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AnalyzeClasses(packages = "io.ourfit.api")
public class SpringConventionTests {

  @ArchTest
  static final ArchRule noFieldAutowiredInjectionInBeans =
      noFields()
          .that()
          .areDeclaredInClassesThat()
          .areMetaAnnotatedWith(Component.class)
          .should(beAnnotatedWith(Autowired.class))
          .because("Bean 주입 시 @Autowired 필드 주입을 사용하지 않는다.");

  @ArchTest
  static final ArchRule noAutowiredMethodInBeans =
      noMethods()
          .that()
          .areDeclaredInClassesThat()
          .areMetaAnnotatedWith(Component.class)
          .should(beAnnotatedWith(Autowired.class))
          .because("Bean 주입 시 @Autowired 메서드 주입을 사용하지 않는다.");
}

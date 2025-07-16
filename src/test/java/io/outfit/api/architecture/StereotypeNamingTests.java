package io.outfit.api.architecture;

import static com.tngtech.archunit.lang.conditions.ArchConditions.haveNameEndingWith;
import static com.tngtech.archunit.lang.conditions.ArchConditions.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packages = "io.ourfit.api")
public class StereotypeNamingTests {

  @ArchTest
  static final ArchRule controllerClassesShouldFollowConvention =
      classes()
          .that()
          .areAnnotatedWith(Controller.class)
          .or()
          .areAnnotatedWith(RestController.class)
          .should(resideInAPackage("..controller.."))
          .andShould(haveNameEndingWith("Controller"))
          .because("Controller는 일반적인 Spring 컨벤션을 유지해야 한다.");

  @ArchTest
  static final ArchRule serviceClassesShouldFollowConvention =
      classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should(resideInAPackage("..service.."))
          .andShould(haveNameEndingWith("Service").or(haveNameEndingWith("ServiceImpl")))
          .because("Service 구현체는 일반적인 Spring 컨벤션을 유지해야 한다.");
}

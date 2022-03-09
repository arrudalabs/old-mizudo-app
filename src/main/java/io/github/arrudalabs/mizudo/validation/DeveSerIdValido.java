package io.github.arrudalabs.mizudo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DeveSerIdValidoValidator.class)
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeveSerIdValido {

    String message();

    Class<?> entityClass();

    Class<? extends Payload>[] payload() default {};

    Class<? extends Default>[] groups() default {};
}

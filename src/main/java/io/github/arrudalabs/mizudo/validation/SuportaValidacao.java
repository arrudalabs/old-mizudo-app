package io.github.arrudalabs.mizudo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

@Constraint(validatedBy = SuportaValidacaoValidator.class)
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SuportaValidacao {
    String message();

    Class<? extends Predicate> classeValidadora();

    Class<? extends Payload>[] payload() default {};

    Class<? extends Default>[] groups() default {};

}

package io.github.arrudalabs.mizudo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NaoDeveConterMembroCOmMesmoNomeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
public @interface NaoDeveConterMembroComMesmoNome {

    String message() default "Já existe membro com o nome informado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}

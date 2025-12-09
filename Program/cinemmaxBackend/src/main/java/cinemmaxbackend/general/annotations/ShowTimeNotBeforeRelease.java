package cinemmaxbackend.general.annotations;

import cinemmaxbackend.general.classes.validators.ShowTimeNotBeforeReleaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShowTimeNotBeforeReleaseValidator.class)
@Documented
public @interface ShowTimeNotBeforeRelease {
    String message() default "Show tiem cannot be before the film's cinema release date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

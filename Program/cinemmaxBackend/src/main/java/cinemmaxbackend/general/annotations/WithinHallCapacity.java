package cinemmaxbackend.general.annotations;

import cinemmaxbackend.general.classes.validators.WithinHallCapacityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WithinHallCapacityValidator.class)
public @interface WithinHallCapacity {
    String message() default "Total seats exceed hall capacity"; //default annotation error message
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package jhhan.harmonynow_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ChordIdsValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidChordIds {

    String message() default "첫 번째와 두 번째 코드는 필수입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
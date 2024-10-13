package jhhan.harmonynow_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ChordIdsValidator implements ConstraintValidator<ValidChordIds, List<Long>> {

    @Override
    public boolean isValid(List<Long> chordIds, ConstraintValidatorContext context) {
        // 첫 번째와 두 번째 요소가 -1이 아닌지 확인
        if (chordIds == null || chordIds.size() < 2) {
            return false; // 첫 번째와 두 번째 코드가 없는 경우도 유효하지 않음
        }
        return chordIds.get(0) != -1 && chordIds.get(1) != -1;
    }
}
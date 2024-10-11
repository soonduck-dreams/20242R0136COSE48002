package jhhan.harmonynow_backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(ValidFileExtension constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.extensions();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // 파일이 비어 있는 경우는 검증하지 않음
        }

        String fileExtension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        for (String ext : allowedExtensions) {
            if (fileExtension.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return false;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
package jhhan.harmonynow_backend.utils;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {}

    @Getter
    private static String fileCommonPath;

    private final static String[] imageExtensions = {"jpg", "jpeg", "png"};
    private final static String[] audioExtensions = {"mp3", "wav"};
    private final static String[] midiExtensions = {"mid"};

    @Autowired
    public FileUtils(Environment env) {
        FileUtils.fileCommonPath = env.getProperty("file.path");
    }

    public static String saveImageIfExists(MultipartFile file, String folder, Long fileName) throws IOException {
        return saveFileIfExists(file, folder, fileName, imageExtensions);
    }

    public static String saveAudioIfExists(MultipartFile file, String folder, Long fileName) throws IOException {
        return saveFileIfExists(file, folder, fileName, audioExtensions);
    }

    public static String saveMidiIfExists(MultipartFile file, String folder, Long fileName) throws IOException {
        return saveFileIfExists(file, folder, fileName, midiExtensions);
    }

    // 파일 저장 메서드 (ID 기반으로 파일명 생성)
    private static String saveFileIfExists(MultipartFile file, String folder, Long fileName, String[] allowedExtensions) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일명이 유효하지 않습니다.");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        boolean isAllowed = Arrays.stream(allowedExtensions)
                .anyMatch(ext -> ext.equalsIgnoreCase(fileExtension));
        if (!isAllowed) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다: " + fileExtension);
        }

        String filePath = Paths.get(fileCommonPath, "uploads", folder, fileName + "." + fileExtension).toString();

        try {
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            logger.error("파일 저장 실패: " + e.getMessage());
            throw e;
        }

        // 저장된 파일의 URL 반환 (fileCommonPath 이후의 상대 경로로)
        return formatPath(Paths.get("uploads", folder, fileName + "." + fileExtension).toString());
    }

    // 파일 확장자 추출 메서드
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private static String formatPath(String path) {
        String formattedPath = path.replace("\\", "/");
        if (!formattedPath.startsWith("/")) {
            formattedPath = "/" + formattedPath;
        }
        return formattedPath;
    }

    public static boolean deleteFile(String filePath) {
        // 파일 경로가 null이거나 빈 값일 경우 처리
        if (filePath == null || filePath.isEmpty()) {
            logger.warn("파일 경로가 유효하지 않음: " + filePath);
            return false;
        }

        File file = new File(getFileCommonPath() + filePath);

        // 파일이 존재할 경우 삭제 시도
        if (file.exists()) {
            try {
                boolean isDeleted = file.delete();
                if (isDeleted) {
                    logger.info("파일 삭제 성공: " + filePath);
                } else {
                    logger.error("파일 삭제 실패: " + filePath);
                }
                return isDeleted;
            } catch (SecurityException e) {
                logger.error("파일 삭제 권한 오류: " + filePath, e);
                return false;
            }
        } else {
            logger.warn("파일이 존재하지 않음: " + filePath);
            return false;
        }
    }
}

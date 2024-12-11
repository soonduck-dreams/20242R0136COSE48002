/**
 * FileUtils
 *
 * [1. 역할]
 * - 파일 업로드, 다운로드, 삭제와 관련된 유틸리티 기능을 제공하는 클래스.
 * - 이미지, 오디오, MIDI와 같은 특정 파일 형식의 저장 및 경로 처리를 담당.
 *
 * [2. 주요 기능]
 * - 파일 저장:
 *   - 특정 폴더 및 파일 이름(ID 기반)으로 파일을 저장.
 *   - 파일 확장자를 검사하여 허용된 형식만 저장.
 * - 파일 삭제:
 *   - 주어진 경로의 파일을 삭제하고 결과를 반환.
 * - 파일 읽기:
 *   - 주어진 경로에서 파일을 읽어 `File` 객체로 반환.
 * - 파일 경로 처리:
 *   - 저장된 파일의 경로를 데이터베이스에 저장될 URL 형식으로 변환.
 *
 * [3. 사용 사례]
 * - 코드 및 코드 진행 관련 이미지, 오디오, MIDI 파일을 저장하거나 삭제할 때 사용.
 * - 업로드된 파일의 경로를 생성하고 유효성을 검증하여 데이터베이스에 저장할 때 활용.
 */


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
    private static String targetBasePath;

    private final static String[] imageExtensions = {"jpg", "jpeg", "png"};
    private final static String[] audioExtensions = {"mp3", "wav"};
    private final static String[] midiExtensions = {"mid"};

    @Autowired
    public FileUtils(Environment env) {
        FileUtils.targetBasePath = env.getProperty("file.target-base-path");
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

        String filePath = Paths.get(targetBasePath, folder, fileName + "." + fileExtension).toString();

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

        String adjustedFilePath = filePath.replaceAll("^/+", "").replaceAll("^uploads/+","");
        File file = new File(Paths.get(targetBasePath, adjustedFilePath).toString());

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

    public static File getFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 파일 경로입니다.");
        }

        String adjustedFilePath = filePath.replaceAll("^/+", "").replaceAll("^uploads/+","");
        File file = new File(Paths.get(targetBasePath, adjustedFilePath).toString());

        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("파일이 존재하지 않거나 파일이 아닙니다: " + filePath);
        }

        return file;
    }

}

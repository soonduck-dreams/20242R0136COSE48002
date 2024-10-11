package jhhan.harmonynow_backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

@Component
public class FileUtils {

    private FileUtils() {}

    private static String fileCommonPath;

    private final static String[] imageExtensions = {"jpg", "jpeg", "png"};
    private final static String[] audioExtensions = {"mp3", "wav"};

    @Autowired
    public FileUtils(Environment env) {
        FileUtils.fileCommonPath = env.getProperty("file.path");
    }

    public static String saveImage(MultipartFile file, String folder, Long fileName) throws IOException {
        return saveFile(file, folder, fileName, imageExtensions);
    }

    public static String saveAudio(MultipartFile file, String folder, Long fileName) throws IOException {
        return saveFile(file, folder, fileName, audioExtensions);
    }

    // 파일 저장 메서드 (ID 기반으로 파일명 생성)
    private static String saveFile(MultipartFile file, String folder, Long fileName, String[] allowedExtensions) throws IOException {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일이 업로드되지 않았거나 파일명이 유효하지 않습니다.");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        boolean isAllowed = Arrays.stream(allowedExtensions)
                .anyMatch(ext -> ext.equalsIgnoreCase(fileExtension));
        if (!isAllowed) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다: " + fileExtension);
        }

        String filePath = Paths.get(System.getProperty("user.dir"), fileCommonPath, folder, fileName + "." + fileExtension).toString();
        file.transferTo(new File(filePath));

        // 저장된 파일의 URL 반환 (fileCommonPath 이후의 상대 경로로)
        return Paths.get(folder, fileName + "." + fileExtension).toString();
    }

    // 파일 확장자 추출 메서드
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

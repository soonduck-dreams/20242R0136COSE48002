package jhhan.harmonynow_backend.config;

import jhhan.harmonynow_backend.repository.ChordRepository;
import jhhan.harmonynow_backend.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DummyfileCopier implements CommandLineRunner {

    private final ChordRepository chordRepository;

    @Override
    public void run(String... args) throws Exception {
        // 더미 파일 복사 로직
        copyDummyFiles("src/main/resources/db/dummyfiles/chord/image", "uploads/chord/image");
        copyDummyFiles("src/main/resources/db/dummyfiles/chord/audio", "uploads/chord/audio");
        copyDummyFiles("src/main/resources/db/dummyfiles/progression/audio", "uploads/progression/audio");
        copyDummyFiles("src/main/resources/db/dummyfiles/progression/sample_midi", "uploads/progression/sample_midi");
    }

    private void copyDummyFiles(String source, String target) {
        Path sourceDir = Paths.get(source); // 소스 디렉토리
        Path targetDir = Paths.get(FileUtils.getFileCommonPath(), target); // 타겟 디렉토리

        try (Stream<Path> files = Files.list(sourceDir)) {
            files.forEach(file -> {
                try {
                    Path targetPath = targetDir.resolve(file.getFileName());
                    Files.createDirectories(targetDir); // Create target directory if it doesn't exist
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.err.println("Failed to copy " + file.getFileName() + ": " + e.getMessage());
                }
            });
            System.out.println("All files copied successfully!");
        } catch (IOException e) {
            System.err.println("Failed to copy files: " + e.getMessage());
        }

    }
}

package jhhan.harmonynow_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class DummyfileCopier implements CommandLineRunner {

    @Value("${file.source-base-path}")
    private String sourceBasePath;

    @Value("${file.target-base-path}")
    private String targetBasePath;

    @Override
    public void run(String... args) throws Exception {
        // 더미 파일 복사
        copyDummyFiles("chord/image", "chord/image");
        copyDummyFiles("chord/audio", "chord/audio");
        copyDummyFiles("progression/audio", "progression/audio");
        copyDummyFiles("progression/sample_midi", "progression/sample_midi");
    }

    private void copyDummyFiles(String source, String target) {
        Path targetDir = Paths.get(targetBasePath, target); // 타겟 경로

        try {
            // ResourcePatternResolver로 소스 리소스 읽기
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(sourceBasePath + source + "/*");

            for (Resource resource : resources) {
                try {
                    Path targetPath = targetDir.resolve(resource.getFilename());
                    Files.createDirectories(targetDir); // 타겟 디렉토리 생성
                    Files.copy(resource.getInputStream(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
//                    System.out.println("Copied: " + resource.getFilename());
                } catch (IOException e) {
                    System.err.println("Failed to copy " + resource.getFilename() + ": " + e.getMessage());
                }
            }
            System.out.println("All files copied successfully to " + targetDir);
        } catch (IOException e) {
            System.err.println("Failed to copy files from source: " + source + " - " + e.getMessage());
        }
    }
}


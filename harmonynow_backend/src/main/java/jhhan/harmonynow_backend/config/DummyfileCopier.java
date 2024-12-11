/**
 * DummyfileCopier
 *
 * [1. 역할]
 * - 애플리케이션 실행 시 특정 소스 디렉토리의 더미 파일을 대상 디렉토리로 복사하는 컴포넌트.
 * - 파일 시스템의 초기 설정을 자동화하여 개발 및 테스트 환경에서 필요한 파일을 준비.
 *
 * [2. 주요 기능]
 * - 소스 디렉토리의 리소스를 읽어와 대상 디렉토리에 복사.
 * - 복사 작업 중 디렉토리가 없으면 생성하고, 기존 파일은 덮어쓰기 처리.
 * - 예외 상황을 처리하며, 복사 실패 시 오류 메시지를 출력.
 *
 * [3. 사용 사례]
 * - 애플리케이션 실행 시 더미 파일을 자동으로 복사하여 초기 데이터 환경을 구성.
 * - 코드 및 코드 진행과 관련된 이미지, 오디오, MIDI 파일 등을 준비.
 */

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


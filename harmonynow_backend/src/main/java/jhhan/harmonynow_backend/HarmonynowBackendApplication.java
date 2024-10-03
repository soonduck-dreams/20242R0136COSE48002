package jhhan.harmonynow_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HarmonynowBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(HarmonynowBackendApplication.class, args);
	}

}

package ida.pe.Invetario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class InvetarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvetarioApplication.class, args);
	}

}

package forus.naviforyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class NaviforyouApplication {

	public static void main(String[] args) {
		SpringApplication.run(NaviforyouApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

}

package library.libCartMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LibCartMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibCartMicroserviceApplication.class, args);
	}

}

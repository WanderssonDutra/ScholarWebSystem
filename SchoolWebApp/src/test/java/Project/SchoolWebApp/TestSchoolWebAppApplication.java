package Project.SchoolWebApp;

import org.springframework.boot.SpringApplication;

public class TestSchoolWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(SchoolWebAppApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

package io.zambi.tinder_ai_backend;

import io.zambi.tinder_ai_backend.profiles.Gender;
import io.zambi.tinder_ai_backend.profiles.Profile;
import io.zambi.tinder_ai_backend.profiles.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Profile profile = new Profile(
				"1",
				"Shubham",
				"Bakshi",
				30,
				"Indian",
				Gender.MALE,
				"Software Engineer",
				"foo.jpg",
				"INTP"
		);

		profileRepository.save(profile);

		List<Profile> profiles = profileRepository.findAll();
		profiles.forEach(System.out::println);
	}
}

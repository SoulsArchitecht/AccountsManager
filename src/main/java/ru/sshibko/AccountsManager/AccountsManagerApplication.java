package ru.sshibko.AccountsManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("ru.sshibko.AccountsManager.*")
@ComponentScan("ru.sshibko.AccountsManager.*")
@EntityScan("ru.sshibko.AccountsManager.model.entity")
public class AccountsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsManagerApplication.class, args);
	}

}

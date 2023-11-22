package ru.sshibko.AccountsManager.service.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sshibko.AccountsManager.service.model.entity.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
}

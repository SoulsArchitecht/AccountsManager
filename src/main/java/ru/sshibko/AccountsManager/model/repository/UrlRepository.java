package ru.sshibko.AccountsManager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sshibko.AccountsManager.model.entity.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
}

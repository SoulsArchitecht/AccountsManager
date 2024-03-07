package ru.sshibko.AccountsManager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sshibko.AccountsManager.model.entity.Account;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    String searchQuery = "SELECT * FROM accounts  WHERE "
            + "link LIKE %:keyword%"
            + " OR description LIKE %:keyword%";

    @Query(value = searchQuery, nativeQuery = true)
    List<Account> findByKeyword(@Param("keyword") String keyword);
}

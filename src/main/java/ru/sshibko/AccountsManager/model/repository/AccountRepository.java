package ru.sshibko.AccountsManager.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    String searchQueryForCurrentUser = """
            SELECT a FROM Account a  WHERE a.user.id = :userId
            AND (a.link LIKE %:keyword% OR a.description LIKE %:keyword)
            """;

    String queryForCurrentUserWithKeywordAndStatus = """
            SELECT a FROM Account a  WHERE a.user.id = :userId
            AND (a.link LIKE %:keyword% OR a.description LIKE %:keyword)
            AND a.active = :active
            """;

    @Query(queryForCurrentUserWithKeywordAndStatus)
    Page<Account> findByCurrentUserWithKeywordAndStatus(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            @Param("active") Boolean active,
            Pageable pageable);

    @Query(value = searchQuery, nativeQuery = true)
    Page<Account> findByKeywordPaged(@Param("keyword") String keyword, PageRequest pageRequest);

    @Query(value = searchQuery, nativeQuery = true)
    List<Account> findByKeyword(@Param("keyword") String keyword);

    @Query(value = searchQueryForCurrentUser)
    Page<Account> findByCurrentUserAndKeyword(@Param("userId") Long userId,
                                              @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT a FROM Account a WHERE a.user.id = :accountId")
    Page<Account> findAllUserAccounts(@Param("accountId") Long accountId, Pageable pageable);
}

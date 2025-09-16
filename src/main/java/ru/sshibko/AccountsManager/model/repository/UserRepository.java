package ru.sshibko.AccountsManager.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sshibko.AccountsManager.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String searchQuery = "SELECT * FROM users u WHERE "
            + "u.email LIKE %:keyword%"
            + " OR u.login LIKE %:keyword%";

    String queryForAdminGetAllUsersWithKeywordAndStatus = """
            SELECT u FROM User u  WHERE
            (u.email LIKE %:keyword% OR u.login LIKE %:keyword)
            AND u.status = :status
            """;

/*    @Query(value = searchQuery, nativeQuery = true)
    Page<User> findUserByKeywordPaged(@Param("keyword") String keyword, PageRequest pageRequest);*/

    @Query(value = queryForAdminGetAllUsersWithKeywordAndStatus)
    Page<User> findAllUsersWithKeywordAndStatus(
            @Param("keyword") String keyword,
            @Param("status") Boolean status,
            Pageable pageable);

    @Query(value = searchQuery, nativeQuery = true)
    Page<User> findUserByKeywordPaged(String keyword, Pageable pageable);

    @Query(value = searchQuery, nativeQuery = true)
    List<User> findUserByKeyword(@Param("keyword") String keyword);

    //@EntityGraph(attributePaths = {}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}

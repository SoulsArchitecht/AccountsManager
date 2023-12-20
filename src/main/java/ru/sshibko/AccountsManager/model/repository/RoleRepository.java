package ru.sshibko.AccountsManager.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sshibko.AccountsManager.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

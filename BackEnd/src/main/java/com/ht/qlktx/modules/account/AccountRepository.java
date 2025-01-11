package com.ht.qlktx.modules.account;

import com.ht.qlktx.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM Account u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<Account> lookupByUsernameOrEmail(String keyword);

    boolean existsByEmailAndUsernameIsNot(String email, String username);
}

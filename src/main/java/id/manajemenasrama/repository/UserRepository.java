package id.manajemenasrama.repository;

import id.manajemenasrama.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u FROM User u WHERE TRIM(LOWER(u.username)) = ?1")
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE TRIM(LOWER(u.username)) = ?1 and u.id != ?2")
    Optional<User> findByUsernameAndIdNot(String username, Long id);

    List<User> findAll();
}


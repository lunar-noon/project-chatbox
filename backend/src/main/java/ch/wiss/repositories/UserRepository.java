package ch.wiss.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.wiss.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

package sit.syone.itbkkapi.userdatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.userdatasource.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

}

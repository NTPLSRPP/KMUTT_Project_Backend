package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.primarydatasource.entities.PrimaryUser;

public interface PrimaryUserRepository extends JpaRepository<PrimaryUser, String> {
}

package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.primarydatasource.entities.Constant;

public interface ConstantRepository extends JpaRepository<Constant, String> {
}

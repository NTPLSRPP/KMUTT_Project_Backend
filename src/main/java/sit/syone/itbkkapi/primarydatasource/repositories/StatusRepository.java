package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.syone.itbkkapi.primarydatasource.entities.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    boolean existsByNameAndBoardID(String name, String boardId);
    List<Status> findAllByName(String name);

    List<Status> findAllByBoardID(String boardID);
    Status findByIdAndBoardID(Integer id, String boardId);

    Boolean existsByIdAndBoardID(Integer id, String boardId);
}

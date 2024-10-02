package sit.syone.itbkkapi.primarydatasource.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import sit.syone.itbkkapi.primarydatasource.entities.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findAllByOwnerID(String userID);

    @Query(value = "SELECT b.isPublic FROM Board b WHERE b.boardID = :id")
    Boolean getIsPublicByBoardID(@Param("id") String boardID);

    @Modifying
    @Query(value = "UPDATE Board b SET b.isPublic = :vis WHERE b.boardID = :bID")
    void setVisibility(@Param("bID") String boardID, @Param("vis") Boolean newVis);
}

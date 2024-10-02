package sit.syone.itbkkapi.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sit.syone.itbkkapi.dtos.board.VisibilityDTO;
import sit.syone.itbkkapi.primarydatasource.entities.Board;
import sit.syone.itbkkapi.primarydatasource.entities.PrimaryUser;
import sit.syone.itbkkapi.primarydatasource.repositories.BoardRepository;
import sit.syone.itbkkapi.primarydatasource.repositories.PrimaryUserRepository;
import sit.syone.itbkkapi.primarydatasource.repositories.StatusRepository;
import sit.syone.itbkkapi.primarydatasource.repositories.TaskRepository;

import java.util.List;

@Service
public class UserBoardService {
    @Autowired
    private PrimaryUserRepository primaryUserRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;


    @Transactional(readOnly = true)
    public List<Board> getBoardsByUserID(String userID) {
        return boardRepository.findAllByOwnerID(userID);
    }

    public Board getBoardsDetail(String boardID) {
        return boardRepository.findById(boardID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
    }

    @Transactional
    public Board createBoardForUser(String userID, String boardName) {
        PrimaryUser user = primaryUserRepository.findById(userID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + userID + " does not exist !!!"));

        Board newBoard = new Board();
        newBoard.setBoardName(boardName);
        newBoard.setOwnerID(user.getUserID());
        newBoard.setIsPublic(false);

        Board createdBoard = boardRepository.save(newBoard);
        createdBoard.setOwner(user);
        return createdBoard;
    }

    @Transactional
    public Board updateBoard(String boardID, Board board) {
        Board boardToUpdate = boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board " + boardID + " does not exist !!!"));
        boardToUpdate.setBoardName(board.getBoardName());
        boardRepository.save(boardToUpdate);
        return boardToUpdate;
    }

    @Transactional
    public Board deleteBoard(String boardID) {
        Board board = boardRepository.findById(boardID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board " + boardID + " does not exist !!!"));
        taskRepository.deleteAll(taskRepository.findAllByBoardID(boardID));
        statusRepository.deleteAll(statusRepository.findAllByBoardID(boardID));
        boardRepository.delete(board);
        return board;
    }

    public String generateBoardID() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 10);
    }

    @Transactional
    public VisibilityDTO setVisibility(String boardID, VisibilityDTO visibility) {
        Boolean newVis = null;
        switch (visibility.getVisibility().toLowerCase()){
            case "public": newVis = true;
                break;
            case "private":newVis = false;
                break;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid visibility value");
        }
        try{
            boardRepository.setVisibility(boardID, newVis);
            return new VisibilityDTO(visibility.getVisibility().toLowerCase());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "update failed please try again");
        }
    }
}

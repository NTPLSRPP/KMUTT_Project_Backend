package sit.syone.itbkkapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.syone.itbkkapi.dtos.board.BoardPermissionDTO;
import sit.syone.itbkkapi.primarydatasource.repositories.BoardPermissionRepository;

@Service
public class BoardPermissionService {
    @Autowired
    BoardPermissionRepository boardPermissionRepository;

    public BoardPermissionDTO getPermission(String oid, String boardID){
        BoardPermissionDTO bpDTO = new BoardPermissionDTO();
        bpDTO.setPermission(boardPermissionRepository.getPermission(oid, boardID));
        return bpDTO;
    }
}

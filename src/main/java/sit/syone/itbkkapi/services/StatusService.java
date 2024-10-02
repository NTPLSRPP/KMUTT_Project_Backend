package sit.syone.itbkkapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sit.syone.itbkkapi.dtos.status.CreateStatusDTO;
import sit.syone.itbkkapi.primarydatasource.entities.Status;
import sit.syone.itbkkapi.primarydatasource.repositories.StatusRepository;
import sit.syone.itbkkapi.primarydatasource.repositories.TaskRepository;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ConstantService constantService;


    public List<Status> getAllStatuses(String boardID) {
        return statusRepository.findAllByBoardID(boardID);
    }

    @Transactional(readOnly = true)
    public Status getStatusById(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id,boardId);
        if(status == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found");
        }
        return status;
    }

    @Transactional
    public Status createStatus(CreateStatusDTO status, String boardID) {
        // Check if name is empty
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
        if (statusRepository.existsByNameAndBoardID(status.getName(), boardID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + status.getName() + ") already exists.");
        }
        try {
            // Save status
            if (status.getLimitEnabled() == null) {
                status.setLimitEnabled(false);
            }
            Status newStatus = new Status();
            newStatus.setName(status.getName());
            newStatus.setDescription(status.getDescription());
            newStatus.setLimitEnabled(status.getLimitEnabled());
            newStatus.setCustomizable(true);
            newStatus.setBoardID(boardID);
            return statusRepository.save(newStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save status.", e);
        }
    }

    @Transactional
    public void deleteStatus(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id, boardId);
        if(status == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to delete not found");
        }
        if (!status.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + status.getName() + " !!!");
        }
        if (!taskService.getAllTaskWithStatus(status).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + status.getName() + " because it is in use !!!");
        }
        try {
            statusRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete status.", e);
        }
    }

    @Transactional
    public void deleteAndReplaceStatus(Integer deleteId, Integer replaceId, String boardId) {
        // Check if status exists
        if (deleteId.equals(replaceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "destination status for task transfer must be different from current status");
        }
        Status deleteStatus = statusRepository.findByIdAndBoardID(deleteId, boardId);
        Status replaceStatus = statusRepository.findByIdAndBoardID(replaceId, boardId);
        if(replaceStatus == null || deleteStatus == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status not found please check both status ID");
        }
        if (!deleteStatus.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete status " + deleteStatus.getName() + " !!!");
        }

        // fetch all tasks with deleteStatus

        Integer delStatTaskNum = taskRepository.countByStatus(deleteStatus.getId());
        Integer repStatTaskNum = taskRepository.countByStatus(replaceStatus.getId());
        Integer constLimit = constantService.getConstValue("GStatLim");

        if(replaceStatus.getLimitEnabled()){
            if(delStatTaskNum + repStatTaskNum > constLimit) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot replace status " + deleteStatus.getName() + " with status " + replaceStatus.getName() + " because it will exceed the limit of 10 tasks !!!");
            }
        }
        try {
            // Replace all deleteStatus in tasks with replaceStatus
            taskRepository.transferStatusTasks(deleteStatus.getId(), replaceStatus.getId());
            // after replacing all deleteStatus in tasks, delete deleteStatus
            statusRepository.deleteById(deleteId);
        } catch (Exception e) {
            throw e;
        }


//        List<Task> taskList = taskRepository.findAllByStatus(deleteStatus);
//        List<Task> replaceTaskList = taskRepository.findAllByStatus(replaceStatus);
//        Integer constLimit = constantService.getConstValue("GStatLim");
//        if (replaceStatus.getLimitEnabled()) {
//            if (taskList.size() + replaceTaskList.size() > constLimit) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot replace status " + deleteStatus.getName() + " with status " + replaceStatus.getName() + " because it will exceed the limit of 10 tasks !!!");
//            }
//        }
//        try {
//            // Replace all deleteStatus in tasks with replaceStatus
//            for (Task task : taskList) {
//                task.setStatus(replaceStatus);
//            }
//            taskRepository.saveAll(taskList);
//            // after replacing all deleteStatus in tasks, delete deleteStatus
//            statusRepository.deleteById(deleteId);
//        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete status.", e);
//        }
    }

    @Transactional
    public Status updateStatus(Integer id, Status updateStatus, String boardId) {
        // Check if status exists
        Status existingStatus = statusRepository.findByIdAndBoardID(id, boardId);
        if(existingStatus == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Status to update not found");
        }
        // set existing status value
        if (!existingStatus.getCustomizable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot update status (" + existingStatus.getName() + ") !!!");
        }
        // check if name is empty
        if (updateStatus.getName() == null || updateStatus.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required.");
        }
//        if (statusRepository.existsByName(updateStatus.getName()) && !updateStatus.getId().equals(existingStatus.getId())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
//        }
        if(statusRepository.findAllByName(updateStatus.getName()).stream().anyMatch(status -> !status.getId().equals(updateStatus.getId()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name (" + updateStatus.getName() + ") already exists.");
        }
        // set updated values
        existingStatus.setName(updateStatus.getName());
        existingStatus.setDescription(updateStatus.getDescription());
        existingStatus.setLimitEnabled(updateStatus.getLimitEnabled());
        existingStatus.setCustomizable(updateStatus.getCustomizable());
        try {
            // save updated status
            return statusRepository.save(existingStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update status.", e);
        }
    }

    public Integer checkIsNotInUsed(Integer id, String boardId) {
        Status status = statusRepository.findByIdAndBoardID(id, boardId);
        if(status == null){
            new ResponseStatusException(HttpStatus.NOT_FOUND, "status " + id + " does not exist !!!");
        }
        return taskService.getAllTaskWithStatus(status).size();
    }

    public CreateStatusDTO trimStatus(CreateStatusDTO status) {
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required");
        }
        String trimmedName = status.getName().trim();
        String trimmedDescription = status.getDescription() != null ? status.getDescription().trim() : null;
        status.setName(trimmedName);
        status.setDescription(trimmedDescription);
        return status;
    }

    public Status trimStatusUpdate(Status status) {
        if (status.getName() == null || status.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status name is required");
        }
        String trimmedName = status.getName().trim();
        String trimmedDescription = status.getDescription() != null ? status.getDescription().trim() : null;
        status.setName(trimmedName);
        status.setDescription(trimmedDescription);
        return status;
    }

    public Map<Status, Integer> getAllStatUsage(String boardID) {
        Map usageMap = new HashMap();
        List<Status> statuses = getAllStatuses(boardID);
        statuses.forEach(status -> {
            usageMap.put(status.getId(), checkIsNotInUsed(status.getId(), boardID));
        });
        return usageMap;
    }
}

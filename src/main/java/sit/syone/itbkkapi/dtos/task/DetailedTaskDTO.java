package sit.syone.itbkkapi.dtos.task;

import lombok.Data;
import sit.syone.itbkkapi.primarydatasource.entities.Status;

import java.time.ZonedDateTime;

@Data
public class DetailedTaskDTO {
    private Integer id;
    private String title;
    private String description;
    private String assignees;
    private Status status;
    private ZonedDateTime createdOn;
    private ZonedDateTime updatedOn;
}

package sit.syone.itbkkapi.dtos.task;

import lombok.Data;
import sit.syone.itbkkapi.primarydatasource.entities.Status;

@Data
public class SimpleTaskDTO {
    private Integer id;
    private String Title;
    private String Assignees;
    private Status Status;

//    public String getStatus(){
//        return Status.getName();
//    }
}

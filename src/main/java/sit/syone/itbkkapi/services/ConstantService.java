package sit.syone.itbkkapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.syone.itbkkapi.primarydatasource.repositories.ConstantRepository;

@Service
public class ConstantService {
    @Autowired
    ConstantRepository constantRepository;


    public Integer getConstValue(String constName){
        return constantRepository.findById(constName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "value of " + constName + " does not exist")).getValue();
    }
}

package sit.syone.itbkkapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.syone.itbkkapi.services.ConstantService;

@RestController
@RequestMapping("/v3/const")
@CrossOrigin(origins = {"http://ip23sy1.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th"})
public class ConstantController {
    @Autowired
    ConstantService constantService;

    @GetMapping("/{constant}")
    public Integer getConstValue(@PathVariable String constant){
        return constantService.getConstValue(constant);
    }
}

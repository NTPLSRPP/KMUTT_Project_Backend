package sit.syone.itbkkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sit.syone.itbkkapi.services.UserBoardService;

@SpringBootTest
class ItbkkApiApplicationTests {
    @Autowired
    private UserBoardService userBoardService;

    @Test
    void contextLoads() {

    }

}

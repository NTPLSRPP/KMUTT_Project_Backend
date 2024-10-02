package sit.syone.itbkkapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.syone.itbkkapi.dtos.users.AccessTokenDTO;
import sit.syone.itbkkapi.dtos.users.LoginUserDTO;
import sit.syone.itbkkapi.dtos.users.TokenResponseDTO;
import sit.syone.itbkkapi.exceptions.InvalidTokenException;
import sit.syone.itbkkapi.exceptions.LoginInvalidException;
import sit.syone.itbkkapi.services.AuthenticationService;
import sit.syone.itbkkapi.services.UserService;

@RestController
@RequestMapping("/v3")
@CrossOrigin(origins = {"http://ip23sy1.sit.kmutt.ac.th:80", "http://localhost:5173", "http://intproj23.sit.kmutt.ac.th", "https://intproj23.sit.kmutt.ac.th"})
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) throws LoginInvalidException {
        return ResponseEntity.ok(authenticationService.loginUser(loginUserDTO));
    }

    @PostMapping("/token")
    public ResponseEntity<AccessTokenDTO> refreshToken(HttpServletRequest request) throws InvalidTokenException {

        String refreshToken = request.getHeader("Authorization");

        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.replace("Bearer ", "");
        } else {
            throw new InvalidTokenException("Refresh token is missing or malformed");
        }

        return ResponseEntity.ok(authenticationService.refreshAccessToken(refreshToken));
    }

}

package sit.syone.itbkkapi.services;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.syone.itbkkapi.dtos.users.AccessTokenDTO;
import sit.syone.itbkkapi.dtos.users.LoginUserDTO;
import sit.syone.itbkkapi.dtos.users.TokenResponseDTO;
import sit.syone.itbkkapi.dtos.users.UserDetailsDTO;
import sit.syone.itbkkapi.exceptions.InvalidTokenException;
import sit.syone.itbkkapi.exceptions.LoginInvalidException;
import sit.syone.itbkkapi.primarydatasource.entities.PrimaryUser;
import sit.syone.itbkkapi.primarydatasource.repositories.PrimaryUserRepository;
import sit.syone.itbkkapi.util.JwtUtils;

@Service
public class AuthenticationService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserService userService;
    @Autowired
    PrimaryUserService primaryUserService;
    @Autowired
    PrimaryUserRepository primaryUserRepository;

    public TokenResponseDTO loginUser(LoginUserDTO loginUserDTO) throws LoginInvalidException {
        UserDetailsDTO user = null;
        try {
            user = userService.loadUserByUsername(loginUserDTO.getUsername());
            if (!primaryUserRepository.existsById(user.getOid())) {
                primaryUserService.createUser(new PrimaryUser(user.getOid(), user.getUsername()));
            }
        }catch (UsernameNotFoundException ex){
            throw new LoginInvalidException("Username or Password is incorrect.");
        }
        Argon2PasswordEncoder argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
        if (!argon2PasswordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
            throw new LoginInvalidException("Username or Password is incorrect.");
        }

        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public AccessTokenDTO refreshAccessToken(String refreshToken) throws InvalidTokenException {
        try{
            Claims claims = jwtUtils.getAllClaimsFromToken(refreshToken);
            String oid = claims.get("oid", String.class);
            if(!primaryUserService.checkUserExist(oid)){
                throw new RuntimeException("user not exist");
            }
            UserDetailsDTO user = userService.loadUserByOid(oid);
            System.out.println(user);
            String newAccessToken = jwtUtils.generateToken(user);
            return new AccessTokenDTO(newAccessToken);
        }
        catch (Exception e){
            throw new InvalidTokenException("Refresh token is invalid or expired.");
        }
    }
}

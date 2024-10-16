package sit.syone.itbkkapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sit.syone.itbkkapi.filters.BoardPermissionFilter;
import sit.syone.itbkkapi.filters.JwtAuthenFilter;
import sit.syone.itbkkapi.services.UserService;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtAuthenFilter jwtAuthenFilter;
    @Autowired
    private BoardPermissionFilter boardPermissionFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeRequests(authorize -> authorize.requestMatchers("/v3/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/v3/token").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/boards/{boardID}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/boards/{boardID}/tasks").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/boards/{boardID}/statuses").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/boards/{boardID}/tasks/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/v3/boards/{boardID}/statuses/{id}").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        httpSecurity.addFilterBefore(jwtAuthenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(boardPermissionFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(csrf -> csrf.disable());
//        httpSecurity.authorizeRequests(authorize -> authorize.requestMatchers("/v3/login").permitAll()
//                        .requestMatchers("/error").permitAll()
//                        .requestMatchers(regexMatcher("/v3/boards/([a-zA-Z0-9_-]{10})")).permitAll()
//                        .requestMatchers(regexMatcher("/v3/boards/([a-zA-Z0-9_-]{10})/.*")).permitAll()
//                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//        httpSecurity.addFilterBefore(jwtAuthenFilter, UsernamePasswordAuthenticationFilter.class);
//        httpSecurity.addFilterAfter(boardPermissionFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return httpSecurity.build();
//    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return ((request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            String message = authException.getMessage();

        });
    }

}

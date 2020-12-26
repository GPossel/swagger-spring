package io.swagger.api;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.AuthenticationResponse;
import io.swagger.model.Login;
import io.swagger.model.User;
import io.swagger.service.UserApiService;
import io.swagger.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class LoginApiController implements LoginApi {

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserApiService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<?> loginUser(@ApiParam(value = "created users", required = true) @Valid @RequestBody Login authenticationRequest
    ) throws Exception {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                );
            } catch (BadCredentialsException e) {
                throw new Exception("Incorrect username or password", e);
            }
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String jwt = jwtTokenUtil.generateToken(userDetails);

            List<String> authorities = new ArrayList<>();
            for(GrantedAuthority authority : userDetails.getAuthorities())
            {
                authorities.add(authority.getAuthority());
            }

            if(authorities.contains("Blocked")){
                SecurityContextHolder.clearContext();
                ResponseEntity responseEntity = ResponseEntity.status(401).body((JsonNode) objectMapper.createObjectNode().put("message", "The user is blocked"));
                return responseEntity;
            }

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }

        return (ResponseEntity<?>) ResponseEntity.status(400);
    }

}
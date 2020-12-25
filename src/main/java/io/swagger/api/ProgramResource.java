package io.swagger.api;

import io.swagger.model.AuthenticationResponse;
import io.swagger.model.Login;
//import io.swagger.service.MyUserDetailsService;
import io.swagger.service.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import io.swagger.util.JwtUtil;

@Controller
public class ProgramResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserApiService userApiService;


    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "Hello World";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Login authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userApiService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

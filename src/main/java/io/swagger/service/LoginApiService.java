package io.swagger.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginApiService {

    public void checkForBlocks(UserDetails userDetails)
    {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority authority : userDetails.getAuthorities())
        {
            authorities.add(authority.getAuthority());
        }

        if(authorities.contains("Blocked")){
            SecurityContextHolder.clearContext();
            throw new ResponseStatusException(HttpStatus.valueOf(401), "The user us blocked");
        }
    }
}

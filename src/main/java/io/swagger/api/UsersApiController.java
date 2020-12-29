package io.swagger.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.model.UserRequest;
import io.swagger.service.UserApiService;
import io.swagger.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-18T09:28:40.437Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;

    @Autowired
    private UserApiService userApiService;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

//    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<User> create(@ApiParam(value = "created users", required = true) @Valid @RequestBody UserRequest body
    ) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");

        if (accept != null && content.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue(objectMapper.writeValueAsString(userApiService.create(body)), User.class), HttpStatus.CREATED);
            }  catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Employee')")
    public ResponseEntity<List<User>> getAll() {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");

        if (accept != null && content.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue(objectMapper.writeValueAsString(userApiService.getAll()), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('Employee')")
    public ResponseEntity<Void> delete(@ApiParam(value = "The userId that needs to be deleted", required = true) @PathVariable("userId") Long userId)
    {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                userApiService.delete(userId);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).body((JsonNode) objectMapper.createObjectNode().put("message", "Deleted Successfully!"));
                return responseEntity;
            } catch (Exception e) {
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> getById(@ApiParam(value= "", required = true) @PathVariable("userId") Long userId){
        String accept = request.getHeader("Accept");

        if (accept != null) {
            try {
                User user = userApiService.getByIdWithAuth(userId);
                return new ResponseEntity<User>(objectMapper.readValue(objectMapper.writeValueAsString(user), User.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Admin') or hasAuthority('Employee')")
    public ResponseEntity<List<User>> getUsers(@ApiParam(value = "") @Valid @RequestParam(value = "firstname", required = false) String firstname
            , @ApiParam(value = "") @Valid @RequestParam(value = "lastname", required = false) String lastname
            , @ApiParam(value = "", allowableValues = "Customer, Employee, Admin") @Valid @RequestParam(value = "RankOfUser", required = false) String rankOfUser
            , @ApiParam(value = "", allowableValues = "Active, Blocked") @Valid @RequestParam(value = "StatusOfUser", required = false) String statusOfUser
    ) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue(objectMapper.writeValueAsString(userApiService.getUsersForFilters(firstname, lastname, rankOfUser, statusOfUser)), List.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<User> update(@ApiParam(value = "Updated user object", required = true) @Valid @RequestBody User body
            , @ApiParam(value = "userId that need to be updated", required = true) @PathVariable("userId") Long id
    ) {
        String accept = request.getHeader("Accept");
        String content = request.getHeader("Content-Type");
        if (accept != null && content.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue(objectMapper.writeValueAsString(userApiService.update(id, body)), User.class), HttpStatus.OK);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message", e.getMessage()));
                return responseEntity;
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}

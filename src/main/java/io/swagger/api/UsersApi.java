/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-18T09:28:40.437Z[GMT]")
@Api(value = "users", description = "the users API")
public interface UsersApi {

    @ApiOperation(value = "Create a new user", nickname = "createUser", notes = "Create a new user", response = UserResponse.class, tags={ "users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "A user was created succesfully.", response = UserResponse.class),
        @ApiResponse(code = 400, message = "invalid operation"),
        @ApiResponse(code = 401, message = "Unauthorized"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 406, message = "Not Acceptable"),
        @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserResponse> create(@ApiParam(value = "created user" ,required=true )  @Valid @RequestBody UserRequest body);

    @ApiOperation(value = "returns list of all users", nickname = "getAll", notes = "returns list of all users", response = UserResponse.class, responseContainer = "List", tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succesful request.", response = UserResponse.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/users",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<UserResponse>> getAll(@ApiParam(value = "firstname of an user") @Valid @RequestParam(value = "firstname", required = false) String firstname,
                                              @ApiParam(value = "lastname of an user") @Valid @RequestParam(value = "lastname", required = false) String lastname,
                                              @ApiParam(value = "email of an user") @Valid @RequestParam(value = "email", required = false) String email
    );

    @ApiOperation(value = "Delete user", nickname = "deleteUser", notes = "Deletes a user, only the current user or an employee can delete a user.", tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successfully deleted!"),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/users/{userId}",
        method = RequestMethod.DELETE)
    ResponseEntity<Void> delete(@ApiParam(value = "The userId that needs to be deleted",required=true) @PathVariable("userId") Long userId);

    @ApiOperation(value = "Get Users by its Id", nickname = "getUserById", notes = "Get Users by its Id", response = UserResponse.class, tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user by userId", response = UserResponse.class),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/users/{userId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<UserResponse> getById(@ApiParam(value = "",required=true) @PathVariable("userId") Long userId
    );

    @ApiOperation(value = "Updated user", nickname = "updateUser", notes = "Updates the current logged in user, or an employee", response = UserResponse.class, tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful user update", response = UserResponse.class),
            @ApiResponse(code = 400, message = "invalid operation"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @RequestMapping(value = "/users/{userId}",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<UserResponse> update(@ApiParam(value = "user object that needs to be updated" ,required=false )  @Valid @RequestBody UserRequest body
            ,@ApiParam(value = "userId that need to be updated",required=true) @PathVariable("userId") Long userId
    );

}

/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T09:24:14.507Z[GMT]")
@Api(value = "logout", description = "the logout API")
public interface LogoutApi {

    @ApiOperation(value = "Logout & removes keys", nickname = "logout", notes = "Log the user out.", tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "succesfull logout"),
            @ApiResponse(code = 404, message = "User not found") })
    @RequestMapping(value = "/logout",
            method = RequestMethod.DELETE)
    ResponseEntity<Void> logout();
}


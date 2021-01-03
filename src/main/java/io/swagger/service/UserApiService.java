package io.swagger.service;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;
import io.swagger.dao.RepositoryUser;
import io.swagger.model.*;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@NoArgsConstructor
public class UserApiService implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    private User loggedInUser;

    public UserResponse create(UserRequest body) {
        User user = new User(body);
        user = repositoryUser.save(user);
        return new UserResponse(user);
    }

    public Iterable<UserResponse> getAll(UserRequest body) {
        List<User> users;
        if(body == null){
            users = (List<User>) repositoryUser.findAll();
        }
        else {
            users = repositoryUser.findByUserParams(body.getFirstname(), body.getLastname(), body.getEmail());
        }

        return convertListAccountToResponse(users);
    }

    public User getById(Long id) {
        Optional<User> optionalUser = repositoryUser.findById(id);
        if (!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalUser.get();
    }

    public UserResponse getByIdWithAuth(Long id) {
        User user = this.getById(id);
        if (!UserHasRights(id)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new UserResponse(user);
    }

    public void delete(Long id)  {
        User user = getById(id);
        user.setStatus(User.StatusEnum.DELETED);

        Integer i = repositoryUser.update(user, user.getId());
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public UserResponse update(Long id, UserRequest body) throws NoSuchFieldException, IllegalAccessException {
        User user = getById(id);
        if (!UserHasRights(id)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        user = user.checkNull(user, body);
        repositoryUser.update(user, id);

        return new UserResponse(user);
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }

    public Iterable<UserResponse> convertListAccountToResponse(Iterable<User> users){
        List<UserResponse> userResponses = new ArrayList<UserResponse>();
        users.forEach(user -> {
            userResponses.add(new UserResponse(user));
        });
        return userResponses;
    }

    public boolean UserHasRights(Long userId){

        loggedInUser = getLoggedInUser();

        if (!loggedInUser.getRank().equals(User.RankEnum.EMPLOYEE)){
            if (!this.loggedInUser.getId().equals(userId)){
                return false;
            }
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositoryUser.findByUserName(username);
        return user;
    }
}

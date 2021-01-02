package io.swagger.service;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;
import io.swagger.dao.RepositoryUser;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.model.UserRequest;
import io.swagger.model.UserResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
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
//        checkNull(body);
//        if(body == null){
//            Iterable<User> users = repositoryUser.findAll();
//            List<UserResponse> userResponse = new ArrayList<>();
//            users.forEach(user -> {
//                userResponse.add(new UserResponse(user));
//            });
//            return userResponse;
//        }
//        Iterable<User> users = repositoryUser.findAll();
//        return repositoryUser.findAll();

        return null;
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

    public UserResponse update(Long id, UserRequest body) {
        getByIdWithAuth(id); //checks if user exist and has rights to update

        //TODO CHECK OP NULL IN MODEL
        User user = new User(body);
        Integer i = repositoryUser.update(user, id);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        return new UserResponse(this.getById(id));
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
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

    public boolean checkNull(UserRequest userR) throws IllegalAccessException {
        for (Field f : userR.getClass().getDeclaredFields())
            if (f.get(this) != null)
                return false;
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositoryUser.findByUserName(username);
        return user;
    }
}

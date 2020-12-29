package io.swagger.service;

import ch.qos.logback.core.joran.conditional.ThenOrElseActionBase;
import io.swagger.dao.RepositoryUser;
import io.swagger.model.Account;
import io.swagger.model.User;
import io.swagger.model.UserRequest;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor
public class UserApiService implements UserDetailsService {

    @Autowired
    private RepositoryUser repositoryUser;

    private User loggedInUser;

    public User create(UserRequest body) {
        User user = new User(body);
        return repositoryUser.save(user);
    }

    public Iterable<User> getAll() {
        return repositoryUser.findAll();
    }

    public User getById(Long id) {
        Optional<User> optionalUser = repositoryUser.findById(id);
        if (!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalUser.get();
    }

    public User getByIdWithAuth(Long id) {
        User user = this.getById(id);
        if (!UserHasRights(id)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return user;
    }

    public void delete(Long id) {
        repositoryUser.deleteById(id);
    }

    public User update(Long id, User body) {
        getByIdWithAuth(id); //checks if user exist and has rights to delete
        Integer i = repositoryUser.updateUser(id, body);
        if (i == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        return this.getById(id);
    }

    public Iterable<User> getUsersForFilters(String firstname, String lastname, String rank, String status) {
        return new ArrayList<>();
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User)authentication.getPrincipal();
    }

    public boolean UserHasRights(Long userId){
        if (this.loggedInUser == null) {
            this.loggedInUser = this.getLoggedInUser();
        }

        if (this.loggedInUser.getRank() != User.RankEnum.EMPLOYEE){
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

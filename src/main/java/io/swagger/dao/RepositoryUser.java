package io.swagger.dao;

import io.swagger.model.*;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Parameter;
import org.springframework.stereotype.Repository;
import org.threeten.bp.LocalDate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.logging.Filter;

@Repository
public interface RepositoryUser extends CrudRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u = :user WHERE u.id = :id")
    Integer update(@Param("user") User user, @Param("id") Long id);

    @Query("select u from User u where u.email =:email")
    User findByUserName(@Param("email") String username);

    @Query("select u from User u where u.firstname = :firstname")
    List<User> findByFirstname(@Param("firstname") String firstname);

    @Query("select u from User u where u.lastname = :lastname")
    List<User> findByLastname(@Param("lastname") String lastname);

    @Query("select u from User u where u.rank = :rank")
    List<User> findByRank(@Param("rank") User.RankEnum rank);

    @Query("select u from User u where u.status = :status")
    List<User> findByStatus(@Param("status") User.StatusEnum status);

    @Query(value = "select u from User u where (u.firstname = :firstname or :firstname is null or :firstname = '') and (u.lastname = :lastname or :lastname is null or :lastname = '') and (u.email = :email or :email is null or :email = '')")
    List<User> findByParams(@Param("firstname") String firstname, @Param("lastname") String lastname, @Param("email") String email);
}
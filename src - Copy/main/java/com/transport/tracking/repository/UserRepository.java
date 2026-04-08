package com.transport.tracking.repository;


import com.transport.tracking.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.xusrname) = LOWER(:userName) AND u.xpswd = :password")
    public User findByXusrnameAndXpswd(@Param("userName") String userName, @Param("password") String password);

    @Query("SELECT u FROM User u WHERE LOWER(u.xlogin) = LOWER(:userName) AND u.xpswd = :password AND u.xact = :x")
    public User findByXloginAndXpswdAndXact(@Param("userName") String userName, @Param("password") String password, @Param("x") int x);

    public List<User> findAll();
}

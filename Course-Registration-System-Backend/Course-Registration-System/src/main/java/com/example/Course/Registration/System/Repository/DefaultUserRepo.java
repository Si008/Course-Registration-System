package com.example.Course.Registration.System.Repository;

import com.example.Course.Registration.System.Model.DefaultUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultUserRepo extends JpaRepository<DefaultUser,Integer> {

    @Query(value = "SELECT * FROM default_user WHERE user_name = :username", nativeQuery = true)
    DefaultUser findNativeByUserName(@Param("username") String username);

    boolean existsByUserName(String userName);

    void deleteByUserName(String userName);
}

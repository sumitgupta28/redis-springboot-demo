package com.spring.redis.db.repository;

import com.spring.redis.db.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <AppUser,Long>{

}

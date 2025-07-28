package com.example.user_service.repository;

import com.example.user_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String name);
}

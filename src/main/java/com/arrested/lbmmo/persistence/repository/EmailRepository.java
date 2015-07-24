package com.arrested.lbmmo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arrested.lbmmo.persistence.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {

}

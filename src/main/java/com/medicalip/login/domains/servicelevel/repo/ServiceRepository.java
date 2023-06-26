package com.medicalip.login.domains.servicelevel.repo;

import com.medicalip.login.domains.servicelevel.entity.ServiceLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceLevel, String>{
}

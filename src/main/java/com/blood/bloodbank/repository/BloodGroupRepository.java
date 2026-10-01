
package com.blood.bloodbank.repository;

import com.blood.bloodbank.entity.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodGroupRepository
        extends JpaRepository<BloodGroup, Integer> {
}
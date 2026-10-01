
package com.blood.bloodbank.repository;

import com.blood.bloodbank.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonorRepository
        extends JpaRepository<Donor, Integer> {

    interface DonorDetails {
        Integer getDonorId();
        String getName();
        Integer getAge();
        String getGender();
        String getPhone();
        String getBloodGroup();
    }

    @Query("""
        SELECT d.donorId AS donorId,
               d.name AS name,
               d.age AS age,
               d.gender AS gender,
               d.phone AS phone,
               bg.groupName AS bloodGroup
        FROM Donor d
        JOIN d.bloodGroup bg
        ORDER BY d.donorId
        """)
    List<DonorDetails> findAllDonorDetails();
}
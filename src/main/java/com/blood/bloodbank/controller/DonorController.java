
package com.blood.bloodbank.controller;

import com.blood.bloodbank.dto.DonorRequest;
import com.blood.bloodbank.entity.BloodGroup;
import com.blood.bloodbank.entity.Donor;
import com.blood.bloodbank.repository.BloodGroupRepository;
import com.blood.bloodbank.repository.DonorRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DonorController {

    private final DonorRepository donorRepository;
    private final BloodGroupRepository bloodGroupRepository;

    public DonorController(
            DonorRepository donorRepository,
            BloodGroupRepository bloodGroupRepository) {
        this.donorRepository = donorRepository;
        this.bloodGroupRepository = bloodGroupRepository;
    }

    // GET /api/donors
    // Uses a JOIN to return donor and blood-group details.
    @GetMapping("/donors")
    public List<DonorRepository.DonorDetails> getAllDonors() {
        return donorRepository.findAllDonorDetails();
    }

    // GET /api/donors/1
    @GetMapping("/donors/{id}")
    public Donor getDonorById(@PathVariable Integer id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Donor not found"));
    }

    // POST /api/donors
    @PostMapping("/donors")
    @ResponseStatus(HttpStatus.CREATED)
    public Donor registerDonor(
            @Valid @RequestBody DonorRequest request) {

        BloodGroup bloodGroup =
                bloodGroupRepository.findById(request.bloodGroupId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Invalid blood group ID"));

        Donor donor = new Donor();
        donor.setName(request.name());
        donor.setAge(request.age());
        donor.setGender(request.gender());
        donor.setPhone(request.phone());
        donor.setBloodGroup(bloodGroup);

        return donorRepository.save(donor);
    }

    // GET /api/blood-groups
    @GetMapping("/blood-groups")
    public List<BloodGroup> getBloodGroups() {
        return bloodGroupRepository.findAll();
    }
}
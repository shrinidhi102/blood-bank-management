
package com.blood.bloodbank.controller;

import com.blood.bloodbank.dto.DonationRequest;
import com.blood.bloodbank.repository.DonorRepository;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final JdbcTemplate jdbcTemplate;
    private final DonorRepository donorRepository;

    public DonationController(
            JdbcTemplate jdbcTemplate,
            DonorRepository donorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.donorRepository = donorRepository;
    }

    // GET /api/donations
    @GetMapping
    public List<Map<String, Object>> getDonations() {
        return jdbcTemplate.queryForList("""
            SELECT d.donation_id,
                   d.donor_id,
                   dr.name AS donor_name,
                   bg.group_name,
                   d.quantity_ml,
                   d.units_donated,
                   d.donation_date
            FROM DONATIONS d
            JOIN DONORS dr ON d.donor_id = dr.donor_id
            JOIN BLOOD_GROUPS bg
                ON dr.blood_group_id = bg.blood_group_id
            ORDER BY d.donation_date DESC, d.donation_id DESC
            """);
    }

    // POST /api/donations
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> registerDonation(
            @Valid @RequestBody DonationRequest request) {

        if (request.donationDate().isAfter(
                java.time.LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Donation date cannot be in the future");
        }

        if (!donorRepository.existsById(request.donorId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Donor not found");
        }

        jdbcTemplate.update(
                "CALL register_donation(?, ?, ?, ?)",
                request.donorId(),
                request.quantityMl(),
                request.unitsDonated(),
                Date.valueOf(request.donationDate())
        );

        return Map.of(
                "message", "Donation registered successfully",
                "status", "success"
        );
    }
}
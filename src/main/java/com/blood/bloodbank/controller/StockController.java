
package com.blood.bloodbank.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StockController {

    private final JdbcTemplate jdbcTemplate;

    public StockController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // GET /api/blood-units
    @GetMapping("/blood-units")
    public List<Map<String, Object>> getAllStock() {
        return jdbcTemplate.queryForList("""
            SELECT bg.blood_group_id,
                   bg.group_name,
                   bu.available_units
            FROM BLOOD_GROUPS bg
            JOIN BLOOD_UNITS bu
                ON bg.blood_group_id = bu.blood_group_id
            ORDER BY bg.blood_group_id
            """);
    }

    // GET /api/blood-units/1
    // Here the ID is the blood_group_id.
    @GetMapping("/blood-units/{groupId}")
    public Map<String, Object> getStock(
            @PathVariable Integer groupId) {

        List<Map<String, Object>> result =
                jdbcTemplate.queryForList("""
                    SELECT bg.blood_group_id,
                           bg.group_name,
                           get_available_units(
                               bg.blood_group_id
                           ) AS available_units
                    FROM BLOOD_GROUPS bg
                    WHERE bg.blood_group_id = ?
                    """, groupId);

        if (result.isEmpty()) {
            throw new org.springframework.web.server
                    .ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Blood group not found");
        }

        return result.get(0);
    }

    // GET /api/reports/below-average
    @GetMapping("/reports/below-average")
    public List<Map<String, Object>> getBelowAverageStock() {
        return jdbcTemplate.queryForList("""
            SELECT bg.group_name,
                   bu.available_units
            FROM BLOOD_GROUPS bg
            JOIN BLOOD_UNITS bu
                ON bg.blood_group_id = bu.blood_group_id
            WHERE bu.available_units < (
                SELECT AVG(available_units)
                FROM BLOOD_UNITS
            )
            ORDER BY bu.available_units
            """);
    }
}
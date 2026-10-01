DROP PROCEDURE IF EXISTS register_donation;

DELIMITER //

CREATE PROCEDURE register_donation(
    IN p_donor_id INT,
    IN p_quantity_ml INT,
    IN p_units_donated INT,
    IN p_donation_date DATE
)
BEGIN
    IF p_quantity_ml <= 0 OR p_units_donated <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Quantities must be positive';
END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM DONORS
        WHERE donor_id = p_donor_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Donor does not exist';
END IF;

INSERT INTO DONATIONS (
    donor_id,
    quantity_ml,
    units_donated,
    donation_date
)
VALUES (
           p_donor_id,
           p_quantity_ml,
           p_units_donated,
           p_donation_date
       );
END //

DELIMITER ;


DROP FUNCTION IF EXISTS get_available_units;

DELIMITER //

CREATE FUNCTION get_available_units(
    p_blood_group_id INT
)
    RETURNS INT
    READS SQL DATA
BEGIN
    DECLARE v_units INT DEFAULT 0;

SELECT COALESCE(MAX(available_units), 0)
INTO v_units
FROM BLOOD_UNITS
WHERE blood_group_id = p_blood_group_id;

RETURN v_units;
END //

DELIMITER ;


DROP TRIGGER IF EXISTS after_donation_insert;

DELIMITER //

CREATE TRIGGER after_donation_insert
    AFTER INSERT ON DONATIONS
    FOR EACH ROW
BEGIN
    UPDATE BLOOD_UNITS bu
        JOIN DONORS d
    ON d.blood_group_id = bu.blood_group_id
        SET bu.available_units =
            bu.available_units + NEW.units_donated
    WHERE d.donor_id = NEW.donor_id;
END //

DELIMITER ;
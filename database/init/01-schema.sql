CREATE TABLE IF NOT EXISTS BLOOD_GROUPS (
                                            blood_group_id INT PRIMARY KEY AUTO_INCREMENT,
                                            group_name VARCHAR(5) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS DONORS (
                                      donor_id INT PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20),
    phone VARCHAR(15),
    blood_group_id INT NOT NULL,
    FOREIGN KEY (blood_group_id)
    REFERENCES BLOOD_GROUPS(blood_group_id)
    );

CREATE TABLE IF NOT EXISTS DONATIONS (
                                         donation_id INT PRIMARY KEY AUTO_INCREMENT,
                                         donor_id INT NOT NULL,
                                         quantity_ml INT NOT NULL,
                                         units_donated INT NOT NULL DEFAULT 1,
                                         donation_date DATE NOT NULL,
                                         FOREIGN KEY (donor_id)
    REFERENCES DONORS(donor_id),
    CHECK (quantity_ml > 0),
    CHECK (units_donated > 0)
    );

CREATE TABLE IF NOT EXISTS BLOOD_UNITS (
                                           unit_id INT PRIMARY KEY AUTO_INCREMENT,
                                           blood_group_id INT NOT NULL UNIQUE,
                                           available_units INT NOT NULL DEFAULT 0,
                                           FOREIGN KEY (blood_group_id)
    REFERENCES BLOOD_GROUPS(blood_group_id),
    CHECK (available_units >= 0)
    );
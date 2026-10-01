INSERT IGNORE INTO BLOOD_GROUPS (group_name)
VALUES
    ('A+'),
    ('A-'),
    ('B+'),
    ('B-'),
    ('AB+'),
    ('AB-'),
    ('O+'),
    ('O-');

INSERT IGNORE INTO BLOOD_UNITS (blood_group_id, available_units)
SELECT blood_group_id, 0
FROM BLOOD_GROUPS;

INSERT INTO DONORS (name, age, gender, phone, blood_group_id)
SELECT 'Arun Kumar', 25, 'Male', '9000000001', blood_group_id
FROM BLOOD_GROUPS
WHERE group_name = 'A+'
  AND NOT EXISTS (
    SELECT 1 FROM DONORS
    WHERE name = 'Arun Kumar'
);

INSERT INTO DONORS (name, age, gender, phone, blood_group_id)
SELECT 'Priya Devi', 28, 'Female', '9000000002', blood_group_id
FROM BLOOD_GROUPS
WHERE group_name = 'O+'
  AND NOT EXISTS (
    SELECT 1 FROM DONORS
    WHERE name = 'Priya Devi'
);

INSERT INTO DONORS (name, age, gender, phone, blood_group_id)
SELECT 'Rahul Raj', 30, 'Male', '9000000003', blood_group_id
FROM BLOOD_GROUPS
WHERE group_name = 'B+'
  AND NOT EXISTS (
    SELECT 1 FROM DONORS
    WHERE name = 'Rahul Raj'
);
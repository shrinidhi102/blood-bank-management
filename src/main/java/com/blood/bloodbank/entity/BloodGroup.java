
package com.blood.bloodbank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BLOOD_GROUPS")
public class BloodGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blood_group_id")
    private Integer bloodGroupId;

    @Column(name = "group_name", nullable = false, unique = true)
    private String groupName;

    public BloodGroup() {
    }

    public Integer getBloodGroupId() {
        return bloodGroupId;
    }

    public void setBloodGroupId(Integer bloodGroupId) {
        this.bloodGroupId = bloodGroupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
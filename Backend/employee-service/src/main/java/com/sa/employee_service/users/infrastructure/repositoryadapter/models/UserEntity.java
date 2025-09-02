/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.sa.employee_service.users.infrastructure.repositoryadapter.models;

import java.time.LocalDate;

import com.sa.employee_service.employees.infrastructure.repositoryadapter.models.EmployeeEntity;
import com.sa.shared.models.AuditorEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends AuditorEntity {

    @Column(unique = true, length = 100)
    private String username;

    @Column(length = 255)
    private String password;

    private LocalDate desactivatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private EmployeeEntity employee;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntity(String id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

}

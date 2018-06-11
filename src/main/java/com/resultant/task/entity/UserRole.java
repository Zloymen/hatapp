package com.resultant.task.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
@Data
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Integer id;

    @Column(length = 200, nullable = false, unique = true)
    @JsonProperty
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
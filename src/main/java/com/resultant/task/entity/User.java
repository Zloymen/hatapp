package com.resultant.task.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.resultant.task.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails,Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Long id;

    @Column
    @JsonProperty
    private String username;

    @Column
    private String password;

    @Column(name = "is_account_non_expired")
    @JsonProperty
    private Boolean accountNonExpired = Boolean.TRUE;

    @Column(name = "is_account_non_locked")
    @JsonProperty
    private Boolean accountNonLocked = Boolean.TRUE;

    @Column(name = "is_credentials_non_expired")
    @JsonProperty
    private Boolean credentialsNonExpired = Boolean.TRUE;

    @Column(name = "is_enabled")
    @JsonProperty
    private Boolean enabled = Boolean.TRUE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @JsonProperty
    private List<UserRole> roles = new ArrayList<>();

    @Column(name = "is_replace_pass")
    @JsonProperty
    private Boolean replacePass = Boolean.FALSE;

    public User(UserDto dto, PasswordEncoder passwordEncoder, List<UserRole> role){
        this.password = passwordEncoder.encode(dto.getPassword());
        this.username = dto.getUsername();
        roles.addAll(role);
    }
    @JsonProperty
    public boolean getAdmin(){
        return roles.stream().anyMatch(item -> "admin".equals(item.getName()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

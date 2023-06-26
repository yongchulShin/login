package com.medicalip.login.domains.users.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalip.login.domains.match.entity.MatchUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_INFO_USER_ROLE")
public class UserRole {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
    @JsonIgnore
    @OneToMany(mappedBy = "userRoles")
    private Set<MatchUserRole> matchUserRoles;

    public void setRole(Set<MatchUserRole> matchUserRoles){
        this.matchUserRoles = matchUserRoles;
        matchUserRoles.forEach(o -> o.setUserRoles(this));
    }

}

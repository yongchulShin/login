package com.medicalip.login.domains.match.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medicalip.login.domains.users.entity.UserRole;
import com.medicalip.login.domains.users.entity.Users;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MATCH_USER_ROLE")
public class MatchUserRole {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "users_id")
    @ManyToOne
    private Users users;

    @JoinColumn(name = "roles_id")
    @ManyToOne
    private UserRole userRoles;

}

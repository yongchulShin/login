package com.medicalip.login.domains.match.repo;

import com.medicalip.login.domains.match.entity.MatchUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchUserRoleRepository extends JpaRepository<MatchUserRole, Long>{

    @Query(value = "select t1.* " +
            "from TB_MATCH_USER_ROLE t1 " +
            "where t1.users_id = :userSeq"
    , nativeQuery = true)
    List<MatchUserRole> findByUsers(long userSeq);
}

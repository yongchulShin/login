package com.medicalip.login.domains.match.repo;

import com.medicalip.login.domains.match.entity.MatchUserCredit;
import com.medicalip.login.domains.match.entity.pk.MatchUserCreditPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchUserCreditRepository extends JpaRepository<MatchUserCredit, MatchUserCreditPK>{

}

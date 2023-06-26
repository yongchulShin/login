package com.medicalip.login.domains.match.repo;

import com.medicalip.login.domains.match.entity.MatchUserProduct;
import com.medicalip.login.domains.match.entity.pk.MatchUserProductPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchUserProductRepository extends JpaRepository<MatchUserProduct, MatchUserProductPK>{

}

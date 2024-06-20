package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.VariationDetail;
import com.zubayer.zpos.entity.pk.VariationDetailPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface VariationDetailRepo extends JpaRepository<VariationDetail, VariationDetailPK>{

}

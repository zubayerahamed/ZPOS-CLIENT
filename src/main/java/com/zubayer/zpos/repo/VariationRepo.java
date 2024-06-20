package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Variation;
import com.zubayer.zpos.entity.pk.VariationPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface VariationRepo extends JpaRepository<Variation, VariationPK> {

}

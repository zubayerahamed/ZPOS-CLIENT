package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Charge;
import com.zubayer.zpos.entity.pk.ChargePK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface ChargeRepo extends JpaRepository<Charge, ChargePK>{

}

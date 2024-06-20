package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.UOM;
import com.zubayer.zpos.entity.pk.UOMPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface UOMRepo extends JpaRepository<UOM, UOMPK> {

}

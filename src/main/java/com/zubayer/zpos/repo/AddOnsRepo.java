package com.zubayer.zpos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.AddOns;
import com.zubayer.zpos.entity.pk.AddOnsPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface AddOnsRepo extends JpaRepository<AddOns, AddOnsPK> {

}

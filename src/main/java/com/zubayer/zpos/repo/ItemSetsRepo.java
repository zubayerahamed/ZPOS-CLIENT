package com.zubayer.zpos.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.ItemSets;
import com.zubayer.zpos.entity.pk.ItemSetsPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 16, 2024
 */
@Repository
public interface ItemSetsRepo extends JpaRepository<ItemSets, ItemSetsPK> {

	List<ItemSets> findAllByZid(Integer zid);

	List<ItemSets> findAllByZidAndXitem(Integer zid, Integer xitem);
}

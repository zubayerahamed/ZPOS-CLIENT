package com.zubayer.zpos.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.ItemVariations;
import com.zubayer.zpos.entity.pk.ItemVariationsPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 16, 2024
 */
@Repository
public interface ItemVariationsRepo extends JpaRepository<ItemVariations, ItemVariationsPK> {

	List<ItemVariations> findAllByZid(Integer zid);

	List<ItemVariations> findAllByZidAndXitem(Integer zid, Integer xitem);
}

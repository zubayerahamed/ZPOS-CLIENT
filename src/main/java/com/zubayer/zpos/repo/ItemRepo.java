package com.zubayer.zpos.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Item;
import com.zubayer.zpos.entity.pk.ItemPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface ItemRepo extends JpaRepository<Item, ItemPK> {

	List<Item> findAllByZid(Integer zid);

	List<Item> findAllByZidAndXcat(Integer zid, Integer xcat);
}

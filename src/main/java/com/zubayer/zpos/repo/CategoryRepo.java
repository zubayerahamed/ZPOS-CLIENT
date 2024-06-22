package com.zubayer.zpos.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zubayer.zpos.entity.Category;
import com.zubayer.zpos.entity.pk.CategoryPK;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Repository
public interface CategoryRepo extends JpaRepository<Category, CategoryPK> {

	List<Category> findAllByZid(Integer zid);
	List<Category> findAllByZidAndXpcode(Integer zid, Integer xpcode);
}

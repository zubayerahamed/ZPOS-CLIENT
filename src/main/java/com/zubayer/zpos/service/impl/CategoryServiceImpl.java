package com.zubayer.zpos.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zubayer.zpos.entity.Category;
import com.zubayer.zpos.entity.pk.CategoryPK;
import com.zubayer.zpos.repo.CategoryRepo;
import com.zubayer.zpos.service.CategoryService;

/**
 * @author Zubayer Ahamed
 * @since Jun 22, 2024
 */
@Service
public class CategoryServiceImpl extends AbstractService implements CategoryService{

	@Autowired private CategoryRepo categoryRepo;

	@Override
	public int hasNumberOfParent(Integer xcode) {
		return countParent(xcode, 1);
	}

	private Integer countParent(Integer xcode, Integer count) {

		Optional<Category> categoryOp = categoryRepo.findById(new CategoryPK(sessionManager.getBusinessId(), xcode));
		if(!categoryOp.isPresent()) return count; 

		Category c = categoryOp.get();
		if(c.getXpcode() != null) count = count + 1;
		countParent(c.getXpcode(), count);
		return count;
	}

}

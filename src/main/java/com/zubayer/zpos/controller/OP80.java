package com.zubayer.zpos.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zubayer.zpos.entity.Category;
import com.zubayer.zpos.entity.Item;
import com.zubayer.zpos.repo.CategoryRepo;
import com.zubayer.zpos.repo.ItemRepo;

/**
 * @author Zubayer Ahamed
 * @since Jun 6, 2024
 */
@Controller
@RequestMapping("/OP80")
public class OP80 extends AbstractBaseController {

	@Autowired private CategoryRepo categoryRepo;
	@Autowired private ItemRepo itemRepo;

	@Override
	protected String pageTitle() {
		return "POS";
	}

	@GetMapping
	public String index(Model model) {
		return "pages/OP80/OP80";
	}

	@GetMapping("/category/all")
	public @ResponseBody List<Category> getAllCategory(){
		List<Category> categories = categoryRepo.findAllByZid(sessionManager.getBusinessId());
		categories.sort(Comparator.comparing(Category::getXseqn).thenComparing(Category::getXname));
		categories = categories.stream().filter(f -> f.getXpcode() == null).collect(Collectors.toList());
		return categories;
	}

	@GetMapping("/item/{xcode}")
	public @ResponseBody List<Item> getAllItem(@PathVariable String xcode){
		if(!"ALL".equalsIgnoreCase(xcode)) {
			List<Item> items = itemRepo.findAllByZidAndXcat(sessionManager.getBusinessId(), Integer.parseInt(xcode));
			items.sort(Comparator.comparing(Item::getXseqn).thenComparing(Item::getXname));
			return items;
		}
		List<Item> items =  itemRepo.findAllByZid(sessionManager.getBusinessId());
		items.sort(Comparator.comparing(Item::getXseqn).thenComparing(Item::getXname));
		return items;
	}
}

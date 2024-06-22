package com.zubayer.zpos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zubayer.zpos.entity.AddOns;
import com.zubayer.zpos.entity.Item;
import com.zubayer.zpos.entity.ItemAddons;
import com.zubayer.zpos.entity.ItemSets;
import com.zubayer.zpos.entity.ItemVariations;
import com.zubayer.zpos.entity.UOM;
import com.zubayer.zpos.entity.Variation;
import com.zubayer.zpos.entity.VariationDetail;
import com.zubayer.zpos.entity.pk.AddOnsPK;
import com.zubayer.zpos.entity.pk.ItemPK;
import com.zubayer.zpos.entity.pk.UOMPK;
import com.zubayer.zpos.entity.pk.VariationPK;
import com.zubayer.zpos.repo.AddOnsRepo;
import com.zubayer.zpos.repo.ItemAddonsRepo;
import com.zubayer.zpos.repo.ItemRepo;
import com.zubayer.zpos.repo.ItemSetsRepo;
import com.zubayer.zpos.repo.ItemVariationsRepo;
import com.zubayer.zpos.repo.UOMRepo;
import com.zubayer.zpos.repo.VariationDetailRepo;
import com.zubayer.zpos.repo.VariationRepo;
import com.zubayer.zpos.service.ItemService;

/**
 * @author Zubayer Ahamed
 * @since Jun 22, 2024
 */
@Service
public class ItemServiceImpl extends AbstractService implements ItemService {

	@Autowired private ItemVariationsRepo itemVariationsRepo;
	@Autowired private ItemAddonsRepo itemAddonsRepo;
	@Autowired private ItemSetsRepo itemSetsRepo;
	@Autowired private VariationDetailRepo variationDetailRepo;
	@Autowired private VariationRepo variationRepo;
	@Autowired private AddOnsRepo addonsRepo;
	@Autowired private ItemRepo itemRepo;
	@Autowired private UOMRepo uomRepo;

	@Override
	public Item constructWithDetailData(Item item) {
		if(item == null || item.getXcode() == null) return item;

		List<ItemVariations> variations = itemVariationsRepo.findAllByZidAndXitem(sessionManager.getBusinessId(), item.getXcode());
		item.setVariations(variations);
		variations.stream().forEach(v -> {
			Optional<Variation> variationOp = variationRepo.findById(new VariationPK(sessionManager.getBusinessId(), v.getXvariation()));
			if(variationOp.isPresent()) v.setVariationName(variationOp.get().getXname());

			List<VariationDetail> options = variationDetailRepo.findAllByZidAndXcode(sessionManager.getBusinessId(), v.getXvariation());
			v.setOptions(options);
		});

		List<ItemAddons> addons = itemAddonsRepo.findAllByZidAndXitem(sessionManager.getBusinessId(), item.getXcode());
		item.setAddons(addons);
		addons.stream().forEach(a -> {
			Optional<AddOns> addOnsOp = addonsRepo.findById(new AddOnsPK(sessionManager.getBusinessId(), a.getXaddons()));
			if(addOnsOp.isPresent()) {
				a.setAddonsName(addOnsOp.get().getXname());
				a.setAddonsPrice(addOnsOp.get().getXprice());
			}
		});

		List<ItemSets> sets = itemSetsRepo.findAllByZidAndXitem(sessionManager.getBusinessId(), item.getXcode());
		item.setSets(sets);
		sets.stream().forEach(s -> {
			Optional<Item> itemOp = itemRepo.findById(new ItemPK(sessionManager.getBusinessId(), s.getXsetitem()));
			if(itemOp.isPresent()) {
				s.setSetItemName(itemOp.get().getXname());
				Optional<UOM> uomOp = uomRepo.findById(new UOMPK(sessionManager.getBusinessId(), itemOp.get().getXuom()));
				if(uomOp.isPresent()) s.setSetItemUnit(uomOp.get().getXname());
			}
		});

		return item;
	}

}

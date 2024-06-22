package com.zubayer.zpos.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zubayer.zpos.config.AppConfig;
import com.zubayer.zpos.entity.AddOns;
import com.zubayer.zpos.entity.Category;
import com.zubayer.zpos.entity.Charge;
import com.zubayer.zpos.entity.Currency;
import com.zubayer.zpos.entity.Item;
import com.zubayer.zpos.entity.ItemAddons;
import com.zubayer.zpos.entity.ItemSets;
import com.zubayer.zpos.entity.ItemVariations;
import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.UOM;
import com.zubayer.zpos.entity.Variation;
import com.zubayer.zpos.entity.VariationDetail;
import com.zubayer.zpos.entity.Xfloor;
import com.zubayer.zpos.entity.Xtable;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.enums.SyncMethod;
import com.zubayer.zpos.repo.AddOnsRepo;
import com.zubayer.zpos.repo.CategoryRepo;
import com.zubayer.zpos.repo.ChargeRepo;
import com.zubayer.zpos.repo.CurrencyRepo;
import com.zubayer.zpos.repo.ItemAddonsRepo;
import com.zubayer.zpos.repo.ItemRepo;
import com.zubayer.zpos.repo.ItemSetsRepo;
import com.zubayer.zpos.repo.ItemVariationsRepo;
import com.zubayer.zpos.repo.OutletRepo;
import com.zubayer.zpos.repo.ShopRepo;
import com.zubayer.zpos.repo.TerminalRepo;
import com.zubayer.zpos.repo.UOMRepo;
import com.zubayer.zpos.repo.VariationDetailRepo;
import com.zubayer.zpos.repo.VariationRepo;
import com.zubayer.zpos.repo.XfloorRepo;
import com.zubayer.zpos.repo.XtableRepo;
import com.zubayer.zpos.repo.XusersRepo;
import com.zubayer.zpos.repo.ZbusinessRepo;
import com.zubayer.zpos.service.SyncService;
import com.zubayer.zpos.service.ZSessionManager;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jun 4, 2024
 */
@Slf4j
@Service
public class SyncServiceImpl implements SyncService {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private ZbusinessRepo zbusinessRepo;
	@Autowired
	private OutletRepo outletRepo;
	@Autowired
	private ShopRepo shopRepo;
	@Autowired
	private TerminalRepo terminalRepo;
	@Autowired
	private ZSessionManager sessionManager;
	@Autowired
	private XusersRepo xusersRepo;
	@Autowired
	private UOMRepo uomRepo;
	@Autowired
	private ChargeRepo chargeRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private AddOnsRepo addOnsRepo;
	@Autowired
	private VariationRepo variationRepo;
	@Autowired
	private VariationDetailRepo variationDetailRepo;
	@Autowired
	private ItemRepo itemRepo;
	@Autowired
	private XfloorRepo floorRepo;
	@Autowired 
	private XtableRepo tableRepo;
	@Autowired
	private CurrencyRepo currencyRepo;
	@Autowired private ItemVariationsRepo itemVariationsRepo;
	@Autowired private ItemAddonsRepo itemAddonsRepo;
	@Autowired private ItemSetsRepo itemSetsRepo;

	@Transactional
	@Override
	public void syncBusiness(Integer business, Integer outlet, Integer shop, Integer terminal, SyncMethod syncMethod) {

		try {
			String businessDataUrl = appConfig.getApiUrl() + "/possync/business?businessid=" + business;
			Zbusiness zbusiness = restTemplate.getForObject(businessDataUrl, Zbusiness.class);
			// update data to db
			boolean updateStatus = false;
			if (zbusiness != null) {
				zbusiness = zbusinessRepo.save(zbusiness);
				if (zbusiness != null) {
					updateStatus = true;
					// update session data
					sessionManager.setBusiness(zbusiness);
				}
			}

			// update sync table

			// now send this sync info to server through api, that will help both side
			// communication

			// Outlet sync
			String outletDataUrl = appConfig.getApiUrl() + "/possync/outlet?businessid=" + business + "&outletid=" + outlet;
			Outlet xoutlet = restTemplate.getForObject(outletDataUrl, Outlet.class);
			// update data to db
			updateStatus = false;
			if (xoutlet != null) {
				xoutlet = outletRepo.save(xoutlet);
				if (xoutlet != null) {
					updateStatus = true;
					// update session data
					sessionManager.setOutlet(xoutlet);
				}
			}
			// update sync table

			// now send this sync info to server through api, that will help both side
			// communication

			// Shop sync
			String shopDataUrl = appConfig.getApiUrl() + "/possync/shop?businessid=" + business + "&outletid=" + outlet + "&shopid=" + shop;
			Shop xshop = restTemplate.getForObject(shopDataUrl, Shop.class);
			// update data to db
			updateStatus = false;
			if (xshop != null) {
				xshop = shopRepo.save(xshop);
				if (xshop != null) {
					updateStatus = true;
					// update session data
					sessionManager.setShop(xshop);
				}
			}
			// update sync table

			// now send this sync info to server through api, that will help both side
			// communication

			// Termninal sync
			String terminalDataUrl = appConfig.getApiUrl() + "/possync/terminal?businessid=" + business + "&outletid=" + outlet + "&shopid=" + shop + "&terminalid=" + terminal;
			Terminal xterminal = restTemplate.getForObject(terminalDataUrl, Terminal.class);
			// update data to db
			updateStatus = false;
			if (xterminal != null) {
				xterminal = terminalRepo.save(xterminal);
				if (xterminal != null) {
					updateStatus = true;
					// update session data
					sessionManager.setTerminal(xterminal);
				}
			}
			// update sync table

			// now send this sync info to server through api, that will help both side
			// communication

			// POS Users
			String xusersDataUrl = appConfig.getApiUrl() + "/possync/posusers?businessid=" + business + "&outletid=" + outlet + "&shopid=" + shop;
			List<Xusers> xusersList = restTemplate
					.exchange(xusersDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Xusers>>() {
					}).getBody();
			if (xusersList != null && !xusersList.isEmpty()) {
				xusersRepo.saveAll(xusersList);
			}

			// UOM
			List<UOM> uoms = restTemplate.exchange(appConfig.getApiUrl() + "/possync/uom?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<UOM>>() {
					}).getBody();
			if (uoms != null && !uoms.isEmpty()) {
				uomRepo.saveAll(uoms);
			}

			// Charge
			List<Charge> charges = restTemplate
					.exchange(appConfig.getApiUrl() + "/possync/charges?businessid=" + business, HttpMethod.GET, null,
							new ParameterizedTypeReference<List<Charge>>() {
							})
					.getBody();
			if (charges != null && !charges.isEmpty()) {
				chargeRepo.saveAll(charges);
			}

			// Floors
			List<Xfloor> floors = restTemplate.exchange(appConfig.getApiUrl() + "/possync/floors?businessid=" + business + "&outletid=" + outlet + "&shopid=" + shop, 
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Xfloor>>() {
					}).getBody();
			if (floors != null && !floors.isEmpty()) {
				floorRepo.saveAll(floors);
			}

			// Tables
			List<Xtable> tables = restTemplate.exchange(appConfig.getApiUrl() + "/possync/tables?businessid=" + business + "&outletid=" + outlet + "&shopid=" + shop, 
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Xtable>>() {
					}).getBody();
			if (tables != null && !tables.isEmpty()) {
				tableRepo.saveAll(tables);
			}

			// Currency
			List<Currency> currencies = restTemplate.exchange(appConfig.getApiUrl() + "/possync/currencies?businessid=" + business, 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Currency>>() {
				}).getBody();
			if (currencies != null && !currencies.isEmpty()) {
				currencyRepo.saveAll(currencies);
			}
		} catch (Exception e) {
			log.error("Business Sync Error : {}", e.getMessage());
		}

	}

	@Override
	public void syncMasterData(Integer business, Integer outlet, Integer shop, Integer terminal, SyncMethod syncMethod) {
		try {
			// Category
			List<Category> categories = restTemplate
					.exchange(appConfig.getApiUrl() + "/possync/category?businessid=" + business, HttpMethod.GET, null,
							new ParameterizedTypeReference<List<Category>>() {
							})
					.getBody();
			if (categories != null && !categories.isEmpty()) {
				categoryRepo.saveAll(categories);
			}

			// AddOns
			List<AddOns> addons = restTemplate.exchange(appConfig.getApiUrl() + "/possync/addons?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<AddOns>>() {
					}).getBody();
			if (addons != null && !addons.isEmpty()) {
				addOnsRepo.saveAll(addons);
			}

			// Variation
			List<Variation> variations = restTemplate
					.exchange(appConfig.getApiUrl() + "/possync/variation?businessid=" + business, HttpMethod.GET, null,
							new ParameterizedTypeReference<List<Variation>>() {
							})
					.getBody();
			if (variations != null && !variations.isEmpty()) {
				variationRepo.saveAll(variations);
			}

			// Variation Detail
			List<VariationDetail> variationdts = restTemplate
					.exchange(appConfig.getApiUrl() + "/possync/variation-detail?businessid=" + business, HttpMethod.GET,
							null, new ParameterizedTypeReference<List<VariationDetail>>() {
							})
					.getBody();
			if (variationdts != null && !variationdts.isEmpty()) {
				variationDetailRepo.saveAll(variationdts);
			}

			// Item
			List<Item> items = restTemplate.exchange(appConfig.getApiUrl() + "/possync/item?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<Item>>() {
					}).getBody();
			if (items != null && !items.isEmpty()) {
				itemRepo.saveAll(items);
			}

			// Item Variations
			List<ItemVariations> itemVariations = restTemplate.exchange(appConfig.getApiUrl() + "/possync/item-variations?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemVariations>>() {
					}).getBody();
			if (itemVariations != null && !itemVariations.isEmpty()) {
				itemVariationsRepo.saveAll(itemVariations);
			}

			// Item AddOns
			List<ItemAddons> itemAddons = restTemplate.exchange(appConfig.getApiUrl() + "/possync/item-addons?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemAddons>>() {
					}).getBody();
			if (itemAddons != null && !itemAddons.isEmpty()) {
				itemAddonsRepo.saveAll(itemAddons);
			}

			// Item Sets
			List<ItemSets> itemSets = restTemplate.exchange(appConfig.getApiUrl() + "/possync/item-sets?businessid=" + business,
					HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemSets>>() {
					}).getBody();
			if (itemSets != null && !itemSets.isEmpty()) {
				itemSetsRepo.saveAll(itemSets);
			}

		} catch (Exception e) {
			log.error("Error is : {}", e.getMessage());
		}

	}

}

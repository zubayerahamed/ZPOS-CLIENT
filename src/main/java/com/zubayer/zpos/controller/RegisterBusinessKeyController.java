package com.zubayer.zpos.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.zubayer.zpos.config.AppConfig;
import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.model.ResponseHelper;
import com.zubayer.zpos.repo.OutletRepo;
import com.zubayer.zpos.repo.ShopRepo;
import com.zubayer.zpos.repo.TerminalRepo;
import com.zubayer.zpos.repo.XusersRepo;
import com.zubayer.zpos.repo.ZbusinessRepo;
import com.zubayer.zpos.util.DeviceUtil;

/**
 * @author Zubayer Ahamed
 * @since May 28, 2024
 */
@Controller
@RequestMapping("/registerkey")
public class RegisterBusinessKeyController {

	@Autowired private ResponseHelper responseHelper;
	@Autowired private AppConfig appConfig;
	@Autowired private RestTemplate restTemplate;
	@Autowired private ZbusinessRepo zbusnessRepo;
	@Autowired private OutletRepo outletRepo;
	@Autowired private ShopRepo shopRepo;
	@Autowired private TerminalRepo terminalRepo;
	@Autowired private XusersRepo xusersRepo;

	@PostMapping
	public @ResponseBody Map<String, Object> registerKey(String poskey){
		if(StringUtils.isBlank(poskey)) {
			responseHelper.setErrorStatusAndMessage("POS Key Required");
			return responseHelper.getResponse();
		}

		// Validate key using api
		String validateUrl = appConfig.getApiUrl() + "/validate/key?key=" + poskey;
		boolean status = restTemplate.getForObject(validateUrl, Boolean.class);
		if(!status) {
			responseHelper.setErrorStatusAndMessage("Invalid Key");
			return responseHelper.getResponse();
		}

		// if validation success then import the data and save to db
		// zbusiness
		String businessDataUrl = appConfig.getApiUrl() + "/posdata/business?key=" + poskey;
		Zbusiness zbusiness = restTemplate.getForObject(businessDataUrl, Zbusiness.class);
		if(zbusiness == null) {
			responseHelper.setErrorStatusAndMessage("Invalid Business Id");
			return responseHelper.getResponse();
		}

		// outlet
		String outletDataUrl = appConfig.getApiUrl() + "/posdata/outlet?key=" + poskey;
		Outlet outlet = restTemplate.getForObject(outletDataUrl, Outlet.class);
		if(outlet == null) {
			responseHelper.setErrorStatusAndMessage("Invalid Outlet Id");
			return responseHelper.getResponse();
		}

		// shop
		String shopDataUrl = appConfig.getApiUrl() + "/posdata/shop?key=" + poskey;
		Shop shop = restTemplate.getForObject(shopDataUrl, Shop.class);
		if(shop == null) {
			responseHelper.setErrorStatusAndMessage("Invalid Shop Id");
			return responseHelper.getResponse();
		}

		// terminal
		String terminalDataUrl = appConfig.getApiUrl() + "/posdata/terminal?key=" + poskey;
		Terminal terminal = restTemplate.getForObject(terminalDataUrl, Terminal.class);
		if(terminal == null) {
			responseHelper.setErrorStatusAndMessage("Invalid Terminal Id");
			return responseHelper.getResponse();
		}
		if(StringUtils.isNotBlank(terminal.getXdevice()) && !terminal.getXdevice().equals(DeviceUtil.getDeviceId())) {
			responseHelper.setErrorStatusAndMessage("This key is registered with another device. Please revoke previous device.");
			return responseHelper.getResponse();
		}

		// xusers
		String xusersDataUrl = appConfig.getApiUrl() + "/posdata/posusers?key=" + poskey;
		List<Xusers> xusersList = restTemplate.exchange(xusersDataUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Xusers>>() {}).getBody();
		if(xusersList == null || xusersList.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Users not created for this POS");
			return responseHelper.getResponse();
		}

		// Find out the device id here and register it to ZPOS terminal
		String deviceId = DeviceUtil.getDeviceId();
		if(StringUtils.isBlank(deviceId)) {
			responseHelper.setErrorStatusAndMessage("Your device is not perfect to register this POS");
			return responseHelper.getResponse();
		}
		String registerPOSUrl = appConfig.getApiUrl() + "/registerpos/device?key=" + poskey + "&deviceid=" + deviceId;
		boolean registerStatus = restTemplate.getForObject(registerPOSUrl, Boolean.class);
		if(!registerStatus) {
			responseHelper.setErrorStatusAndMessage("POS Register failed");
			return responseHelper.getResponse();
		}

		// Now save all the data to db
		zbusnessRepo.save(zbusiness);
		outletRepo.save(outlet);
		shopRepo.save(shop);
		terminal.setXdevice(deviceId);
		terminalRepo.save(terminal);
		xusersRepo.saveAll(xusersList);

		responseHelper.setSuccessStatusAndMessage("POS Registered Successfully");
		responseHelper.setRedirectUrl("/login");
		return responseHelper.getResponse();
	}
}

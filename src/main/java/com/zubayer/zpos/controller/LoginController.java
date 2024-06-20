package com.zubayer.zpos.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.repo.TerminalRepo;
import com.zubayer.zpos.util.DeviceUtil;
import com.zubayer.zpos.util.POSUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2024
 * CSE202401068
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	private static final String FAKE_LOGAIN_PAGE_PATH = "login";
	private static final String BUSINESS_REGISTER_PAGE = "business-register";
	private static final String DEVICE_TERMINATED = "device-terminated";

	@Autowired private TerminalRepo terminalRepo;

	@GetMapping
	public String loadLoginPage(Model model) {
		// TODO: first check db has any business registered
		List<Zbusiness> businesses = businessRepo.findAll();
		if(businesses == null || businesses.isEmpty()) {
			model.addAttribute("pageTitle", "Register Business");
			log.debug("No business registered yet.", new Date());
			return BUSINESS_REGISTER_PAGE;
		}

		// if found zbusiness, then check this device is registered with server
		Terminal terminal = terminalRepo.findAll().getFirst();
		if(terminal == null) {
			model.addAttribute("pageTitle", "Device Terminated");
			log.debug("Termnal data not found. Device Terminated ", new Date());
			return DEVICE_TERMINATED;
		}
		String terminalDataUrl = appConfig.getApiUrl() + "/posdata/terminal?key=" + POSUtil.generatePOSKey(terminal.getZid(), terminal.getOutletId(), terminal.getShopId(), terminal.getId());
		Terminal term = null;
		try {
			term = restTemplate.getForObject(terminalDataUrl, Terminal.class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if(term != null) {
			if(!term.getXdevice().equals(DeviceUtil.getDeviceId())) {
				model.addAttribute("pageTitle", "Device Terminated");
				log.debug("Device terminated. Device key not matched ", new Date());
				return DEVICE_TERMINATED;
			}
		}


		// If device registered then open login form and do login work
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if (OUTSIDE_USERS_NAME.equalsIgnoreCase(username)) {
			model.addAttribute("pageTitle", "Login");
			log.debug("Login page called at {}", new Date());
			return FAKE_LOGAIN_PAGE_PATH;
		}

		return "redirect:/";
	}
}

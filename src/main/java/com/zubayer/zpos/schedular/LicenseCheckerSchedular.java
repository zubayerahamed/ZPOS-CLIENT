/**
 * 
 */
package com.zubayer.zpos.schedular;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zubayer.zpos.config.AppConfig;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.repo.TerminalRepo;
import com.zubayer.zpos.repo.ZbusinessRepo;
import com.zubayer.zpos.util.DeviceUtil;
import com.zubayer.zpos.util.POSUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@EnableScheduling
@Configuration
public class LicenseCheckerSchedular {

	@Autowired
	private ZbusinessRepo businessRepo;
	@Autowired
	private TerminalRepo terminalRepo;
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private RestTemplate restTemplate;

	@Scheduled(cron = "0 */1 * * * *")
	private void validatePOS() {
		log.info("Checking License at : " + new Date());

		List<Zbusiness> businesses = businessRepo.findAll();
		if (businesses == null || businesses.isEmpty())
			return;

		// if found zbusiness, then check this device is registered with server
		Terminal terminal = terminalRepo.findAll().getFirst();
		if (terminal == null)
			return;

		String terminalDataUrl = appConfig.getApiUrl() + "/posdata/terminal?key=" + POSUtil
				.generatePOSKey(terminal.getZid(), terminal.getOutletId(), terminal.getShopId(), terminal.getId());
		Terminal term = null;
		try {
			term = restTemplate.getForObject(terminalDataUrl, Terminal.class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (term != null) {
			if (term.getXdevice() == null || !term.getXdevice().equals(DeviceUtil.getDeviceId())) {
				// remove business and terminal info
				businessRepo.delete(businesses.get(0));
				terminalRepo.delete(terminal);
				log.info("License Terminated. Stopping the system.");
				System.exit(0);
			}
		}
	}
}

package com.zubayer.zpos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zubayer.zpos.enums.SyncMethod;
import com.zubayer.zpos.service.SyncService;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2024
 */
@Controller
@RequestMapping("/AD80")
public class AD80 extends AbstractBaseController {

	@Autowired private SyncService syncService;

	@Override
	protected String pageTitle() {
		return "Sync Status";
	}

	@GetMapping
	public String loadAD80(Model model) {

		syncService.syncBusiness(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId(), SyncMethod.AUTOMATIC);

		syncService.syncMasterData(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId(), SyncMethod.AUTOMATIC);

		return "pages/AD80/AD80";
	}
}


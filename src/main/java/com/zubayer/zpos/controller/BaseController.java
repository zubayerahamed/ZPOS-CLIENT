package com.zubayer.zpos.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.zubayer.zpos.config.AppConfig;
import com.zubayer.zpos.model.ResponseHelper;
import com.zubayer.zpos.repo.LogInOutRepo;
import com.zubayer.zpos.repo.XusersRepo;
import com.zubayer.zpos.repo.ZbusinessRepo;
import com.zubayer.zpos.service.ZSessionManager;
import com.zubayer.zpos.util.POSUtil;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2024
 * CSE202401068
 */
public class BaseController {

	protected static final String OUTSIDE_USERS_NAME = "anonymousUser";
	protected static final String ALL_BUSINESS = "ALL_ACCESSABLE_BUSINESS";
	protected static final String ERROR = "Error is : {}, {}"; 
	protected static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat SDF2 = new SimpleDateFormat("E, dd-MMM-yyyy");
	protected static final SimpleDateFormat SDF3 = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss");
	protected static final String UTF_CODE = "UTF-8";

	@Autowired protected ApplicationContext appContext;
	@Autowired protected Environment env;
	@Autowired protected ZSessionManager sessionManager;
	@Autowired protected AppConfig appConfig;
	@Autowired protected ResponseHelper responseHelper;
	@Autowired protected XusersRepo usersRepo;
	@Autowired protected ZbusinessRepo businessRepo;
	@Autowired protected RestTemplate restTemplate;
	@Autowired protected POSUtil posUtil;
	@Autowired protected LogInOutRepo logRepo;

	@ModelAttribute("appVersion")
	protected String appVersion() {
		return appConfig.getAppVersion();
	}
}

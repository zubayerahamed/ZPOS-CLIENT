package com.zubayer.zpos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Zubayer Ahamed
 * @since May 26, 2024
 */
@Controller
@RequestMapping({"/", "/home"})
public class HomeController extends AbstractBaseController {

	@Override
	protected String pageTitle() {
		return "Home";
	}

	@GetMapping
	public String loadHomePage() {
		System.out.println(sessionManager.getPosTime());
		return "index";
	}
}

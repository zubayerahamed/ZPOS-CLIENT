package com.zubayer.zpos.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.zubayer.zpos.entity.Currency;
import com.zubayer.zpos.entity.LogInOut;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.entity.pk.CurrencyPK;
import com.zubayer.zpos.entity.pk.XusersPK;
import com.zubayer.zpos.model.MyUserDetail;
import com.zubayer.zpos.model.validator.ModelValidator;
import com.zubayer.zpos.repo.CurrencyRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Validator;

/**
 * @author Zubayer Ahamed
 * @since Mar 25, 2024 
 * CSE202401068
 */
public abstract class AbstractBaseController extends BaseController {

	protected String pageTitle = null;

	@Autowired protected ModelValidator modelValidator;
	@Autowired protected Validator validator;
	@Autowired protected CurrencyRepo currencyRepo;

	@ModelAttribute("pageTitle")
	protected abstract String pageTitle();

	@ModelAttribute("loggedInUser")
	protected MyUserDetail loggedInUser() {
		return sessionManager.getLoggedInUserDetails();
	}

	@ModelAttribute("loggedInZbusiness")
	protected Zbusiness loggedInZbusiness() {
		return sessionManager.getLoggedInUserDetails().getZbusiness();
	}

	@ModelAttribute("defaultCurrency")
	protected Currency defaultCurrency() {
		String defaultCurrency = sessionManager.getBusiness().getCurrency();
		Optional<Currency> op = currencyRepo.findById(new CurrencyPK(sessionManager.getBusinessId(), defaultCurrency));
		return op.isPresent() ? op.get() : null;
	}

	@ModelAttribute("currency")
	protected Currency shopCurrency() {
		String currency = sessionManager.getLoggedInUserDetails().getShop().getXcurrency();
		Optional<Currency> op = currencyRepo.findById(new CurrencyPK(sessionManager.getBusinessId(), currency));
		return op.isPresent() ? op.get() : defaultCurrency();
	}

	@ModelAttribute("logInOut")
	protected LogInOut logInOut() {
		Optional<LogInOut> logOp = logRepo.findTop1ByZidAndXoutletAndXshopAndXterminalOrderByXgdateDescXshiftDesc(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId());
		if(!logOp.isPresent()) return null;

		LogInOut l = logOp.get();
		Optional<Xusers> userOp = usersRepo.findById(new XusersPK(sessionManager.getBusinessId(), l.getXusername()));
		if(userOp.isPresent()) {
			l.setSignInUser(userOp.get().getXdisplayname());
		}

		if("Open".equals(l.getXstatus()) && sessionManager.getPosUser() == null) {
			sessionManager.addToMap("POS_USER", usersRepo.findById(new XusersPK(sessionManager.getBusinessId(), l.getXusername())).get());
			sessionManager.addToMap("POS_SHIFT", l.getXshift());
			sessionManager.addToMap("POS_TIME", l.getXgdate());
		}

		return l;
	}

	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWithHeader = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(requestedWithHeader);
	}
}

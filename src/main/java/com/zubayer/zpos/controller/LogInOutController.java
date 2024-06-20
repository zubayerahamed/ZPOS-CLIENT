package com.zubayer.zpos.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zubayer.zpos.entity.LogInOut;
import com.zubayer.zpos.entity.pk.XusersPK;
import com.zubayer.zpos.enums.POSRole;
import com.zubayer.zpos.model.MyUserDetail;

/**
 * @author Zubayer Ahamed
 * @since May 30, 2024
 */
@Controller
@RequestMapping("/log-in-out")
public class LogInOutController extends AbstractBaseController {

	@Override
	protected String pageTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@PostMapping("/resignin")
	public @ResponseBody Map<String, Object> relogin(){
		MyUserDetail user = sessionManager.getLoggedInUserDetails();
		if(user == null) {
			responseHelper.setErrorStatusAndMessage("You are not logged In");
			return responseHelper.getResponse();
		}

		if(!(POSRole.ADMIN.name().equals(user.getRoles()) || POSRole.MANAGER.name().equals(user.getRoles()) || POSRole.SYSTEM_ADMIN.name().equals(user.getRoles()))) {
			responseHelper.setErrorStatusAndMessage("You are not allowed to do Reopen POS Re-Sign In");
			return responseHelper.getResponse();
		}

		Optional<LogInOut> logOp = logRepo.findTop1ByZidAndXoutletAndXshopAndXterminalOrderByXgdateDescXshiftDesc(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId());
		if(logOp.isPresent() && "Open".equals(logOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Already a user is Sign In");
			return responseHelper.getResponse();
		}

		LogInOut l = logOp.get();
		l.setXstatus("Open");
		l.setXouttime(null);
		logRepo.save(l);

		sessionManager.addToMap("POS_USER", usersRepo.findById(new XusersPK(sessionManager.getBusinessId(), l.getXusername())).get());
		sessionManager.addToMap("POS_SHIFT", l.getXshift());
		sessionManager.addToMap("POS_TIME", l.getXgdate());

		responseHelper.setSuccessStatusAndMessage("Re-Sign In Successfully");
		responseHelper.setRedirectUrl("/");
		return responseHelper.getResponse();
	}

	@PostMapping("/signin")
	public @ResponseBody Map<String, Object> login(){
		MyUserDetail user = sessionManager.getLoggedInUserDetails();
		if(user == null) {
			responseHelper.setErrorStatusAndMessage("You are not logged In");
			return responseHelper.getResponse();
		}

		if(!POSRole.CASHIER.name().equals(user.getRoles())) {
			responseHelper.setErrorStatusAndMessage("You are not Cashier to do POS Sign In");
			return responseHelper.getResponse();
		}

		Optional<LogInOut> logOp = logRepo.findTop1ByZidAndXoutletAndXshopAndXterminalOrderByXgdateDescXshiftDesc(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId());
		if(logOp.isPresent() && "Open".equals(logOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Already a user is Sign In");
			return responseHelper.getResponse();
		}

		Integer totalShift = sessionManager.getLoggedInUserDetails().getTerminal().getXshift();
		if(totalShift == null) totalShift = 1;

		Integer currentShift = 1;
		if(logOp.isPresent()) {
			if(SDF.format(logOp.get().getXgdate()).equals(SDF.format(new Date()))) {
				currentShift = logOp.get().getXshift() + 1;
			}
		}

		if(currentShift > totalShift) {
			responseHelper.setErrorStatusAndMessage("Max number of shift reached today.");
			return responseHelper.getResponse();
		}

		Date d = new Date();
		LogInOut l = new LogInOut();
		l.setZid(sessionManager.getBusinessId());
		l.setXoutlet(sessionManager.getOutletId());
		l.setXshop(sessionManager.getShopId());
		l.setXterminal(sessionManager.getTerminalId());
		l.setXshift(currentShift);
		l.setXusername(Integer.valueOf(sessionManager.getLoggedInUserDetails().getUsername()));
		l.setXgdate(d);
		l.setXintime(d);
		l.setXstatus("Open");
		logRepo.save(l);

		sessionManager.addToMap("POS_USER", usersRepo.findById(new XusersPK(sessionManager.getBusinessId(), Integer.valueOf(sessionManager.getLoggedInUserDetails().getUsername()))).get());
		sessionManager.addToMap("POS_SHIFT", currentShift);
		sessionManager.addToMap("POS_TIME", d);

		responseHelper.setSuccessStatusAndMessage("Sign In Successfully");
		responseHelper.setRedirectUrl("/");
		return responseHelper.getResponse();
	}

	@PostMapping("/signout")
	public @ResponseBody Map<String, Object> logout(){
		MyUserDetail user = sessionManager.getLoggedInUserDetails();
		if(user == null) {
			responseHelper.setErrorStatusAndMessage("You are not logged In");
			return responseHelper.getResponse();
		}

		if(!POSRole.CASHIER.name().equals(user.getRoles())) {
			responseHelper.setErrorStatusAndMessage("You are not Cashier to do POS Sign Out");
			return responseHelper.getResponse();
		}

		Optional<LogInOut> logOp = logRepo.findTop1ByZidAndXoutletAndXshopAndXterminalOrderByXgdateDescXshiftDesc(sessionManager.getBusinessId(), sessionManager.getOutletId(), sessionManager.getShopId(), sessionManager.getTerminalId());
		if(logOp.isPresent() && "Closed".equals(logOp.get().getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Already Sign Out");
			return responseHelper.getResponse();
		}

		if(logOp.isPresent() && !logOp.get().getXusername().equals(Integer.valueOf(user.getUsername()))) {
			responseHelper.setErrorStatusAndMessage("You are not allowed to do Sign Out");
			return responseHelper.getResponse();
		}

		LogInOut l = logOp.get();
		l.setXstatus("Closed");
		l.setXouttime(new Date());
		logRepo.save(l);

		sessionManager.removeFromMap("POS_USER");
		sessionManager.removeFromMap("POS_SHIFT");
		sessionManager.removeFromMap("POS_TIME");

		responseHelper.setSuccessStatusAndMessage("Sign Out Successfully");
		responseHelper.setRedirectUrl("/");
		return responseHelper.getResponse();
	}
}

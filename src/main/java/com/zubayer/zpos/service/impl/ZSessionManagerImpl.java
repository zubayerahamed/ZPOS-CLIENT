package com.zubayer.zpos.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.model.MyUserDetail;
import com.zubayer.zpos.service.ZSessionManager;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2024
 * CSE202101068
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ZSessionManagerImpl implements ZSessionManager {

	private Map<String, Object> sessionMap;

	public ZSessionManagerImpl() {
		this.sessionMap = new HashMap<>();
	}

	@Override
	public void addToMap(String key, Object value) {
		sessionMap.put(key, value);
	}

	@Override
	public Object getFromMap(String key) {
		return sessionMap.get(key);
	}

	@Override
	public void removeFromMap(String key) {
		if (sessionMap.containsKey(key))
			sessionMap.remove(key);
	}

	@Override
	public Integer getBusinessId() {
		Zbusiness business = getBusiness();
		return business == null ? null : business.getZid();
	}

	@Override
	public Integer getOutletId() {
		return getLoggedInUserDetails().getOutlet().getId();
	}

	@Override
	public Integer getShopId() {
		return getLoggedInUserDetails().getShop().getId();
	}

	@Override
	public Integer getTerminalId() {
		return getLoggedInUserDetails().getTerminal().getId();
	}

	@Override
	public Zbusiness getBusiness() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			return mud.getZbusiness();
		}

		return null;
	}

	@Override
	public void setBusiness(Zbusiness zbusiness) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			mud.setZbusiness(zbusiness);
		}
	}

	@Override
	public void setOutlet(Outlet outlet) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			mud.setOutlet(outlet);
		}
	}

	@Override
	public void setShop(Shop shop) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			mud.setShop(shop);
		}
	}

	@Override
	public void setTerminal(Terminal terminal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return;

		Object principal = auth.getPrincipal();
		if(principal instanceof MyUserDetail) {
			MyUserDetail mud = (MyUserDetail) principal;
			mud.setTerminal(terminal);
		}
	}

	@Override
	public MyUserDetail getLoggedInUserDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null || !auth.isAuthenticated()) return null;

		Object principal = auth.getPrincipal(); 

		if(principal instanceof MyUserDetail) {
			return (MyUserDetail) principal;
		}

		return null;
	}

	@Override
	public Date getPosTime() {
		if(sessionMap.get("POS_TIME") == null) return null;
		return (Date) sessionMap.get("POS_TIME");
	}

	@Override
	public Xusers getPosUser() {
		if(sessionMap.get("POS_USER") == null) return null;
		return (Xusers) sessionMap.get("POS_USER");
	}

	@Override
	public Integer getPosShift() {
		if(sessionMap.get("POS_SHIFT") == null) return null;
		return (Integer) sessionMap.get("POS_SHIFT");
	}

	
}

package com.zubayer.zpos.service;

import java.util.Date;

import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.model.MyUserDetail;

/**
 * @author Zubayer Ahamed
 * @since Mar 24, 2024
 * CSE202101068
 */
public interface ZSessionManager {

	public void addToMap(String key, Object value);

	public Object getFromMap(String key);

	public void removeFromMap(String key);

	public Integer getBusinessId();

	public Integer getOutletId();

	public Integer getShopId();

	public Integer getTerminalId();

	public Zbusiness getBusiness();

	public void setBusiness(Zbusiness zbusiness);
	public void setOutlet(Outlet outlet);
	public void setShop(Shop shop);
	public void setTerminal(Terminal terminal);

	public MyUserDetail getLoggedInUserDetails();

	public Date getPosTime();

	public Xusers getPosUser();

	public Integer getPosShift();
}

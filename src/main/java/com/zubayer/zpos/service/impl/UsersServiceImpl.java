package com.zubayer.zpos.service.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.model.MyUserDetail;
import com.zubayer.zpos.repo.OutletRepo;
import com.zubayer.zpos.repo.ShopRepo;
import com.zubayer.zpos.repo.TerminalRepo;
import com.zubayer.zpos.repo.XusersRepo;
import com.zubayer.zpos.repo.ZbusinessRepo;
import com.zubayer.zpos.service.UsersService;

/**
 * @author Zubayer Ahamed
 * @since Mar 23, 2024 
 * CSE202101068
 */
@Service
public class UsersServiceImpl implements UserDetailsService, UsersService {

	@Autowired private XusersRepo usersRepo;
	@Autowired private ZbusinessRepo businessRepo;
	@Autowired private OutletRepo outletRepo;
	@Autowired private ShopRepo shopRepo;
	@Autowired private TerminalRepo terminalRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isBlank(username)) {
			throw new UsernameNotFoundException("User not found in the system");
		}

		Zbusiness business = businessRepo.findAll().getFirst();
		if(business == null || Boolean.FALSE.equals(business.getZactive())) {
			throw new UsernameNotFoundException("Business not active");
		}

		Optional<Xusers> userOp = usersRepo.findByZidAndXusername(business.getZid(), Integer.valueOf(username));
		if (!userOp.isPresent()) throw new UsernameNotFoundException("User not found");

		// if all ok, then get the other session data info
		Outlet outlet = outletRepo.findAll().getFirst();
		if(outlet == null) throw new UsernameNotFoundException("Outlet not configured");

 		Shop shop = shopRepo.findAll().getFirst();
 		if(shop == null) throw new UsernameNotFoundException("Shop not configured");
 
 		Terminal terminal = terminalRepo.findAll().getFirst();
 		if(terminal == null) throw new UsernameNotFoundException("Terminal not configured");

		return new MyUserDetail(userOp.get(), business, outlet, shop, terminal);
	}

}

package com.zubayer.zpos.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.zubayer.zpos.entity.Outlet;
import com.zubayer.zpos.entity.Shop;
import com.zubayer.zpos.entity.Terminal;
import com.zubayer.zpos.entity.Xusers;
import com.zubayer.zpos.entity.Zbusiness;
import com.zubayer.zpos.enums.POSRole;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Mar 23, 2024
 * CSE202101068
 */
@Data
public class MyUserDetail implements UserDetails {

	private static final long serialVersionUID = -8257425089705103719L;

	private Integer username;
	private String displayname;
	private String password;
	private Integer zid;
	private POSRole xrole;
	private Zbusiness zbusiness;
	private Outlet outlet;
	private Shop shop;
	private Terminal terminal;

	private boolean accountExpired;
	private boolean credentialExpired;
	private boolean accountLocked;
	private boolean enabled;
	private String roles;
	private List<GrantedAuthority> authorities;

	public MyUserDetail(Xusers user, Zbusiness business, Outlet outlet, Shop shop, Terminal terminal) {
		this.username = user.getXusername();
		this.displayname = user.getXdisplayname();
		this.password = user.getXpassword();
		this.zid = business.getZid();
		this.xrole = user.getXrole();
		this.zbusiness = business;
		this.outlet = outlet;
		this.shop = shop;
		this.terminal = terminal;

		this.accountExpired = false;
		this.credentialExpired = false;
		this.accountLocked = !Boolean.TRUE.equals(user.getZactive());
		this.enabled = Boolean.TRUE.equals(user.getZactive());
		this.roles = user.getXrole() == null ? POSRole.SYSTEM_ADMIN.name() : user.getXrole().name();
		this.authorities = Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public void setZbusiness(Zbusiness zbusiness) {
		this.zbusiness = zbusiness;
	}

	public Zbusiness getZbusiness() {
		return zbusiness;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public void setOutlet(Outlet outlet) {
		this.outlet = outlet;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getRoles() {
		return roles;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return username.toString();
	}

	public String getDisplayname() {
		return this.displayname;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !credentialExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public Integer getBusinessId() {
		return zid;
	}
}

package com.hypercube.evisa.approver.api.model;

import java.util.Arrays;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hypercube.evisa.approver.api.domain.EmployeeUserLogin;
import com.hypercube.evisa.common.api.util.CommonsUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeUserDetailsDTO implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2268154289380376226L;

	/**
	 * 
	 */
	private final String username;
	/**
	 * 
	 */
	private final String password;
	/**
	 * 
	 */
	private final Date accountExpired;
	/**
	 * 
	 */
	private final boolean active;
	/**
	 * 
	 */
	private final List<GrantedAuthority> authorities;
	/**
	 * 
	 */
	private final boolean accountLocked;

	/**
	 * @param user
	 */
	public EmployeeUserDetailsDTO(EmployeeUserLogin user) {
		log.trace("MyUserDetails parameterized constructor initiated");
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.accountExpired = user.getAccountExpiryDate();
		this.active = user.isAccountEnabled();
		this.authorities = Arrays.stream(user.getRoles().split(",")).map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		this.accountLocked = user.isAccountNonLocked();

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return CommonsUtil.cloneList(authorities);
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !(CommonsUtil.getTime(accountExpired) - CommonsUtil.getTime(Calendar.getInstance().getTime()) <= 0);
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return active;
	}

	public Date getAccountExpired() {
		return CommonsUtil.cloneDate(accountExpired);
	}

	public boolean isActive() {
		return active;
	}

	public boolean isAccountLocked() {
		return accountLocked;
	}

}

package com.fate.restful.et.service;

import java.util.List;

import com.fate.restful.et.domain.IdcUser;
/**
 * 
 * @author WangGang
 * 2016年05月31日16:36:24
 */
public interface IdcUserService {
	/**
	 * Retrieve All Users
	 * @return
	 */
	public List<IdcUser> findAllUsers();
	
	/**
	 * Retrieve An User By Id
	 * @return
	 */
	public IdcUser findById(long id);
	
	/**
	 * Retrieve an user by model
	 * @return
	 */
	public IdcUser findByModel(IdcUser u);
	
	/**
	 * Insert an user
	 * @param u
	 * @return
	 */
	public int add(IdcUser u);
	
	/**
	 * Update an user
	 * @param u
	 * @return
	 */
	public int modify(IdcUser u);
}

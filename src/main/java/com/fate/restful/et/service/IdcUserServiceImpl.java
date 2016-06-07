package com.fate.restful.et.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fate.restful.et.common.bean.ResultCode;
import com.fate.restful.et.domain.IdcUser;
import com.fate.restful.et.persistence.IdcUserMapper;
import com.github.pagehelper.PageHelper;
/**
 * 
 * @author WangGang
 * 2016年05月31日16:39:46
 */
@Service("idcUserService")
public class IdcUserServiceImpl implements IdcUserService {
	@Autowired
	private IdcUserMapper idcUserMapper;

	@Override
	public List<IdcUser> findAllUsers(int pageNum, int pageSize) throws Exception {
		if(pageNum == 0) pageNum = ResultCode.DEFAULT_PAGE_NUM;
		if(pageSize == 0) pageSize = ResultCode.DEFAULT_PAGE_SIZE;
		PageHelper.startPage(pageNum, pageSize);
		List<IdcUser> list = idcUserMapper.findAllUsers();
		return list;
	}

	@Override
	public IdcUser findById(long id) throws Exception {
		if(id <= 0){
			return null;
		}
		return idcUserMapper.findById(id);
	}

	@Override
	public IdcUser findByModel(IdcUser u) throws Exception {
		if(u == null){
			return null;
		}
		return idcUserMapper.findByModel(u);
	}

	@Override
	public int add(IdcUser u) throws Exception {
		if(u == null){
			return 0;
		}
		return idcUserMapper.add(u);
	}

	@Override
	public int modify(IdcUser u) throws Exception {
		if(u == null || u.getId() <= 0){
			return 0;
		}
		return idcUserMapper.modify(u);
	}

}

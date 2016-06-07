package com.fate.restful.et.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fate.restful.et.common.bean.ReqResult;
import com.fate.restful.et.common.bean.ResultCode;
import com.fate.restful.et.common.util.Exception2String;
import com.fate.restful.et.domain.IdcUser;
import com.fate.restful.et.service.IdcUserService;
import com.github.pagehelper.PageInfo;

/**
 * 
 * @author WangGang
 * 2016年05月31日16:45:56
 */
@RestController
public class IdcUserController {
	@Autowired
	private IdcUserService idcUserService;
	
	/**
	 * Retrieve All Users
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUsers", method=RequestMethod.POST)
	public ResponseEntity<ReqResult> ListAllUsers(HttpServletRequest request, @RequestBody IdcUser u){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		List<IdcUser> list = null;
		PageInfo<IdcUser> page = null;
		int pageNum = ResultCode.DEFAULT_PAGE_NUM;
		int pageSize = ResultCode.DEFAULT_PAGE_SIZE;
		if(u.getPagination() != null) {
			pageNum = u.getPagination().getCurrentPage();
			pageSize = u.getPagination().getPageSize();
		}
		try {
			list = idcUserService.findAllUsers(pageNum, pageSize);
			page = new PageInfo<IdcUser>(list);
			rst.setReturnObject(page);
			if (CollectionUtils.isEmpty(list)) {
				rst.setResultCode(ResultCode.HTTP_DB_NO_CONTENT);
				rst.setReturnMessage("没有数据");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.ListAllUsers():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
	}
	
	/**
	 * Retrieve An User By Id
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUser/{id}", method=RequestMethod.GET)
	public ResponseEntity<ReqResult> getById(@PathVariable("id") long id){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		if(id <= 0) {
			rst.setResultCode(ResultCode.HTTP_INVALID_PARAM);
			rst.setReturnMessage("无效id，不能小于等于0");
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} 
		IdcUser u = null;
		try {
			u = idcUserService.findById(id);
			rst.setReturnObject(u);
			if(u == null) {
				rst.setResultCode(ResultCode.HTTP_DB_NO_CONTENT);
				rst.setReturnMessage("没有数据");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.getById():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
	}
	
	/**
	 * Retrieve an user by model
	 * @param request
	 * @param u
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUser/model", method=RequestMethod.POST)
	public ResponseEntity<ReqResult> getByModel(HttpServletRequest request, @RequestBody IdcUser u){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		if(StringUtils.isNotEmpty(u.getUserCode()) || StringUtils.isNotEmpty(u.getUserName())
				|| StringUtils.isNotEmpty(u.getUserPwd()) || StringUtils.isNotEmpty(u.getUserPhone())
				|| StringUtils.isNotEmpty(u.getUserMail()) || StringUtils.isNotEmpty(u.getUserLevel())
				|| StringUtils.isNotEmpty(u.getUserGroup()) || StringUtils.isNotEmpty(u.getIsDeleted())) {
			rst.setResultCode(ResultCode.HTTP_INVALID_PARAM);
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		IdcUser ru = null;
		try {
			ru = idcUserService.findByModel(u);
			rst.setReturnObject(ru);
			if(ru == null) {
				rst.setResultCode(ResultCode.HTTP_DB_NO_CONTENT);
				rst.setReturnMessage("没有数据");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.getByModel():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		
	}
	
	/**
	 * Add an user
	 * @param request
	 * @param u
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUser/add", method=RequestMethod.POST)
	public ResponseEntity<ReqResult> add(HttpServletRequest request, @RequestBody IdcUser u){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		if (StringUtils.isEmpty(u.getUserName()) || StringUtils.isEmpty(u.getUserMail())) {
			rst.setResultCode(ResultCode.HTTP_INVALID_PARAM);
			rst.setReturnMessage("缺少必要参数");
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		IdcUser check = null;
		try {
			for (int i = 0; i < 2; i++) {
				check = new IdcUser();
				String[] duplicateStr = new String[]{"用户名", "邮箱"};
				if(i == 0) check.setUserName(u.getUserName());
				if(i == 1) check.setUserMail(u.getUserMail());
				if (idcUserService.findByModel(check) != null) {
					rst.setResultCode(ResultCode.HTTP_DB_DUPLICATE);
					rst.setReturnMessage(duplicateStr[i] + "重复");
					return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
				}
			}
			if(StringUtils.isEmpty(u.getUserCode())) {
				IdcUser u4Code = new IdcUser();
				String uCode = "UC_" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
				u4Code.setUserCode(uCode);
				while(idcUserService.findByModel(u4Code) != null){
					uCode = "UC_" + RandomStringUtils.randomAlphanumeric(8).toUpperCase();
					u4Code.setUserCode(uCode);
				}
				u.setUserCode(uCode);
			}
			if(StringUtils.isEmpty(u.getUserLevel())) u.setUserLevel("normal");
			if(StringUtils.isEmpty(u.getUserGroup())) u.setUserGroup("basic");
			int line = idcUserService.add(u);
			if (line <= 0) {
				rst.setResultCode(ResultCode.HTTP_EXECUTE_DB_ERROR);
				rst.setReturnMessage("新增失败！操作数据库受影响行数为：0");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.add():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
	}
	
	/**
	 * Modify an user
	 * @param request
	 * @param u
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUser/modify", method=RequestMethod.PUT)
	public ResponseEntity<ReqResult> modify(HttpServletRequest request, @RequestBody IdcUser u){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		if(StringUtils.isNotEmpty(u.getUserCode()) || StringUtils.isNotEmpty(u.getUserName())
				|| StringUtils.isNotEmpty(u.getUserPwd()) || StringUtils.isNotEmpty(u.getUserPhone())
				|| StringUtils.isNotEmpty(u.getUserMail()) || StringUtils.isNotEmpty(u.getUserLevel())
				|| StringUtils.isNotEmpty(u.getUserGroup()) || StringUtils.isNotEmpty(u.getIsDeleted())) {
			rst.setResultCode(ResultCode.HTTP_INVALID_PARAM);
			rst.setReturnMessage("参数无效");
			if(u.getId() <= 0) rst.setReturnMessage("参数无效，ID不能小于等于0");
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		int line = 0;
		try {
			line = idcUserService.modify(u);
			if(line <= 0) {
				rst.setResultCode(ResultCode.HTTP_EXECUTE_DB_ERROR);
				rst.setReturnMessage("修改失败！操作数据库受影响行数为：0");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
//			e.printStackTrace();
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.modify():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		
	}
	
	/**
	 * Delete Users
	 * @param request
	 * @param u
	 * @return
	 */
	@RequestMapping(value="/restApi/idcUser/delete", method=RequestMethod.PUT)
	public ResponseEntity<ReqResult> delete(HttpServletRequest request, @RequestBody List<Long> ids){
		ReqResult rst = new ReqResult();
		rst.setResultCode(ResultCode.HTTP_SUCCESS);
		rst.setReturnMessage("删除成功！");
		if(CollectionUtils.isEmpty(ids)) {
			rst.setResultCode(ResultCode.HTTP_OUT_OF_PARAM);
			rst.setReturnMessage("缺少必要参数：id");
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
		IdcUser u = null;
		try {
			int line = 0;
			for (Long id : ids) {
				u = new IdcUser();
				u.setId(id);
				u.setIsDeleted("y");
				line += idcUserService.modify(u);
			}
			if(line <= 0) {
				rst.setResultCode(ResultCode.HTTP_EXECUTE_DB_ERROR);
				rst.setReturnMessage("删除失败！操作数据库受影响行数为：0");
			}
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		} catch (Exception e) {
			rst.setResultCode(ResultCode.HTTP_SYSTEM_ERROR);
			rst.setReturnObject("IdcUserController.delete():" + e.toString());
			rst.setReturnMessage(Exception2String.getErrorInfoFromException(e));
			return new ResponseEntity<ReqResult>(rst, HttpStatus.OK);
		}
	}
}

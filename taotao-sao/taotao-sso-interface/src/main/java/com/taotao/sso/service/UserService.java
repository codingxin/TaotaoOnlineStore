package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
	// http://sso.taotao.com/user/check/{param}/{type}
	TaotaoResult checkData(String data, int type);

	TaotaoResult register(TbUser user);

	TaotaoResult login(String username, String password);

	// 通过token获取用户信息
	TaotaoResult getUserByToken(String token);

	// 安全退出
	TaotaoResult logout(String token);

}

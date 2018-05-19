package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 用户处理Controller SSO接口设计
 * 
 * @author Person
 *
 */
@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;

	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
		TaotaoResult result = userService.checkData(param, type);
		return result;
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult regitster(TbUser user) {
		TaotaoResult result = userService.register(user);
		return result;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、接收两个参数。
		// 2、调用Service进行登录。
		TaotaoResult result = userService.login(username, password);
		// 3、从返回结果中取token，写入cookie。Cookie要跨域。
		String token = result.getData().toString();
		if (result.getStatus() == 200) {
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}

		// 4、响应数据。Json数据。TaotaoResult，其中包含Token。
		return result;
	}

	@RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) {
		TaotaoResult result = userService.getUserByToken(token);    
        if(StringUtils.isNotBlank(callback)){  
            return callback+"("+JSON.toJSONString(result)+");";  
        }  
        return JSON.toJSONString(result);    
	}

	@RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token) {
		TaotaoResult result = userService.logout(token);
		return result;
	}

}

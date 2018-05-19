package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.util.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper usermapper;
	@Autowired
	private JedisClient JedisClient;
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;

	// http://sso.taotao.com/user/check/{param}/{type}
	@Override
	public TaotaoResult checkData(String data, int type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		// 1.判断用户名是否可用
		if (type == 1) {
			criteria.andUsernameEqualTo(data);
		}
		// 2.判断手机号是否可以使用
		else if (type == 2) {
			criteria.andPhoneEqualTo(data);
		} // 3.邮箱是否可以使用
		else if (type == 3) {
			criteria.andEmailEqualTo(data);
		} else {
			return TaotaoResult.build(400, "参数中包含非法数据");
		}
		// 执行查询
		List<TbUser> list = usermapper.selectByExample(example);
		if (list != null && list.size() > 0) {// 查询数据，返回false
			return TaotaoResult.ok(false);

		}
		// 数据可用
		return TaotaoResult.ok(true);

	}

	@Override
	public TaotaoResult register(TbUser user) {
		// 检查数据有效性
		if (StringUtils.isBlank(user.getUsername())) {
			return TaotaoResult.build(400, "用户名不能为空");
		}
		// 判断用户名是否重复
		TaotaoResult taotaoResult = checkData(user.getUsername(), 1);
		if (!(boolean) taotaoResult.getData()) {
			return TaotaoResult.build(400, "用户名重复");
		}
		// 判断密码是否为空
		if (StringUtils.isBlank(user.getPassword())) {
			return TaotaoResult.build(400, "密码不能为空");
		}
		// 是否重复校验
		if (StringUtils.isNotBlank(user.getPhone())) {
			taotaoResult = checkData(user.getPhone(), 2);
			if (!(boolean) taotaoResult.getData()) {
				return TaotaoResult.build(400, "电话号码重复");
			}

		}
		// 如果email不为空进行是否重复校验
		if (StringUtils.isNotBlank(user.getEmail())) {// 是否重复校验
			taotaoResult = checkData(user.getEmail(), 3);
			if (!(boolean) taotaoResult.getData()) {
				return TaotaoResult.build(400, "邮件重复");
			}
		}
		// 补全pojo的属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 密码要进行md5加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 插入数据
		usermapper.insert(user);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		// 判断用户名和密码是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = usermapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			// 返回登录失败
			return TaotaoResult.build(400, "用户名或者密码不正确");
		}
		TbUser user = list.get(0);
		// 密码要进行md5加密然后在校验
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			// 返回登录失败
			return TaotaoResult.build(400, "密码不正确");
		}
		// 2.生成token，使用uuid
		String token = UUID.randomUUID().toString();
		// 吧用户信息保存到redis，key就是token，value就是用户信息
		// 清空密码
		user.setPassword(null);
		JedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
		// 设置key的过期时间
		JedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 返回登录成功，其中要把token返回
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = JedisClient.get(USER_SESSION + ":" + token);
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "token已过期！");
		}
		// 如果我们直接把json返回的话，由于字符串中的"在redis中是有特殊意义的，因此
		// "会被转义，这不是我们想要的结果，我们想要的结果是不带转义符的字符串，因此我们
		// 需要先把json转换成对象，然后把对象返回。
		TbUser user = JSON.parseObject(json, TbUser.class);
		// 我们每访问一次该token，如果该token还没过期，我们便需要更新token的值，再把token恢复
		// 到原来的最大值
		JedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
		// 返回结果
		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		JedisClient.expire(USER_SESSION + ":" + token, 0);
		return TaotaoResult.ok();

	}

}

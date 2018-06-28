package cn.et.conf;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.et.mapper.UserMapper;

/**
 * 自定义realm读取数据库
 */
@Component
public class MyRealm extends AuthorizingRealm{

	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 授权，当前用户拥有哪些角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//获取用户名
		String userName = principals.getPrimaryPrincipal().toString();
		
		//返回当前用户所有的角色和权限
		SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
		
		List<String> queryRole = userMapper.queryPerms(userName);
		sai.addRoles(queryRole);
		
		List<String> queryPerms = userMapper.queryPerms(userName);
		sai.addStringPermissions(queryPerms);
		
		return sai;
	}
	
	static String REALM_NAME = "myrealm";
	
	/**
	 * 认证，登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//用户登录输入的用户名和密码
		UsernamePasswordToken upt = (UsernamePasswordToken)token;
		
		String inputUserName = upt.getUsername();
		String inputPassword = new String(upt.getPassword());
		String inputPasswordMd5 = new Md5Hash(inputPassword).toString();
		
		Map map = userMapper.queryUser(inputUserName, inputPasswordMd5);
		
		if(map == null) {
			throw new AuthenticationException();
		}	
		
		return new SimpleAccount(inputUserName,inputPassword,REALM_NAME);
	}

}

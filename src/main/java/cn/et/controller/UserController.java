package cn.et.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

	@RequestMapping("/lg")
	public String login(String userName,String password) {
		//获取当前用户，当前用户是一个内存中分配的虚拟用户
		Subject currentUser = SecurityUtils.getSubject();
		
		if(currentUser.isAuthenticated()) {
			currentUser.logout();
		}
		
		//判断是否登录过，默认false
		if(!currentUser.isAuthenticated()) {
			//输入命令，用户名和密码
			UsernamePasswordToken upt = new UsernamePasswordToken(userName,password);
			
			try {
				currentUser.login(upt);
				
				Session session = currentUser.getSession();
				session.setAttribute("lostedMoney", 1000);
				
				return "/suc.jsp";
			}catch(UnknownAccountException uae) {
				return "/login.jsp";
			}catch(IncorrectCredentialsException ice) {
				return "/login.jsp";
			}
		}
		
		return "/suc.jsp";
	}
	
}

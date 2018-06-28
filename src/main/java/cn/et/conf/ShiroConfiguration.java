package cn.et.conf;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class ShiroConfiguration {

	/**
	 * 注入shiro在web.xml中的过滤器
	 */
	@Bean
	public FilterRegistrationBean webShiroFilter() {
		
		FilterRegistrationBean frb = new FilterRegistrationBean();
		frb.setFilter(new DelegatingFilterProxy());
		
		//filter的名称和ShiroFilterFactoryBean的bean名称一致
		frb.setName("shiroFilter");
		
		LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
		linkedHashSet.add("/*");
		
		frb.setUrlPatterns(linkedHashSet);
		
		return frb;
	}
	
	@Bean(name="securityManager")
	public DefaultWebSecurityManager securityManager(@Autowired MyRealm myRealm) {
		
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(myRealm);
		
		return defaultWebSecurityManager;
	}
	
	@Bean(name="shiroFilter")
	public ShiroFilterFactoryBean shiroFilter1(@Autowired DefaultWebSecurityManager securityManager) {
		
		ShiroFilterFactoryBean sffb = new ShiroFilterFactoryBean();
		sffb.setSecurityManager(securityManager);
		
		//添加过滤器
		Map map = new HashMap();
		map.put("/login.jsp", "anon");
		map.put("/suc.jsp","authc");
		
		sffb.setFilterChainDefinitionMap(map);
		
		return sffb;
	}
	
	@Bean(name="lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		
		return new LifecycleBeanPostProcessor();
	}
	
}


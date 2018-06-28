package cn.et.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM t_userinfo WHERE username=#{userName}  AND password=#{password}")
	public Map queryUser(@Param("userName")String userName,@Param("password")String password);
	
	
	@Select("SELECT r.rolename FROM t_userinfo u INNER JOIN t_user_role ur ON u.userid=ur.userid\r\n" + 
			" INNER JOIN t_role r ON ur.roleid=r.roleid WHERE u.username=#{userName}")
	public List<String> queryRole(@Param("userName")String userName);
	
	
	@Select("SELECT pm.permstr FROM t_userinfo u INNER JOIN t_user_role ur ON u.userid=ur.userid\r\n" + 
    		"                           INNER JOIN t_role r ON ur.roleid=r.roleid \r\n" + 
    		"                           INNER JOIN t_role_perm p ON r.roleid=p.roleid\r\n" + 
    		"                           INNER JOIN t_perm pm ON p.permid=pm.permid\r\n" + 
    		"                           WHERE u.username=#{userName}")
	public List<String> queryPerms(@Param("userName")String userName);

}

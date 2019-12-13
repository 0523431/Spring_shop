package dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import logic.User;



public class UserDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;
	
	// spring-mvc.xml을 보면 알수 있음 (p:dataSource-ref="dataSource")
	// Connection 객체가 주입될거야
	// 즉, dataSource는
	// 			class="org.springframework.jdbc.datasource.DriverManagerDataSource" 객체 주입
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate에 dataSource가 주입되면서 DB에 연결이 된거야
	}

	// ShopService.java에서 ..
	// User user객체를 파라미터값으로 사용
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		
		String sql = "insert into useraccount "
						+ " (userid,username,password,birthday,phoneno,postcode,address,email) "
						+ " values (:userid,:username,:password,:birthday,:phoneno,:postcode,:address,:email) ";
		template.update(sql, param);
		// insert를 하고 UserEntryController로 넘어감
	}
	


}

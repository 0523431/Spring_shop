package dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.User;

//@Component + dao 기능 (객체화가 됨)
@Repository
public class UserDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;
	
	// mapper는 item 클래스
	/* 	옛날 방식
		Item item = new Item();
			 item.setId(rs.getString("id"));
			 item.setName(rs.getString("name"));
			 ...
		이 작업을 RowMapper가 대신 작업해줌	*/
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String, Object> param = new HashMap<>();
	
	// 주입해
	// 누구를? DataSource ====> spring-db.xml에서 확인 가능
	// spring-mvc.xml을 보면 알수 있음 (p:dataSource-ref="dataSource")
	// Connection 객체가 주입될거야
	// 즉, dataSource는
	// 			class="org.springframework.jdbc.datasource.DriverManagerDataSource" 객체 주입
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate에 dataSource가 주입되면서 DB에 연결이 된거야
	}
	
	
	public void userInsert(User user) {
		String sql = "insert into useraccount "
						+ " (userid,password,username,phoneno,postcode,address,email,birthday) "
						+ " values (:userid,:password,:username,:phoneno,:postcode,:address,:email,:birthday) ";
		/*  item에 있는 프로퍼티property를 파라미터parameter로 대체해줌
			그래서,
			param.put("id", item.getId());
			...
			이런 과정이 필요없고 한번에 가능해짐   */
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		template.update(sql, proparam);
	}


	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
		// mapper의 정의가 User클래스이기때문에, 리턴타입이 User가 될 수 있는거야
		// private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	}
}

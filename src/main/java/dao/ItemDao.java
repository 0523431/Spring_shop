package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource; // db Connection 객체

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;
	
	/* 	옛날 방식
		Item item = new Item();
		item.setId(rs.getString("id"));
		item.setName(rs.getString("name"));
		...
		이 작업을 RowMapper가 대신 작업해줌	*/
	RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	
	// spring-mvc.xml을 보면 알수 있음 (p:dataSource-ref="dataSource")
	// Connection 객체가 주입될거야
	// 즉, dataSource는
	// 			class="org.springframework.jdbc.datasource.DriverManagerDataSource" 객체 주입
	public void setDataSource(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate에 dataSource가 주입되면서 DB에 연결이 된거야
	}
	
	public List<Item> list() {
		return template.query("select * from item", mapper);
	}

	public Item selectOne(Integer id) {
		Map<String, Integer> param = new HashMap<>();
		param.put("id", id); // :id와 값이 같아야 함
		return template.queryForObject("select * from item where id=:id", param, mapper);
	}
}

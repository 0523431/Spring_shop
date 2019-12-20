package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource; // db Connection 객체

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;

//@Component + dao 기능
@Repository
public class ItemDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;
	
	// mapper는 item 클래스
	/* 	옛날 방식
		Item item = new Item();
		item.setId(rs.getString("id"));
		item.setName(rs.getString("name"));
		...
		이 작업을 RowMapper가 대신 작업해줌	*/
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
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
	
	// mapper : property 이름
	public List<Item> list() {
		return template.query("select * from item order by id", mapper);
	}

	public void insert(Item item) {
		param.clear();
		/* Integer.class : 실행하려는 query의 결과가 int */
		int id = template.queryForObject(" select ifnull(max(id), 0) from item ", param, Integer.class);
		item.setId(++id+""); // "": String type으로 형변화 시켜줌
		String sql = "insert into item "
						+ " (id,name,price,description,pictureUrl) "
						+ " values (:id,:name,:price,:description,:pictureUrl) ";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql, proparam);
	}

	public Item itemInfo(String id) { // 여기서 내가 Integer로 한 건 => 지역변수(여기서만 해당)
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		map.put("id", id);
//		return template.queryForObject("select * from item where id=:id", map, mapper);
		
		param.clear();
		param.put("selectid", id);
		
		return template.queryForObject("select * from item where id=:selectid", param, mapper);
		// mapper의 정의가 Item 클래스이기때문에, 리턴타입이 Item이 될 수 있는거야
		// private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	}

	public void update(Item item) {
		String sql = "update item set name=:name, price=:price, "
						+ " description=:description, pictureUrl=:pictureUrl "
						+ " where id=:id ";
		/*
			item에 있는 프로퍼티property를 파라미터parameter로 대체해줌
			그래서,
			param.put("id", item.getId());
			...
			이런 과정이 필요없고 한번에 가능해짐
		*/
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql, proparam);
	}

	public void itemDelete(String id) {
		param.clear();
		param.put("id", id);
		
		template.update("delete from item where id=:id", param);
	}
}

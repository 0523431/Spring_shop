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
	
	private NamedParameterJdbcTemplate template;
	// mapper는 item 클래스
	private RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	private Map<String, Object> param = new HashMap<>();
	
	// 주입해
	// 누구를? DataSource ====> spring-db.xml에서 확인 가능
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	
	// mapper : property 이름
	public List<Item> list() {
		return template.query("select * from item order by id", mapper);
	}

	public void insert(Item item) {
		param.clear();
		/*
			Integer.class : 실행하려는 query의 결과가 int
		 */
		int id = template.queryForObject(" select ifnull(max(id), 0) from item ", param, Integer.class);
		item.setId(++id+""); // "": String type으로 형변화 시켜줌
		String sql = "insert into item "
						+ " (id,name,price,description,pictureUrl) "
						+ " values (:id,:name,:price,:description,:pictureUrl) ";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(item);
		template.update(sql, proparam);
	}
}

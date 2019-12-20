package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;
import logic.Sale;

//@Component + dao 기능
@Repository
public class SaleDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;

	// mapper는 item 클래스
	/* 	옛날 방식
		Item item = new Item();
		item.setId(rs.getString("id"));
		item.setName(rs.getString("name"));
		...
		이 작업을 RowMapper가 대신 작업해줌	*/
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	private Map<String, Object> param = new HashMap<>();

	// 주입해
	// 누구를? DataSource ====> spring-db.xml에서 확인 가능
	// spring-mvc.xml을 보면 알수 있음 (p:dataSource-ref="dataSource")
	// Connection 객체가 주입될거야
	// 즉, dataSource는
	// class="org.springframework.jdbc.datasource.DriverManagerDataSource" 객체 주입
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate에 dataSource가 주입되면서 DB에 연결이 된거야
	}
	
	public int getMaxSaleId() {
		String sql = "select ifnull(max(saleid), 0) from sale";
		
		// max는 sale테이블에 저장된 saleid값의 최대값
		Integer max = template.queryForObject(sql, param, Integer.class);
		return max +1;
	}
	
	public void insert(Sale sale) {
		String sql = "insert into sale (saleid,userid,updatetime) "
						+ " values (:saleid,:userid,:updatetime) ";
		/*
		item에 있는 프로퍼티property를 파라미터parameter로 대체해줌
		그래서,
		param.put("id", item.getId());
		...
		이런 과정이 필요없고 한번에 가능해짐  */
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(sale);
		template.update(sql, proparam); // saleid 1건을 등록하게 됨
	}

	public List<Sale> list(String id) {
		String sql = "select * from sale where userid = :userid";
		param.clear();
		param.put("userid", id);
		return template.query(sql, param, mapper);
	}
}

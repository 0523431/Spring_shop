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

import logic.SaleItem;

//@Component + dao 기능
@Repository
public class SaleItemDao {

	private NamedParameterJdbcTemplate template;
	private RowMapper<SaleItem> mapper = new BeanPropertyRowMapper<SaleItem>(SaleItem.class);
	private Map<String, Object> param = new HashMap<>();

	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
		// NamedParameterJdbcTemplate에 dataSource가 주입되면서 DB에 연결이 된거야
	}
	
	public void insert(SaleItem si) {
		String sql = "insert into saleitem "
						+ " (saleid,saleitemid,itemid,quantity) "
						+ " values (:saleid,:saleitemid,:itemid,:quantity) ";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(si);
		template.update(sql, proparam);
	}

	public List<SaleItem> list(int saleid) {
		String sql = "select * from saleitem "
						+ " where saleid = :saleid ";
		param.clear();
		param.put("saleid", saleid);
		return template.query(sql, param, mapper);
	}
}

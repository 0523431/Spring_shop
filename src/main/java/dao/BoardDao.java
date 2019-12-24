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

import logic.Board;

//@Component + dao 기능 (객체화가 됨)
@Repository
public class BoardDao {
	// spring jdbc 프레임워크(spring 자체적으로)
	private NamedParameterJdbcTemplate template;
	
	// mapper는 Board 클래스
	/* 	옛날 방식
		Board b = new Board();
			  b.setName(rs.getString("name"));
			  ...
		이 작업을 RowMapper가 대신 작업해줌	*/
	private RowMapper<Board> mapper = new BeanPropertyRowMapper<Board>(Board.class);
	private Map<String, Object> param = new HashMap<>();
	
	// file1과 fileurl 매칭이 되야함
	// 미리 필요한 공통 sql을 작성한 것
	private String boardcolumn = "select num,name,pass,title,content,file1 as fileurl, "
								+ " regdate,readcnt,grp,grplevel,grpstep from board";
	
	
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

	public int count(String searchtype, String searchcontent) {
		String sql = "select count(*) from board";
		if(searchtype !=null) {
			sql += " where " + searchtype + " like :searchcontent ";
		}
		param.clear();
		param.put("searchcontent", "%"+searchcontent+"%");
		return template.queryForObject(sql, param, Integer.class);		
	}

	public List<Board> list(Integer pageNum, int limit, String searchtype, String searchcontent) {
		String sql = boardcolumn;
		param.clear();
		if(searchtype != null) {
			sql += " where "+ searchtype + " like '%" + searchcontent + "%' ";
			param.put("searchcontent", searchcontent);
		}	
		sql += " order by grp desc, grpstep limit :startrow, :limit ";
		param.put("startrow", (pageNum -1) *limit);
		param.put("limit", limit);
		return template.query(sql,param,mapper);
	}
	
	public int maxnum() {
		return template.queryForObject("select ifnull(max(num),0) from board", param, Integer.class);
	}

	public void insert(Board board) {
		String sql = "insert into board "
				+ " (num,name,pass,title,content,file1,regdate,readcnt,grp,grplevel,grpstep) "
				+ " values (:num,:name,:pass,:title,:content,:fileurl,now(),0,:grp,:grplevel,:grpstep) ";

		SqlParameterSource proparam = new BeanPropertySqlParameterSource(board);
		template.update(sql, proparam);	
	}

	// 조회수 증가
	public void readcntadd(Integer num) {
		// param값 잊지말고 집어넣자
		param.clear();
		param.put("num", num);
		template.update("update board set readcnt=readcnt +1 where num =:num", param);
	}

	public Board selectOne(Integer num) {
		String sql = boardcolumn + " where num = :num ";
		param.clear();
		param.put("num", num);
		return template.queryForObject(sql, param, mapper);
	}
	
	// 기존글들의 순서를 밀어냄
	public void grpstepAdd(int grp, int grpstep) {
		param.clear();
		param.put("grp", grp);
		param.put("grpstep", grpstep);
		template.update("update board set grpstep=grpstep+1 where grp=:grp and grpstep > :grpstep", param);
	}

	public void update(Board board) {
		String sql = "update board set "
						+ " name=:name,title=:title,content=:content,file1=:fileurl "
						+ " where num=:num ";
		
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(board);
		template.update(sql, proparam);	
	}

	public void delete(int num) {
		String sql = "delete from board where num=:num";
		param.clear();
		param.put("num", num);
		template.update(sql, param);		
	}

}

package jp.co.sample.emp_management.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.sample.emp_management.domain.Employee;

/**
 * employeesテーブルを操作するリポジトリ.
 * 
 * @author igamasayuki
 * 
 */
@Repository
public class EmployeeRepository {
	
	/** テーブル名 */
	private static final String TABLE_NAME = "employees";

	/** すべてのカラム */
	private static final String ALL_COLMUN 
	= " id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count ";
	
	/**
	 * Employeeオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
		Employee employee = new Employee();
		employee.setId(rs.getInt("id"));
		employee.setName(rs.getString("name"));
		employee.setImage(rs.getString("image"));
		employee.setGender(rs.getString("gender"));
		employee.setHireDate(rs.getDate("hire_date"));
		employee.setMailAddress(rs.getString("mail_address"));
		employee.setZipCode(rs.getString("zip_code"));
		employee.setAddress(rs.getString("address"));
		employee.setTelephone(rs.getString("telephone"));
		employee.setSalary(rs.getInt("salary"));
		employee.setCharacteristics(rs.getString("characteristics"));
		employee.setDependentsCount(rs.getInt("dependents_count"));
		return employee;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	/**
	 * 従業員一覧情報を入社日順で取得します.
	 * 
	 * @return 全従業員一覧 従業員が存在しない場合はサイズ0件の従業員一覧を返します
	 */
	public List<Employee> findAll() {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC";

		List<Employee> developmentList = template.query(sql, EMPLOYEE_ROW_MAPPER);

		return developmentList;
	}
	
	/**
	 * 従業員名前一覧を入社日順で取得します.
	 * 
	 * @return 全従業員名前一覧
	 */
	public List<String> findAllName(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT name FROM ");
		sql.append(TABLE_NAME);
		sql.append(" ORDER BY hire_date DESC ");
		SqlParameterSource param = new MapSqlParameterSource();
		return template.queryForList(sql.toString(), param ,String.class);
	}
	
	/**
	 * 従業員一覧を曖昧検索する.
	 * 
	 * @param name : 検索する名前
	 * @return 検索された従業員一覧 存在しない場合はサイズ0件の従業員情報一覧を返す
	 */
	public List<Employee> findByName(String name){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");	sql.append(ALL_COLMUN);
		sql.append(" FROM ");	sql.append(TABLE_NAME);
		sql.append(" WHERE name LIKE :name ORDER BY hire_date DESC ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List<Employee> employeeList = template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER); 
		return employeeList;
	}
	
	/**
	 * メールアドレスを指定して従業員情報を検索する.
	 * 
	 * @param mailAddress : 検索するメールアドレス
	 * @return 検索された従業員情報
	 */
	public Employee findByMailAddress(String mailAddress) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");	sql.append(ALL_COLMUN);
		sql.append(" FROM ");	sql.append(TABLE_NAME);
		sql.append(" WHERE mail_address=:mailAddress ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
		List<Employee> employeeList = template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
		if(employeeList.size() != 0) {
			return employeeList.get(0);
		}else {			
			return null;
		}
	}
	
	/**
	 * IDの最大値を検索する.
	 * 
	 * @return DBに存在するidの最大値 or 0
	 */
	public int findByMaxId() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT max(id) FROM ");
		sql.append(TABLE_NAME);
		SqlParameterSource param = new MapSqlParameterSource();
		Integer id = template.queryForObject(sql.toString(), param, Integer.class);
		if( id == null){
			return 0;
		}else {
			return id;
		}
	}

	/**
	 * 主キーから従業員情報を取得します.
	 * 
	 * @param id 検索したい従業員ID
	 * @return 検索された従業員情報
	 * @exception 従業員が存在しない場合は例外を発生します
	 */
	public Employee load(Integer id) {
		String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";

		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);

		Employee development = template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);

		return development;
	}

	/**
	 * 従業員情報を変更します.
	 */
	public void update(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);

		String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
		template.update(updateSql, param);
	}
	
	
	/**
	 * 従業員情報を追加する.<br>
	 * 従業員情報を追加し追加したidを取得しセットして返す
	 * 
	 * @param employee : 追加する従業員情報
	 * @return 従業員情報
	 */
	public void insert(Employee employee) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ");	sql.append(TABLE_NAME);
		sql.append("(");	sql.append(ALL_COLMUN);
		sql.append(") values(:id,:name,:image,:gender,:hireDate,:mailAddress,:zipCode,:address,:telephone,:salary,:characteristics,:dependentsCount)");
		template.update(sql.toString(), param);
	}
	
}

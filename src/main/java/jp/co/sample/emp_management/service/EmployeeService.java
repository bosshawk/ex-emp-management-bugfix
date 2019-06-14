package jp.co.sample.emp_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return　従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}
	
	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}
	
	/**
	 * 全従業員名を検索する.
	 * 
	 * @return 全従業員名一覧
	 */
	public List<String> showNameList(){
		return employeeRepository.findAllName();
	}
	
	/**
	 * 名前を指定して従業員一覧を取得.
	 * 
	 * @param name : 検索する名前
	 * @return 検索さてた従業員一覧
	 */
	public List<Employee> searchByName(String name){
		return employeeRepository.findByName(name);
	}
	
	/**
	 * メールアドレスがDBに登録されているか調べる.
	 * 
	 * @param mailAddress 検索するメールアドレス
	 * @return ある場合:true/ない場合:false
	 */
	public Boolean checkByMailAddress(String mailAddress) {
		if(employeeRepository.findByMailAddress(mailAddress) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * IDの最大値を検索する.
	 * 
	 * @return IDの最大値
	 */
	public int searchByMaxId() {
		return employeeRepository.findByMaxId();
	}
	
	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee　更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}
	
	/**
	 * 従業員情報を追加する.
	 * 
	 * @param employee 追加する従業員情報
	 */
	public Employee insert(Employee employee) {
		int id = employeeRepository.findByMaxId();
		employee.setId(id+1);
		employeeRepository.insert(employee);
		return employee;
	}
}

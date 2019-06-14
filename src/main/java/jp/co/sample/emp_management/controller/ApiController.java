package jp.co.sample.emp_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sample.emp_management.service.EmployeeService;
import net.arnx.jsonic.JSON;

/**
 * Ajaxのコントローラー.
 * 
 * @author taka
 *
 */
@Controller
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * 全従業員名一覧をJSONで返す.
	 * 
	 * @return JSONの名前一覧
	 */
	@ResponseBody
	@RequestMapping(value="/getallname")
	public String getAllName(){
		
		List<String> nameList = employeeService.showNameList();
		return JSON.encode(nameList);
	}
	
	

}

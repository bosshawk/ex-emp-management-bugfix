package jp.co.sample.emp_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sample.emp_management.domain.Employee;
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
	
	@ResponseBody
	@RequestMapping(value="/getallname")
	public String getAllName(){
		
		List<Employee> employeeList = employeeService.showList();
		List<String> nameList = new ArrayList<>();
		for(Employee employee:employeeList) {
			nameList.add(employee.getName());
		}
		System.out.println(nameList);
		return JSON.encode(nameList);
	}
	
	

}

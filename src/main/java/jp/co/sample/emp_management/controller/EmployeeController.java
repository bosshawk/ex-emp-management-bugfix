package jp.co.sample.emp_management.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.SearchNameForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/**
	 * 使用する検索名前フォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public SearchNameForm setUpSearchForm() {
		return new SearchNameForm();
	}

	/**
	 * 使用する追加する従業員情報フォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertEmployeeForm setUpInsertForm() {
		return new InsertEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model) {
		List<Employee> employeeList = employeeService.showList();
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を名前検索する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧を名前で検索する
	 * 
	 * @param form   : リクエストパラメータで送られてくる検索する名前
	 * @param result : エラー情報
	 * @param model  : モデル
	 * @return : 従業員一覧画面
	 */
	@RequestMapping("/search")
	public String searchByName(@Validated SearchNameForm form, BindingResult result, Model model) {

		if ("".equals(form.getName())) {
			model.addAttribute("searchMessage", "全件検索しました");
			return showList(model);
		}
		
		List<Employee> employeeList = employeeService.searchByName(form.getName());
		int searchNum = employeeList.size();
		
		if (searchNum == 0) {
			model.addAttribute("searchMessage", "検索された結果0件です。全従業員一覧を表示します。");
			return showList(model);
		}
		
		model.addAttribute("searchMessage", searchNum + "件検索されました");
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員追加画面に遷移する
	/////////////////////////////////////////////////////
	/**
	 * 従業員情報追加画面に遷移する.
	 * 
	 * @return 従業員情報追加画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		Path path = Paths.get("src/main/resources/static/img");
		System.out.println(path.toAbsolutePath().toString());
		return "employee/insert";
	}
	
	/////////////////////////////////////////////////////
	// ユースケース：従業員情報を追加する
	/////////////////////////////////////////////////////
	/**
	 * 従業員情報を追加する.
	 * 
	 * @param form 追加する従業員情報
	 * @param result エラー 
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/insert")
	public synchronized String insert(
			@Validated InsertEmployeeForm form,
			BindingResult result
			) {
		
		if ( form.getImage().isEmpty() ) {
			result.rejectValue("image", null, "写真を選択して下さい");
		}
		Boolean hasMailAddress = employeeService.checkByMailAddress(form.getMailAddress());
		if (hasMailAddress) {
			result.rejectValue("mailAddress", null, "このメールアドレスは既に登録されています");
		}		
		if ( result.hasErrors() ) {
			return toInsert();
		}
		
		// 拡張子がpngかjpgのもの以外は保存しないためエラーを返す
		String extention = form.getExtentionImage();
		if( !".png".equals(extention) && !".jpg".equals(extention) ) {
			result.rejectValue("image", null, "pngかjpg形式を選択して下さい");
			return toInsert();
		}
		
		
		String imageName = saveImage(form.getImage(),extention);
		int id = employeeService.searchByMaxId() + 1;
		
		Employee employee = form.copyEmployee();
		employee.setImage(imageName);
		employee.setId(id);
		
		employeeService.insert(employee);

		return "redirect:/employee/showList";
	}

	
	/**
	 * 写真をサーバーに保存する.
	 * 
	 * @param image 保存する写真
	 * @param extention 拡張子
	 * @return 保存されたimageの名前
	 */
	private static String saveImage(MultipartFile image,String extention) {
		
		int dotPoint = image.getOriginalFilename().lastIndexOf(".");
		
		StringBuffer filename = new StringBuffer();
		filename.append(image.getOriginalFilename());
		filename.delete(dotPoint, filename.length());
		filename.append("_");
		filename.append(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()));
		filename.append(extention);
		Path uploadfile = Paths.get("src/main/resources/static/img/" + filename.toString() );
		try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
			byte[] bytes = image.getBytes();
			os.write(bytes);
		} catch (IOException ex) {
			System.err.println(ex);
		}
		
		return filename.toString();

	}
	
	

}

package jp.co.sample.emp_management.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Not Fount(404)などのエラーを処理するコントローラ.
 * 
 * @author taka
 *
 */
@Controller
public class NotFoundController implements ErrorController {

	private static final String PATH = "/error";
	
	/**
	 * not Fount(404)エラーに遷移.
	 */
	@Override
	@RequestMapping(PATH)
	public String getErrorPath() {
		return "common/notFound";
	}

}

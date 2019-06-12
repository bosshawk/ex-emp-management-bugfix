package jp.co.sample.emp_management.form;

/**
 * 名前を検索するフォーム
 * 
 * @author taka
 *
 */
public class SearchNameForm {
	
	/** 名前 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SearchNameForm [name=" + name + "]";
	}
	
	

}

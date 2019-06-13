package jp.co.sample.emp_management.form;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import jp.co.sample.emp_management.domain.Employee;

/**
 * 従業員情報追加時に使用するフォーム.
 * 
 * @author taka
 *
 */
public class InsertEmployeeForm {

	/** 従業員名 */
	@NotBlank(message="名前を入力して下さい")
	private String name;
	/** 画像 */
	private MultipartFile image;
	/** 性別 */
	@NotEmpty(message="性別を選択して下さい")
	private String gender;
	/** 入社日 */
	@NotEmpty(message="日付を入力して下さい")
	private String hireDate;
	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メードアドレスの形式で入力してください")
	private String mailAddress;
	/** 郵便番号 */
	@Pattern(regexp = "^\\d{3}\\-?\\d{4}$", message = "郵便番号を入力したください")
	private String zipCode;
	/** 住所 */
	@NotBlank(message = "住所を入力してください")
	private String address;
	/** 電話番号 */
	@NotBlank(message = "電話番号を入力してください")
	private String telephone;
	/** 給料 */
	@NotBlank(message = "給料を入力してください")
	@Pattern(regexp = "[0-9]*", message = "数字を入力してください")
	private String salary;
	/** 特性 */
	@NotBlank(message = "特性を入力してください")
	private String characteristics;
	/** 扶養人数 */
	@NotBlank(message = "扶養人数を入力してください")
	@Pattern(regexp = "[0-9]*", message = "数字を入力してください")
	private String dependentsCount;

	/**
	 * フォームの内容を従業員情報にコピーする.
	 * 
	 * @return コピーされた従業員情報
	 */
	public Employee copyEmployee() {
		Employee employee = new Employee();
		BeanUtils.copyProperties(this, employee);
		employee.setImage(getStrImage());
		employee.setHireDate(getDateHireDate());
		employee.setZipCode(getHyphenZipCode());
		employee.setSalary(getIntSalary());
		employee.setTelephone(getHyphenTelephone());
		employee.setDependentsCount(getIntDependentsCount());
		return employee;
	}
	
	/**
	 * imageの名前を返す.
	 * 
	 * @return String型のimage
	 */
	public String getStrImage() {
		return this.image.getOriginalFilename();
	}

	/**
	 * Date型の入社日を返す.
	 * 
	 * @return Date型のhireDate
	 */
	public Date getDateHireDate() {
		return java.sql.Date.valueOf(this.hireDate);
	}
	
	/**
	 * 郵便番号にハイフンをつける.
	 * 
	 * @return zipCodeにハイフンをつける
	 */
	public String getHyphenZipCode() {
		StringBuffer zipCode = new StringBuffer(this.zipCode);
		int hyphenIndex = zipCode.indexOf("-");
		if(hyphenIndex == -1) {
			zipCode.insert(3, "-");
			return zipCode.toString();
		}else {
			return this.zipCode;
		}
	}
	
	/**
	 * 
	 * 
	 * @return telephoneにハイフンをつける
	 */
	public String getHyphenTelephone() {
		final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber phoneNumber = PHONE_NUMBER_UTIL.parse(this.telephone, "JP");
			String telephone = PHONE_NUMBER_UTIL.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);	
			return telephone;
		}catch(NumberParseException e) {
			return this.telephone;
		}
	}
	
	/**
	 * int型の給料を返す.
	 * 
	 * @return int型のsalary
	 */
	public int getIntSalary() {
		return Integer.parseInt(this.salary);
	}

	/**
	 * int型の扶養人数を返す.
	 * 
	 * @return int型のdepandentsCount
	 */
	public int getIntDependentsCount() {
		return Integer.parseInt(this.dependentsCount);
	}
	
	/**
	 * 写真のファイルの拡張子を返す.
	 * 
	 * @return imageファイルの拡張子
	 */
	public String getExtentionImage() {
		int dot = image.getOriginalFilename().lastIndexOf(".");
		String extention = "";
		if (dot > 0) {
			extention = image.getOriginalFilename().substring(dot).toLowerCase();
		}
		return extention;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(String characteristics) {
		this.characteristics = characteristics;
	}

	public String getDependentsCount() {
		return dependentsCount;
	}

	public void setDependentsCount(String dependentsCount) {
		this.dependentsCount = dependentsCount;
	}

	@Override
	public String toString() {
		return "InsertEmployeeForm [name=" + name + ", image=" + image + ", gender=" + gender + ", hireDate=" + hireDate
				+ ", mailAddress=" + mailAddress + ", zipCode=" + zipCode + ", address=" + address + ", telephone="
				+ telephone + ", salary=" + salary + ", characteristics=" + characteristics + ", dependentsCount="
				+ dependentsCount + "]";
	}

}

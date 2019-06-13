package jp.co.sample.emp_management.util;

import java.util.Date;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * 変換系の有用メソッド.
 * 
 * @author taka
 *
 */
public class ConvertUtils {
	
	
	/**
	 * String型の日付をDate型に変換する.
	 * 
	 * @param date 日付
	 * @return Date型の日付
	 */
	public static Date getSqlDateHireDate(String date) {
		return java.sql.Date.valueOf(date);
	}
	
	
	/**
	 * 郵便番号にハイフンをつける.
	 * 
	 * @return zipCodeにハイフンをつける
	 */
	public static String getHyphenZipCode(String zipCode) {
		StringBuffer hyphenZipCode = new StringBuffer(zipCode);
		int hyphenIndex = hyphenZipCode.indexOf("-");
		if(hyphenIndex == -1) {
			hyphenZipCode.insert(3, "-");
			return hyphenZipCode.toString();
		}else {
			return zipCode;
		}
	}
	
	
	/**
	 * 電話番号にハイフンをつける.
	 * 
	 * @param telephone 電話番号
	 * @return ハイフンをつけた電話番号
	 */
	public static String getHypehnTelephone(String telephone) {
		final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber phoneNumber = PHONE_NUMBER_UTIL.parse(telephone, "JP");
			return PHONE_NUMBER_UTIL.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);	
		}catch(NumberParseException e) {
			return telephone;
		}
	}
	
	
	
	
	

}

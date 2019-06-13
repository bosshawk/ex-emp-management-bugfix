package jp.co.sample.emp_management.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * ファイルの有用メソッド
 * 
 * @author taka
 *
 */
public class FileUtils {
	
	/**
	 * ファイルの拡張子を返す.
	 * 
	 * @param file ファイル
	 * @return ファイルの拡張子
	 */
	public static String getExtentionFile(MultipartFile file) {
		int dot = file.getOriginalFilename().lastIndexOf(".");
		String extention = "";
		if (dot > 0) {
			extention = file.getOriginalFilename().substring(dot).toLowerCase();
		}
		return extention;
	}

}

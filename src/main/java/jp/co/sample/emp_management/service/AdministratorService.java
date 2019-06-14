package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService {
	
	/**
	 * ハッシュエンコードDI.
	 * 
	 * @return ハッシュエンコード
	 */
	@Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	@Autowired
	private AdministratorRepository administratorRepository;

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param administrator 管理者情報
	 */
	public void insert(Administrator administrator) {
		administratorRepository.insert(administrator);
	}
	
	/**
	 * メールアドレスが管理者DBに存在するか調べる.
	 * 
	 * @param mailAddress 検索するメールアドレス
	 * @return 存在する:true 存在しない:false
	 */
	public boolean checkMailAddress(String mailAddress) {
		if(administratorRepository.findByMailAddress(mailAddress) != null) {
			return true;			
		}else {
			return false;
		}
		
	}
	
	/**
	 * ログインをします.
	 * @param mailAddress メールアドレス
	 * @param password パスワード
	 * @return 管理者情報　存在しない場合はnullが返ります
	 */
	public Administrator login(String mailAddress, String password) {
		Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
		Boolean isLogin = passwordEncoder().matches(password,administrator.getPassword());
		if(isLogin) {
			return administrator;
		}else {
			return null;
		}
	}
}

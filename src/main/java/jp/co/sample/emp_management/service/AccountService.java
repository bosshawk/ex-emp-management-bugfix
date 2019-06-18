package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.sample.emp_management.domain.Account;
import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * アカウントのサービス.
 * 
 * @author taka
 *
 */
@Service
public class AccountService implements UserDetailsService {

	@Autowired
	private AdministratorRepository adminRepository ;
	
	/**
	 * アカウント.
	 */
	@Override
	  public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
	    if (mailAddress == null || mailAddress.isEmpty()) {
	      throw new UsernameNotFoundException("not mailaddress");
	    }
	    Administrator admin = adminRepository.findByMailAddress(mailAddress);
	    if (admin == null) {
	      throw new UsernameNotFoundException("login null");
	    }
	    Account userInfo = new Account(admin);
	    
	    return userInfo;
	  }

}

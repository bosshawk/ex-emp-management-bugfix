package jp.co.sample.emp_management.comfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * セキュリティのコンフィグ.
 * 
 * @author taka
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * ログインのコンフィグ.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin().disable();
	}

}

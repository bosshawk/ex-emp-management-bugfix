package jp.co.sample.emp_management.comfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * セキュリティのコンフィグ.
 * 
 * @author taka
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailsService userDetailsService;

	/**
	 * セキュリティ設定を無視するリクエスト設定
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/img/**", "/css/**", "/js/**");
	}

	/**
	 * ログインのコンフィグ.
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//http.formLogin().disable();
	
		http.authorizeRequests()
				.antMatchers("/","/toInsert","/insert").permitAll()
				.antMatchers("/administrator/**").permitAll()
				//.antMatchers("/common/**","/error").permitAll()
				//.antMatchers("/employee/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
					.loginPage("/")
					.loginProcessingUrl("/login")
					.usernameParameter("mailAddress")
					.passwordParameter("password")
					.defaultSuccessUrl("/employee/showList",true)
					//.failureForwardUrl("/login")
					.permitAll()
					.and()
				.logout()
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					.permitAll()
					;
		
				//      .failureUrl("/login-error")
				//.defaultSuccessUrl("/employee/showList").permitAll().and().logout()
				// ログアウトでログインページに戻る
				//.logoutSuccessUrl("/login")
				// セッションを破棄する
				//.invalidateHttpSession(true).permitAll();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

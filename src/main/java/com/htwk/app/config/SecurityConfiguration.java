package com.htwk.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/webjars/**", "/css/**", "/js/**", "/images/**", "/fonts/**",
        "/weather/**");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf().disable()
        .authorizeRequests()
            .anyRequest()
            .fullyAuthenticated()
        .and()
           .antMatcher("/admin")
           .authorizeRequests()
           .anyRequest()
           .fullyAuthenticated()
        .and()
           .formLogin()
           .loginPage("/login")
           .defaultSuccessUrl("/admin/")
           .failureUrl("/login?error")
           .usernameParameter("username")
           .passwordParameter("password")
           .permitAll()
        .and()
           .logout()
           .logoutUrl("login?logout")
           .deleteCookies("remember-me")
           .deleteCookies("JSESSIONID")
           .permitAll()
        .and()
           .rememberMe();
    // @formatter:on 
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
  }
}

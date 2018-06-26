package com.mytaxi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication()
                .withUser("carAdmin").password("carAdmin@123").roles("CAR_ADMIN")
                .and()
                .withUser("driverAdmin").password("driverAdmin@123").roles("DRIVER_ADMIN", "DRIVER")
                .and()
                .withUser("driver").password("driver@123").roles("DRIVER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                    .antMatchers("/h2-console/**").permitAll()
                    .antMatchers("/cars/**").hasAnyRole("CAR_ADMIN")
                    .antMatchers("/drivers/**").hasAnyRole("DRIVER_ADMIN", "DRIVER")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler)
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
    }
}

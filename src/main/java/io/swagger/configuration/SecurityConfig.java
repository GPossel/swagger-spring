package io.swagger.configuration;

import io.swagger.filters.JwtRequestFilter;
import io.swagger.service.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserApiService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/Login.html").permitAll()
                .antMatchers("/loginIn.js").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout.js").permitAll()
                .antMatchers("/user/js/**").permitAll()
                .antMatchers("/user/AllUsers.html").permitAll()
                .antMatchers("/user/AddUser.html").permitAll()
                .antMatchers("/user/UpdateUsers.html").permitAll()
                .antMatchers("/Transactions.html").permitAll()
                .antMatchers("/TransactionsJSON.js").permitAll()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/users").access("hasAuthority('Admin')")
                .antMatchers("/transactions").access("hasAuthority('Employee') or hasAuthority('Customer') or hasAuthority('Admin')")
                .anyRequest().authenticated()
                // now we need to add the filter
                .and()
                .sessionManagement()
                // creating a stateless means; spring dont make a session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

}

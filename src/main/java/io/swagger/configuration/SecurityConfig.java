package io.swagger.configuration;

import io.swagger.filters.JwtRequestFilter;
import io.swagger.service.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserApiService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
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
                .antMatchers("/user/**").permitAll()
                .antMatchers("/Transactions.html").permitAll()
                .antMatchers("/TransactionsJSON.js").permitAll()
                .antMatchers("/ATM/**").permitAll()
                .antMatchers("/accounts").permitAll()
                .antMatchers("/accounts/").access("hasAuthority('Employee') or hasAuthority('Customer')")
                .antMatchers("/accounts/**").access("hasAuthority('Employee') or hasAuthority('Customer')")
                .antMatchers("/users").permitAll()
                .antMatchers("/users/**").access("hasAuthority('Admin') or hasAuthority('Employee') or hasAuthority('Customer')")
                .antMatchers("/transactions").access("hasAuthority('Employee') or hasAuthority('Customer')")
                .antMatchers("/transactions/**").access("hasAuthority('Employee') or hasAuthority('Customer')")
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
         return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}

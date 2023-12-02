package hs.aalen.financial_assets_portfolio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig {

        @Autowired
        MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

        @Autowired
        MyAuthenticationFailureHandler myAuthenticationFailureHandler;

        /* sources that are available admin-only */
        public static final String ADMIN = "[ROLE_ADMIN]";

        /* sources that are available publicly */
        private static final String[] PUBLIC_MATCHERS = {
                "/css/**",
                "/ts/**",
                "/images/**",
                "/",
                "/error/**/*",
                "/console/**",
                "/home",
                "/register",
                "/cookieData",
                "/login"
        };

        private static final String[] ADMIN_MATCHERS = {
        };


        //@Override
        public void configure(HttpSecurity http) throws Exception {

            http.csrf().disable() // disable cross-site-request-forgery
                    .authorizeRequests()
                    //.antMatchers(PUBLIC_MATCHERS).permitAll() // permit all public sources
                    /* allow post and get requests for login and registrations */
                    //.antMatchers(HttpMethod.POST, "/register/**").permitAll()
                    //.antMatchers(HttpMethod.GET, "/register/**").permitAll()
                    //.antMatchers(HttpMethod.POST, "/login/**").permitAll()
                    //.antMatchers(HttpMethod.GET, "/login/**").permitAll()
                    //.antMatchers(ADMIN_MATCHERS).hasRole("ADMIN") // permit admin matchers admin only
                    .anyRequest().authenticated()
                    .and().formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/loginProc").failureHandler(myAuthenticationFailureHandler)
                    .successHandler(myAuthenticationSuccessHandler)
                    .defaultSuccessUrl("/home")
                    .permitAll();
                    //.and().logout(logout -> logout
                            //.logoutUrl("/logout")
                            //.logoutSuccessUrl("/home")
                            /* delete cookies and invalidate Session after logout */
                            //.deleteCookies("role", "username")
                            //.invalidateHttpSession(true)
                    //);
        }

        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new AccountDetailsService();
        }

        /* Provider used for Authentication */
        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService());
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        //@Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());
        }
}
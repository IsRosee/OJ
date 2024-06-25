package com.yz.oj.config;


import com.yz.oj.util.JwtAuthenticationFilter;
import com.yz.oj.util.JwtService;
import com.yz.oj.util.JwtAuthenticationFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    //    private final CustomUserDetailsService userDetailsService;
//
//    public SecurityConfiguration(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public JwtService jwtService(
            @Value("${jwt.secret-key}") String secretKeyStr,
            @Value("${jwt.valid-seconds}") int validSeconds
    ) {
        return new JwtService(secretKeyStr, validSeconds);
    }
    // 1.需要注意的是SpringSecurity6.0版本不再是是继承WebSecurityConfigurerAdapter来配置HttpSecurity，而是使用SecurityFilterChain来注入
    // 2.SpringSecurity6.0需要添加@EnableWebSecurity来开启一些必要的组件
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            JwtAuthenticationFilter jwtAuthFilter)
            throws Exception {
        // 关闭csrf因为不使用session
        http.csrf(AbstractHttpConfigurer::disable);
        // 不通过Session获取SecurityContext,即
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 配置请求地址的权限
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // 配置不需要认证的请求
                        .requestMatchers("/", "/login","/index", "/css/**",
                                "/js/**", "/api/login","/api/who-am-i",
                                "/notification","test","experiments",
                                "/api/experiments", "/api/notifications").permitAll()
                        // 除了上面那些请求都需要认证
                        // jyz:先放开所有请求
                        .anyRequest().permitAll()
                )
//                // 禁用默认登录页
//                .formLogin(AbstractHttpConfigurer::disable)
                .formLogin(formLogin -> formLogin
                                .loginPage("/login")
//                        .defaultSuccessUrl("/index", true)
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()

                )
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

//    @Bean
//    WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().anyRequest();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var user = User
//                .withUsername("user1")
//                .password("111")
//                .authorities("STUDENT", "ASSISTANT")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    问题在这里shit
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }



    /*
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // 调用 JwtUserDetailService实例执行实际校验
//        return username -> userDetailsService.loadUserByUsername(username);
//    }


}

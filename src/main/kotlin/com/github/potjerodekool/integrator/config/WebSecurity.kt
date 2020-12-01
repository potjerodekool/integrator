package com.github.potjerodekool.integrator.config

import com.github.potjerodekool.integrator.jwt.JwtAuthenticationEntryPoint
import com.github.potjerodekool.integrator.jwt.SsoFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class WebSecurity(private val unauthorizedHandler: JwtAuthenticationEntryPoint,
                  @Value("\${server.servlet.context-path}") private val contextPath: String,
                  @Value("\${jwt.secretkey}") private val secretKey: String): WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .headers().cacheControl()

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "$contextPath/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/v2/api-docs").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/actuator").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/health").permitAll()
                .antMatchers(HttpMethod.GET, "$contextPath/metrics").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "$contextPath/**").permitAll()
                .antMatchers( "$contextPath/**/*").authenticated()
                .and()
                .addFilterBefore(SsoFilter(secretKey), UsernamePasswordAuthenticationFilter::class.java)
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
    }
}




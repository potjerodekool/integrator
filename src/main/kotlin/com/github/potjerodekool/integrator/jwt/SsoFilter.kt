package com.github.potjerodekool.integrator.jwt

import com.github.potjerodekool.integrator.jwt.model.AuthenticatedUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpMethod
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.DatatypeConverter

class SsoFilter(private val secretKey: String): Filter {

    override fun doFilter(request: ServletRequest?, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest

        val noAuth = true
        fakeAuth()

        if (httpServletRequest.method == HttpMethod.OPTIONS.name || noAuth) {
            chain.doFilter(request, response)
        } else {
            var authToken: String? = httpServletRequest.getHeader("Authorization")

            if (authToken != null) {
                if (authToken.startsWith("Bearer ")) {
                    authToken = authToken.substring(7)
                }

                val claims = decodeJWT(authToken)

                if (claims != null && isTokenExpired(claims).not()) {
                    val userName = claims.subject
                    SecurityContextHolder.getContext().authentication = AuthenticatedUser(authToken, userName)
                    chain.doFilter(request, response)
                } else {
                    forBidden(response)
                }
            } else {
                forBidden(response)
            }
        }
    }

    private fun forBidden(response: ServletResponse) {
        (response as HttpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN)
    }

    private fun isTokenExpired(claims: Claims): Boolean {
        return try {
            val expiration = claims.expiration
            expiration!!.before(Date())
        } catch (e: Exception) {
            true
        }
    }

    private fun decodeJWT(jwt: String?): Claims? {
        return try {
            //This line will throw an exception if it is not a signed JWS (as expected)
            Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).body
        } catch (e: Exception) {
            null
        }
    }

    private fun fakeAuth() {
        if (SecurityContextHolder.getContext().authentication !is AuthenticatedUser) {
            SecurityContextHolder.getContext().authentication = AuthenticatedUser(
                "",
                ""
            )
        }
        // AuthenticatedUser
    }
}
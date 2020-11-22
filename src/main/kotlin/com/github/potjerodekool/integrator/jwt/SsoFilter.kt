package com.github.potjerodekool.integrator.jwt

import com.github.potjerodekool.integrator.jwt.model.AuthenticatedUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
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
        val path = httpServletRequest.servletPath

        httpServletRequest.getHeaderNames().asIterator().forEach {header ->
            println("$header = ${httpServletRequest.getHeader(header)}")
        }

        var authToken: String? = httpServletRequest.getHeader("authorization")

        if (authToken != null) {
            if (authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7)
            }

            if (isTokenExpired(authToken).not()) {
                SecurityContextHolder.getContext().authentication = AuthenticatedUser(authToken)
                chain.doFilter(request, response)
            } else {
                forBidden(response)
            }
        } else {
            chain.doFilter(request, response)
        }
    }

    private fun forBidden(response: ServletResponse) {
        (response as HttpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN)
    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val expiration = getExpirationDateFromToken(token)
            expiration!!.before(Date())
        } catch (e: Exception) {
            true
        }
    }

    private fun getClaimsFromToken(token: String): Claims {
        return decodeJWT(token)
    }

    private fun getExpirationDateFromToken(token: String): Date? {
        val claims = getClaimsFromToken(token)
        return claims.expiration
    }

    private fun decodeJWT(jwt: String?): Claims {
        //This line will throw an exception if it is not a signed JWS (as expected)
        return Jwts.parser()
            .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
            .parseClaimsJws(jwt).body
    }
}
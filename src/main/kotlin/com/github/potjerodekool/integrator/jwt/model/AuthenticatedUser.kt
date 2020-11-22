package com.github.potjerodekool.integrator.jwt.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
class AuthenticatedUser(private val token: String): Authentication {

    override fun getName(): String {
        return ""
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getCredentials(): Any {
        return token
    }

    override fun getDetails(): Any {
        return ""
    }

    override fun getPrincipal(): Any {
        return ""
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(p0: Boolean) {
    }

}
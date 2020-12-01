package com.github.potjerodekool.integrator.jwt

import com.github.potjerodekool.integrator.jwt.model.AuthenticatedUser
import org.springframework.security.core.context.SecurityContextHolder

object JwtUtil {

    fun getUser(): AuthenticatedUser {
        return SecurityContextHolder.getContext().authentication as AuthenticatedUser
    }
}
package com.github.potjerodekool.integrator.jwt

import com.github.potjerodekool.integrator.jwt.model.AuthenticatedUser
import org.springframework.security.core.context.SecurityContextHolder

fun getUser(): AuthenticatedUser {
    return SecurityContextHolder.getContext().authentication as AuthenticatedUser
}
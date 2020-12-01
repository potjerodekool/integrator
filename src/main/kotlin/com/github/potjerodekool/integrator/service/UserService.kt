package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.User
import com.github.potjerodekool.integrator.data.jpa.repository.UserRepository
import com.github.potjerodekool.integrator.jwt.JwtUtil
import com.github.potjerodekool.integrator.util.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun findCurrentUser(): User {
        val externalUserId = JwtUtil.getUser().name
        val user = userRepository.findByExternalUserId(externalUserId)
        return user ?: throw UserNotFoundException()
    }
}
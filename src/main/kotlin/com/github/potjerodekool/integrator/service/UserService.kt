package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.User
import com.github.potjerodekool.integrator.data.jpa.repository.UserRepository
import com.github.potjerodekool.integrator.jwt.getUser
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun findCurrentUser(): User {
        val uuid = getUser().name
        return userRepository.findByUuid(uuid) ?: saveUser(uuid)
    }

    fun saveUser(uuid: String): User = userRepository.save(User(uuid = uuid))

    fun deleteUserById(id: Int) {
        userRepository.deleteById(id)
    }
}
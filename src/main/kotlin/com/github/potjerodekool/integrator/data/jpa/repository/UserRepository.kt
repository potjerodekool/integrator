package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Int> {

    fun findByExternalUserId(userId: String): User?
}
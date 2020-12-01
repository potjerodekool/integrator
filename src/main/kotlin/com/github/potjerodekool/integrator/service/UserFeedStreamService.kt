package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.api.model.UserFeedStreamRequest
import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import com.github.potjerodekool.integrator.data.jpa.repository.UserFeedStreamRepository
import com.github.potjerodekool.integrator.data.jpa.repository.UserRepository
import com.github.potjerodekool.integrator.jwt.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFeedStreamService(private val userFeedStreamRepository: UserFeedStreamRepository,
                            private val userService: UserService) {

    fun getUserFeedStreams(): List<UserFeedStream> {
        val userName = JwtUtil.getUser().name
        return userFeedStreamRepository.findByUserId(userName)
    }

    @Transactional
    fun createUserFeedStream(userFeedStreamRequest: UserFeedStreamRequest): Int {
        val user = userService.findCurrentUser()
        return userFeedStreamRepository.save(
                UserFeedStream(
                        user = user,
                        name = userFeedStreamRequest.name,
                        subscriptions = mutableListOf()
                )
        ).id!!
    }

    @Transactional
    fun updateUserFeedStream(id: Int, userFeedStreamRequest: UserFeedStreamRequest): Boolean {
        val userFeedStreamOptional = userFeedStreamRepository.findById(id)

        return if (userFeedStreamOptional.isEmpty) {
            false
        } else {
            val userFeedStream = userFeedStreamOptional.get()

            if (userFeedStreamRequest.name != "") {
                userFeedStream.name = userFeedStreamRequest.name
            }
            true
        }
    }

    @Transactional
    fun deleteUserFeedStream(id: Int) {
        userFeedStreamRepository.deleteById(id)
    }
}
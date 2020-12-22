package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.api.model.UserFeedStreamRequest
import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import com.github.potjerodekool.integrator.data.jpa.repository.SyndFeedSubscriptionRepository
import com.github.potjerodekool.integrator.data.jpa.repository.UserFeedStreamRepository
import com.github.potjerodekool.integrator.jwt.getUser
import com.github.potjerodekool.integrator.util.AllreadyExistsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserFeedStreamService(private val userFeedStreamRepository: UserFeedStreamRepository,
                            private val userService: UserService,
                            private val syndFeedSubscriptionRepository: SyndFeedSubscriptionRepository) {

    fun getUserFeedStreams(): List<UserFeedStream> {
        val userUUID = getUser().name
        return userFeedStreamRepository.findByUuid(userUUID)
    }

    fun getUserFeedStreamById(id: Int): UserFeedStream? {
        val userUUID = getUser().name
        return userFeedStreamRepository.findByUserAndId(userUUID, id)
    }

    @Transactional
    @Throws(AllreadyExistsException::class)
    fun createUserFeedStream(userFeedStreamRequest: UserFeedStreamRequest): Int {
        val user = userService.findCurrentUser()

        val existingUserFeedStream = userFeedStreamRepository.findByUserAndName(
            user.uuid,
            userFeedStreamRequest.name
        )

        if  (existingUserFeedStream != null) {
            throw AllreadyExistsException(existingUserFeedStream.id)
        }

        val subscriptions = userFeedStreamRequest.subscriptions
                .map {
                    syndFeedSubscriptionRepository.findByUri(it.uri) ?:
                        SyndFeedSubscription(uri = it.uri, active = true)
                }
                .toMutableList()

        return userFeedStreamRepository.save(
                UserFeedStream(
                        user = user,
                        name = userFeedStreamRequest.name,
                        subscriptions = subscriptions
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
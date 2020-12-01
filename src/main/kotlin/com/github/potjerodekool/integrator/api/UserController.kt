package com.github.potjerodekool.integrator.api

import com.github.potjerodekool.integrator.api.model.SyncFeedSubscriptionResponse
import com.github.potjerodekool.integrator.api.model.UserFeedStreamModel
import com.github.potjerodekool.integrator.api.model.UserFeedStreamRequest
import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import com.github.potjerodekool.integrator.service.UserFeedStreamService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

@Api
@RestController
@CrossOrigin
class UserController(private val userFeedStreamService: UserFeedStreamService) {

    @GetMapping("/user/feedstreams")
    fun getUserFeedStreams(): List<UserFeedStreamModel> {
        return userFeedStreamService.getUserFeedStreams()
                .map(this::toModel)
    }

    private fun toModel(userFeedStream: UserFeedStream): UserFeedStreamModel {
        val subscriptions = userFeedStream.subscriptions
                .map { toSyncFeedSubscriptionResponseda(it) }

        return UserFeedStreamModel(
                userFeedStream.id!!,
                subscriptions,
                userFeedStream.name
        )
    }

    private fun toSyncFeedSubscriptionResponseda(subscription: SyndFeedSubscription): SyncFeedSubscriptionResponse {
        return SyncFeedSubscriptionResponse(
                subscription.id!!,
                subscription.uri,
                subscription.active
        )
    }

    @PostMapping("/user/feedstreams")
    fun createUserFeedStream(@RequestBody userFeedStreamRequest: UserFeedStreamRequest): Int {
        return userFeedStreamService.createUserFeedStream(userFeedStreamRequest)
    }

    @PutMapping("/user/feedstreams/{id}")
    fun updateUserFeedStream(@PathVariable("id") id: Int,
                             @RequestBody userFeedStreamRequest: UserFeedStreamRequest) {
        userFeedStreamService.updateUserFeedStream(id, userFeedStreamRequest)
    }

    @DeleteMapping("/user/feedstreams/{id}")
    fun deleteUserFeedStream(@PathVariable("id") id: Int) {
        userFeedStreamService.deleteUserFeedStream(id)
    }

}
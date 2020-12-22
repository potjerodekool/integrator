package com.github.potjerodekool.integrator.api

import com.github.potjerodekool.integrator.api.model.SyndFeedSubscriptionResponse
import com.github.potjerodekool.integrator.api.model.UserFeedStreamModel
import com.github.potjerodekool.integrator.api.model.UserFeedStreamRequest
import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import com.github.potjerodekool.integrator.service.UserFeedStreamService
import com.github.potjerodekool.integrator.util.AllreadyExistsException
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api
@RestController
@CrossOrigin
class UserController(private val userFeedStreamService: UserFeedStreamService,
                     @Value("\${server.servlet.context-path}") private val contextPath: String) {

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

    private fun toSyncFeedSubscriptionResponseda(subscription: SyndFeedSubscription): SyndFeedSubscriptionResponse {
        return SyndFeedSubscriptionResponse(
                subscription.id!!,
                subscription.uri,
                subscription.active
        )
    }

    @PostMapping("/user/feedstreams")
    fun creatUserFeedStream(@RequestBody userFeedStreamRequest: UserFeedStreamRequest): ResponseEntity<Int> {
        return try {
            ResponseEntity.ok(userFeedStreamService.createUserFeedStream(userFeedStreamRequest))
        } catch (e: AllreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT)
               .header(HttpHeaders.LOCATION, "$contextPath/user/feedstreams/${e.id}")
                .build()
        }
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
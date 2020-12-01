package com.github.potjerodekool.integrator.api

import com.github.potjerodekool.integrator.api.model.CreateSyndFeedSubscriptionRequest
import com.github.potjerodekool.integrator.api.model.SyncFeedSubscriptionResponse
import com.github.potjerodekool.integrator.api.model.UpdateSyndFeedSubscriptionRequest
import com.github.potjerodekool.integrator.data.model.SyndFeedSubscriptionModel
import com.github.potjerodekool.integrator.service.FeedProcessorService
import com.github.potjerodekool.integrator.service.SyndFeedSubscriptionService
import io.swagger.annotations.Api
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.OutputStreamWriter

@Api
@RestController
@CrossOrigin
class SyndFeedController(private val syndFeedSubscriptionService: SyndFeedSubscriptionService,
                         private val feedProcessorService: FeedProcessorService) {

    @GetMapping("/syndfeed/subscriptions", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSubscriptions(): List<SyndFeedSubscriptionModel> {
        return syndFeedSubscriptionService.getSubscriptions()
    }

    @GetMapping("/syndfeed/subscriptions/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSubscriptions(@PathVariable("id") id: Int): ResponseEntity<SyndFeedSubscriptionModel> {
        val subscription = syndFeedSubscriptionService.getSubscription(id)
        return if (subscription != null) ResponseEntity.ok(subscription) else ResponseEntity.notFound().build()
    }

    @PostMapping("/syndfeed/subscriptions", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addSubscription(@RequestBody request: CreateSyndFeedSubscriptionRequest): SyncFeedSubscriptionResponse {
        val id = syndFeedSubscriptionService.addSubscription(request.uri)
        return SyncFeedSubscriptionResponse(id, request.uri, true)
    }

    @PatchMapping("/syndfeed/subscriptions/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateSubscription(@PathVariable("id") id: Int,
                           @RequestBody request: UpdateSyndFeedSubscriptionRequest): SyncFeedSubscriptionResponse {
        syndFeedSubscriptionService.updateSubscription(id, request.uri)
        return SyncFeedSubscriptionResponse(id, request.uri, true)
    }

    @DeleteMapping("/syndfeed/subscriptions/{id}")
    fun deleteSubscription(@PathVariable("id") id: Int): ResponseEntity<String> {
        return try {
            syndFeedSubscriptionService.deleteSubscription(id)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/syncfeed/sync")
    fun processFeeds(): ResponseEntity<StreamingResponseBody> {
        val stream = StreamingResponseBody { out ->
            val writer = OutputStreamWriter(out)
            feedProcessorService.sync(writer)
            out.close()
        }

        return ResponseEntity.ok(stream)

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(stream)
    }

}
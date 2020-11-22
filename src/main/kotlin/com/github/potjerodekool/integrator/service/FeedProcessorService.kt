package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.repository.SyndFeedSubScriptionRepositry
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.logging.Level
import java.util.logging.Logger

@Service
class FeedProcessorService(private val feedProcessorDBService: FeedProcessorDBService,
                           private val syndFeedSubScriptionRepositry: SyndFeedSubScriptionRepositry) {

    private companion object {
        private val LOGGER = Logger.getLogger(FeedProcessorService::class.java.name)
    }

    @Scheduled(cron = "0 0 0 ? * * *")
    fun processScheduled() {
        process()
    }

    fun process() {
        if (feedProcessorDBService.canStartJob().not()) {
            return
        }

        feedProcessorDBService.startJob()

        try {
            var lastId = 0
            var hasFailures = false

            do {
                val slice = syndFeedSubScriptionRepositry.findAfterIdOrderedById(lastId, PageRequest.of(0, 10))

                if (slice.content.isNotEmpty()) {
                    slice.content.forEach { subscription ->
                        if (processFeeds(subscription).not()) {
                            hasFailures = true
                        }
                        lastId += subscription.id!!
                    }
                }
            } while (slice.content.isNotEmpty())
            feedProcessorDBService.finishJob(if (hasFailures) "" else "finished")
        } catch (e: Exception) {
            LOGGER.log(Level.SEVERE, "Processing feeds failed", e)
            feedProcessorDBService.finishJob(e.message ?: "Failed with a ${e.javaClass.name}")
        }
    }

    private fun processFeeds(subscription: SyndFeedSubscription): Boolean {
        logMessage("Skipped ${subscription.uri}")
        LOGGER.info("Skipped ${subscription.uri}")
        return false
    }

    private fun logMessage(message: String) {
        feedProcessorDBService.logExceptionMessage(message)
    }

}

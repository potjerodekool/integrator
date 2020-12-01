package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.repository.SyndFeedSubScriptionRepositry
import com.github.potjerodekool.integrator.util.*
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.xml.sax.InputSource
import java.io.OutputStreamWriter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import java.util.logging.Level
import java.util.logging.Logger

@Service
class FeedProcessorService(private val feedProcessorDBService: FeedProcessorDBService,
                           private val syndFeedSubScriptionRepositry: SyndFeedSubScriptionRepositry) {

    private companion object {
        private val LOGGER = Logger.getLogger(FeedProcessorService::class.java.name)
    }

    private val client = HttpClient.newHttpClient()

    @Scheduled(cron = "0 0 0 * * *")
    fun syncScheduled() {
        sync()
    }

    fun sync(writer: OutputStreamWriter = DEF_NULL_OUTPUT_STREAM_WRITER) {
        if (feedProcessorDBService.canStartJob().not()) {
            writer.writeLine("Busy")
            return
        }

        feedProcessorDBService.startJob()
        writer.writeLine("Starting at ${LocalDateTime.now().format()}").newLine()

        try {
            var lastId = 0
            var hasFailures = false

            do {
                val slice = syndFeedSubScriptionRepositry.findAfterIdOrderedById(lastId, PageRequest.of(0, 10))

                if (slice.content.isNotEmpty()) {
                    slice.content.forEach { subscription ->
                        if (processFeeds(subscription, writer).not()) {
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

        writer.writeLine("Finished at ${LocalDateTime.now().format()}").newLine()
    }

    private fun processFeeds(subscription: SyndFeedSubscription, writer: OutputStreamWriter): Boolean {
        writer.write("Processing ${subscription.uri} ")
        writer.flush()

        val feed = fetchFeed(subscription.uri)

        if (feed != null) {
            feed.entries.forEach { entry ->
                feedProcessorDBService.saveOrUpdate(entry, subscription)
            }
        }

        writer.writeLine(" done").newLine()
        return false
    }

    private fun fetchFeed(uri: String): SyndFeed? {
        val request = HttpRequest.newBuilder(URI(uri)).build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofInputStream())

        return if (response.statusCode() == HttpStatus.MOVED_PERMANENTLY.value()) {
            val newLocation = response.headers().firstValue("location").orElse("")
            LOGGER.info("Skipping. Moved permanently $uri to $newLocation")
            null
        } else {
            val byteStream = response.body().readAllBytes().inputStream()

            val syndFeedInput = SyndFeedInput()
            syndFeedInput.isAllowDoctypes = true
            syndFeedInput.build(InputSource(byteStream))
        }
    }

}

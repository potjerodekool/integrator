package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntry
import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntryCategory
import com.github.potjerodekool.integrator.data.jpa.entity.ScheduledJob
import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.repository.FeedEntryRepository
import com.github.potjerodekool.integrator.data.jpa.repository.ScheduledJobRepository
import com.github.potjerodekool.integrator.util.toLocalDateTime
import com.rometools.rome.feed.synd.SyndEntry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class FeedProcessorDBService(private val scheduledJobRepository: ScheduledJobRepository,
                             private val feedEntryRepository: FeedEntryRepository) {

    private companion object {
        private const val JOB_NAME = "feedjob"
    }

    fun canStartJob(): Boolean {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)
        val started = job?.started
        return if (job == null || !job.busy || started == null) true
            else started.isBefore(LocalDateTime.now().minusHours(4))
    }

    @Transactional
    fun startJob() {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)

        if (job == null) {
            scheduledJobRepository.save(ScheduledJob(
                    jobName = JOB_NAME,
                    started = LocalDateTime.now(),
                    busy = true
            )).id!!
        } else {
            job.started = LocalDateTime.now()
            job.finished = null
            job.busy = true
            job.message = null
            job.id!!
        }
    }

    @Transactional
    fun finishJob(message: String = "") {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)!!
        job.finished = LocalDateTime.now()
        job.busy = false

        if (message.isNotEmpty()) {
            job.message = message
        }
    }

    @Transactional
    fun logExceptionMessage(message: String) {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)!!
        job.message = message
    }

    @Transactional
    fun saveOrUpdate(entry: SyndEntry, subscription: SyndFeedSubscription) {
        val existingFeedEntry = feedEntryRepository.findBySubscriptionAndLink(subscription, entry.link)

        if (existingFeedEntry == null) {
            val feedEntry = FeedEntry(
                    subscription = subscription,
                    link = entry.link,
                    description = entry.description.value,
                    pubDate = entry.publishedDate.toLocalDateTime(),
                    title = entry.title)
            linkCategories(entry, feedEntry)
            feedEntryRepository.save(feedEntry)
        } else {
            existingFeedEntry.description = entry.description.value
            existingFeedEntry.pubDate = entry.publishedDate.toLocalDateTime()
            existingFeedEntry.title = entry.title
            linkCategories(entry, existingFeedEntry )
        }
    }

    private fun linkCategories(entry: SyndEntry,
                               feedEntry: FeedEntry) {
        /* Create a map with categories of the feedentry.
           All categories that exist after processing in this map are deleted.
         */
        val categoriesToDelete = feedEntry.categories
                .associateBy { it.name }
                .toMutableMap()

        entry.categories
                .map { it.name }
                .forEach { categoryName ->
                    if (categoriesToDelete.containsKey(categoryName)) {
                        categoriesToDelete.remove(categoryName)
                    } else {
                        feedEntry.categories.add(FeedEntryCategory(name = categoryName))
                    }
                }

        categoriesToDelete.forEach { category ->
            feedEntry.categories.remove(category)
        }
    }
}
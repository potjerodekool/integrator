package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntry
import com.github.potjerodekool.integrator.data.jpa.repository.FeedEntryRepository
import com.github.potjerodekool.integrator.data.jpa.repository.UserFeedStreamRepository
import com.github.potjerodekool.integrator.jwt.JwtUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class FeedEntryService(private val feedEntryRepository: FeedEntryRepository,
                       private val userFeedStreamRepository: UserFeedStreamRepository) {

    fun findFeedEntries(page: Int): Page<FeedEntry> {
        val userName = JwtUtil.getUser().name
        val subscriptions = userFeedStreamRepository.findByUserId(userName)
                .flatMap { it.subscriptions }

        return feedEntryRepository.findBySubscriptions(
                subscriptions,
                PageRequest.of(page, 100, Sort.by("pubDate").descending())
        )
    }

    fun findFeedEntries(name: String, page: Int): Page<FeedEntry> {
        val userName = JwtUtil.getUser().name
        val userFeedStream = userFeedStreamRepository.findByUserAndName(userName, name)

        return if (userFeedStream == null) {
            Page.empty()
        } else {
            val subscriptions =  userFeedStream.subscriptions
            feedEntryRepository.findBySubscriptions(
                    subscriptions,
                    PageRequest.of(page, 100, Sort.by("pubDate").descending())
            )
        }
    }
}
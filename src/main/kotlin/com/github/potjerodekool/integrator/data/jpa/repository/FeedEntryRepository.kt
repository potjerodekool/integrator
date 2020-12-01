package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntry
import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FeedEntryRepository: JpaRepository<FeedEntry, Int> {

    fun findBySubscriptionAndLink(subscription: SyndFeedSubscription, link: String): FeedEntry?

    @Query("SELECT fe FROM FeedEntry fe WHERE subscription in (:subscriptions)")
    fun findBySubscriptions(@Param("subscriptions") subscriptions: List<SyndFeedSubscription>,
                            pageable: Pageable): Page<FeedEntry>

}
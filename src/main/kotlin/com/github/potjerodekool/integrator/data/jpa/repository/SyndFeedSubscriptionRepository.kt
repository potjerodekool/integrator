package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SyndFeedSubscriptionRepository : JpaRepository<SyndFeedSubscription, Int> {

    @Query("FROM SyndFeedSubscription WHERE id > :id AND active = true ORDER BY id ASC")
    fun findAfterIdOrderedById(@Param("id") id: Int, pageable: Pageable): Slice<SyndFeedSubscription>

    fun findByUri(uri: String): SyndFeedSubscription?
}
package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.SyndFeedSubscription
import com.github.potjerodekool.integrator.data.jpa.repository.SyndFeedSubscriptionRepository
import com.github.potjerodekool.integrator.data.model.SyndFeedSubscriptionModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
class SyndFeedSubscriptionService(private val syndFeedSubscriptionRepository: SyndFeedSubscriptionRepository) {

    private fun toModel(syndFeedSubscription: SyndFeedSubscription): SyndFeedSubscriptionModel {
        return SyndFeedSubscriptionModel(syndFeedSubscription.id!!, syndFeedSubscription.uri)
    }

    fun getSubscriptions(): List<SyndFeedSubscriptionModel> {
        return syndFeedSubscriptionRepository.findAll()
            .map(this::toModel)
    }

    fun getSubscription(id: Int): SyndFeedSubscriptionModel? {
        return syndFeedSubscriptionRepository.findById(id)
            .map(this::toModel)
            .orElse(null)
    }

    @Transactional
    fun addSubscription(uri: String): Int {
        return syndFeedSubscriptionRepository.save(SyndFeedSubscription(uri = uri)).id!!
    }

    @Transactional
    fun updateSubscription(id: Int, uri: String) {
        val subscriptionOptional = syndFeedSubscriptionRepository.findById(id)

        if (subscriptionOptional.isEmpty) {
            throw Exception("Not found")
        }

        val subscription = subscriptionOptional.get()
        subscription.uri = uri
    }

    @Transactional
    fun deleteSubscription(id: Int) {
        syndFeedSubscriptionRepository.deleteById(id)
    }

}
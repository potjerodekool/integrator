package com.github.potjerodekool.integrator.api.model

data class UserFeedStreamModel(val id: Int,
                               val subscriptions: List<SyndFeedSubscriptionResponse>,
                               val name: String)
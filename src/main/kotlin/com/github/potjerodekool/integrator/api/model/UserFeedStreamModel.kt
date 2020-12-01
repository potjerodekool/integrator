package com.github.potjerodekool.integrator.api.model

data class UserFeedStreamModel(val id: Int,
                               val subscriptions: List<SyncFeedSubscriptionResponse>,
                               val name: String)
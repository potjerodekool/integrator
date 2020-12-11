package com.github.potjerodekool.integrator.api.model

data class UserFeedStreamRequest(val subscriptions: List<SyndFeedSubscription>,
                                 val name: String)
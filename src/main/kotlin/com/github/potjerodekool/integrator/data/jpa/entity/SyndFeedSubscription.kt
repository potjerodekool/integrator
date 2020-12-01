package com.github.potjerodekool.integrator.data.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "synd_feed_subscription")
data class SyndFeedSubscription (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        var id: Int? = null,
        @Column(name = "uri", nullable = false, unique = true)
        var uri: String,
        @Column(name = "active", nullable = false)
        var active: Boolean = true) {

        constructor(): this(null, "")
}
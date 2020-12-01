package com.github.potjerodekool.integrator.data.jpa.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "feedentry")
data class FeedEntry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Int? = null,
        @ManyToOne
        @JoinColumn(name = "subscription_id", nullable = false)
        val subscription: SyndFeedSubscription,
        @Column(name = "link", nullable = false)
        val link: String,
        @Column(name = "description", nullable = false)
        var description: String,
        @Column(name = "pubdate")
        var pubDate: LocalDateTime?,
        @Column(name = "title")
        var title: String? = null,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(name = "feedentry_to_category",
                joinColumns = [
                        JoinColumn(name = "feedentry_id")
                ],
                inverseJoinColumns = [
                        JoinColumn(name = "category_id")
                ]
        )
        var categories: MutableList<FeedEntryCategory> = mutableListOf()
)
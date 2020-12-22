package com.github.potjerodekool.integrator.data.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "user_feed_stream",
        uniqueConstraints = [
                UniqueConstraint(
                        columnNames = ["user_id", "name"]
                )
        ]
)
data class UserFeedStream(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_feed_stream_id", nullable = false)
        var id: Int? = null,
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,
        @Column(name = "name", nullable = false, unique = true)
        var name: String,

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(name = "user_feed_stream_to_synd_feed_subscription",
                joinColumns = [JoinColumn(name = "user_feed_stream_id")],
                inverseJoinColumns = [JoinColumn(name = "synd_feed_subscription_id")]
        )
        var subscriptions: MutableList<SyndFeedSubscription>

) {
}
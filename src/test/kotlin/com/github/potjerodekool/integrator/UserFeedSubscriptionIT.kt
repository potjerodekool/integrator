package com.github.potjerodekool.integrator

import com.github.potjerodekool.integrator.api.model.SyndFeedSubscription
import com.github.potjerodekool.integrator.api.model.SyndFeedSubscriptionResponse
import com.github.potjerodekool.integrator.api.model.UserFeedStreamRequest
import com.github.potjerodekool.integrator.data.jpa.entity.User
import com.github.potjerodekool.integrator.data.jpa.repository.UserRepository
import com.github.potjerodekool.integrator.jwt.model.AuthenticatedUser
import com.github.potjerodekool.integrator.service.SyndFeedSubscriptionService
import com.github.potjerodekool.integrator.service.UserFeedStreamService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.test.assertEquals

@ExtendWith(SpringExtension::class, MariaDBExtension::class)
@SpringBootTest
@ContextConfiguration(
        initializers = [ITContextInitializer::class]
)
class UserFeedSubscriptionIT {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var syndFeedSubscriptionService: SyndFeedSubscriptionService

    @Autowired
    private lateinit var userFeedStreamService: UserFeedStreamService

    private val userUUID = UUID.randomUUID().toString()

    private fun login() {
        SecurityContextHolder.getContext().authentication = AuthenticatedUser(
                "",
                userUUID
        )
    }

    @Test
    fun test() {
        login()

        userRepository.save(User(
                uuid = userUUID
        ))

        val subscriptionId = syndFeedSubscriptionService.addSubscription("http://myfeeds.com/feed")

        val id = userFeedStreamService.createUserFeedStream(
                UserFeedStreamRequest(
                        name = "My subscriptions",
                        subscriptions = listOf(
                                SyndFeedSubscription(
                                        uri = "http://myfeeds.com/feed"
                                )
                        )
                )
        )

        val userFeedStreams = userFeedStreamService.getUserFeedStreams()

        assertEquals(1, userFeedStreams.size)
        val userFeedStream = userFeedStreams.first()
        assertEquals(id, userFeedStream.id)
        assertEquals("My subscriptions", userFeedStream.name)

        val subscriptions = userFeedStreamService.getUserFeedStreamById(id)!!.subscriptions

        assertEquals(1, subscriptions.size)
        assertEquals(subscriptionId, subscriptions.first().id)
        assertEquals("http://myfeeds.com/feed", subscriptions.first().uri)
    }
}
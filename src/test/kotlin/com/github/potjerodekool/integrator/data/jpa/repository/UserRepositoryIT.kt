package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.ITContextInitializer
import com.github.potjerodekool.integrator.MariaDBExtension
import com.github.potjerodekool.integrator.data.jpa.entity.User
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

@ExtendWith(SpringExtension::class, MariaDBExtension::class)
@SpringBootTest
@ContextConfiguration(
    initializers = [ITContextInitializer::class]
)
class UserRepositoryIT {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun testSaveUser() {
        val uuid = UUID.randomUUID().toString()
        assertNull(userRepository.findByUuid(uuid))

        userRepository.save(User(uuid = uuid))

        val user = userRepository.findByUuid(uuid)
        assertEquals(uuid, user?.uuid)
    }
}
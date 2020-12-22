package com.github.potjerodekool.integrator

import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import au.com.dius.pact.provider.junitsupport.loader.PactFolder
import au.com.dius.pact.provider.junitsupport.loader.PactFolderLoader
import au.com.dius.pact.provider.junitsupport.loader.PactSource
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, MariaDBExtension::class)
@ContextConfiguration(initializers = [ITContextInitializer::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("Integrator")
@PactFolder(value = "pacts")
@Disabled
class ContractVerificationTest {

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider::class)
    fun pactVerificationTestTemplate(context: PactVerificationContext) {
        context.verifyInteraction()
    }
}
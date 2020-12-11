package com.github.potjerodekool.integrator

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.support.TestPropertySourceUtils

class ITContextInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        val mariaDB = ITShared.mariaContainer
        val mariaUrl = mariaDB.jdbcUrl
        val mariaUser = mariaDB.username
        val mariaPwd = mariaDB.password

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
            context,
            "spring.datasource.url=$mariaUrl",
            "spring.datasource.username=$mariaUser",
            "spring.datasource.password=$mariaPwd"
        )
    }
}
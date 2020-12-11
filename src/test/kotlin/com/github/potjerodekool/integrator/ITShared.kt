package com.github.potjerodekool.integrator

// Shared things for integrations tests.
object ITShared {

    val mariaContainer = ReusableMariaDBContainer()
        .apply { start() }
}
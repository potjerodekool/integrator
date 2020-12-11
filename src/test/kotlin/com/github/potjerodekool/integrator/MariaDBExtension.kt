package com.github.potjerodekool.integrator

import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * MariaDB extension for integration tests
 * to restore database.
 */
class MariaDBExtension: BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(p0: ExtensionContext?) {
        ITShared.mariaContainer.createDataBaseDump()
    }

    override fun afterEach(p0: ExtensionContext?) {
        ITShared.mariaContainer.restoreDataBase()
    }
}
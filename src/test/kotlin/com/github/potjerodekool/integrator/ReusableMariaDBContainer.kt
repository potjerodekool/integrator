package com.github.potjerodekool.integrator

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.utility.DockerImageName
import java.lang.RuntimeException

/* A Maria DB container that can create a dump and restore it
 * so it can be shared between integration tests.
 */
class ReusableMariaDBContainer: MariaDBContainer<ReusableMariaDBContainer>(
    DockerImageName.parse(NAME).withTag("10.3.6")) {

    private var createDump = true

    init {
        //Use root user so we can
        //withUsername("root")
        //withPassword("")
        withClasspathResourceMapping(
            "dump.sh",
            "/dump.sh",
            BindMode.READ_ONLY
        )
        withClasspathResourceMapping(
            "restore.sh",
        "/restore.sh",
            BindMode.READ_ONLY
        )
    }

    override fun start() {
        super.start()
        execInContainer("chmod", "+x", "/dump.sh")
        execInContainer("chmod", "+x", "/restore.sh")
    }

    fun createDataBaseDump() {
        if (createDump){
            createDump = false
            val result = execInContainer("sh", "/dump.sh")
            if (result.exitCode != 0) {
                throw RuntimeException("Failed to create dump. ExitCode: ${result.exitCode}. Message: ${result.stderr}")
            }
        }
    }

    fun restoreDataBase() {
        val result = execInContainer("sh", "/restore.sh")
        if (result.exitCode != 0) {
            throw RuntimeException("Failed to restore database. ExitCode: ${result.exitCode}. Message: ${result.stderr}")
        }
    }
}
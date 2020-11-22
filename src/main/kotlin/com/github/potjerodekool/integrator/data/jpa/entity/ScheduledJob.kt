package com.github.potjerodekool.integrator.data.jpa.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "scheduldjob")
data class ScheduledJob(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        var id: Int? = null,
        @Column(name = "jobname", nullable = false, unique = true)
        var jobName: String,
        @Column(name = "started")
        var started: LocalDateTime? = null,
        @Column(name = "finished")
        var finished: LocalDateTime? = null,
        @Column(name = "busy", nullable = false)
        var busy: Boolean = false,
        @Column(name = "message")
        var message: String? = null)
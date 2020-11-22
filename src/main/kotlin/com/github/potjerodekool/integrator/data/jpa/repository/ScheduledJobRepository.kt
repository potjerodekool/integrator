package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.ScheduledJob
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScheduledJobRepository: JpaRepository<ScheduledJob, Int> {

    fun findByJobName(jobName: String): ScheduledJob?
}
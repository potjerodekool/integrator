package com.github.potjerodekool.integrator.service

import com.github.potjerodekool.integrator.data.jpa.entity.ScheduledJob
import com.github.potjerodekool.integrator.data.jpa.repository.ScheduledJobRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class FeedProcessorDBService(private val scheduledJobRepository: ScheduledJobRepository) {

    private companion object {
        private const val JOB_NAME = "feedjob"
    }

    fun canStartJob(): Boolean {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)
        return job == null || !job.busy
    }

    @Transactional
    fun startJob() {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)

        if (job == null) {
            scheduledJobRepository.save(ScheduledJob(
                    jobName = JOB_NAME,
                    started = LocalDateTime.now(),
                    busy = true
            )).id!!
        } else {
            job.started = LocalDateTime.now()
            job.finished = null
            job.busy = true
            job.message = null
            job.id!!
        }
    }

    @Transactional
    fun finishJob(message: String = "") {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)!!
        job.finished = LocalDateTime.now()
        job.busy = false

        if (message.isNotEmpty()) {
            job.message = message
        }
    }

    @Transactional
    fun logExceptionMessage(message: String) {
        val job = scheduledJobRepository.findByJobName(JOB_NAME)!!
        job.message = message
    }
}
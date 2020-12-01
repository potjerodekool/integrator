package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.FeedEntryCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedEntryCategoryRepository: JpaRepository<FeedEntryCategory, Int> {

    fun findByName(name: String): FeedEntryCategory?
}
package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserFeedStreamRepository: JpaRepository<UserFeedStream, Int> {

    @Query("FROM UserFeedStream WHERE user.externalUserId = :userId")
    fun findByUserId(@Param("userId") userId: String): List<UserFeedStream>

    @Query("FROM UserFeedStream WHERE user.externalUserId = :userId AND name = :name")
    fun findByUserAndName(@Param("userId") userId: String, @Param("name") name: String): UserFeedStream?
}
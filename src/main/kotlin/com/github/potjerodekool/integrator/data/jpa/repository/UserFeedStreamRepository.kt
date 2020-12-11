package com.github.potjerodekool.integrator.data.jpa.repository

import com.github.potjerodekool.integrator.data.jpa.entity.UserFeedStream
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserFeedStreamRepository: JpaRepository<UserFeedStream, Int> {

    @Query("FROM UserFeedStream WHERE user.uuid = :uuid")
    fun findByUuid(@Param("uuid") uuid: String): List<UserFeedStream>

    @Query("FROM UserFeedStream WHERE user.uuid = :uuid AND name = :name")
    fun findByUserAndName(@Param("uuid") uuid: String, @Param("name") name: String): UserFeedStream?

    @Query("""FROM UserFeedStream us inner join fetch us.subscriptions s
        WHERE us.user.uuid = :uuid AND us.id = :id""")
    fun findByUserAndId(@Param("uuid") uuid: String, @Param("id") id: Int): UserFeedStream?
}
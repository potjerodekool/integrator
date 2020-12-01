package com.github.potjerodekool.integrator.data.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "feedentry_category")
data class FeedEntryCategory(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        val id: Int? = null,
        @Column(name = "name", nullable = false)
        val name: String)
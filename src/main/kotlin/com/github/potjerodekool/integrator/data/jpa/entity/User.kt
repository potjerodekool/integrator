package com.github.potjerodekool.integrator.data.jpa.entity

import javax.persistence.*

@Entity
@Table(name = "user")
class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false)
        var id: Int? = null,
        @Column(name = "ext_user_id", nullable = false)
        val externalUserId: String)
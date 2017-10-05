package com.example.demo.security.model.entities

import java.io.Serializable
import javax.persistence.*


@Entity
@Table(name = "USER_ROLE")
class UserRole {

    @EmbeddedId
    internal var id = Id()

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", insertable = false, updatable = false)
    var role: Role = Role.MEMBER

    @Embeddable
    class Id : Serializable {

        @Column(name = "APP_USER_ID")
        protected var userId: Long? = null

        @Enumerated(EnumType.STRING)
        @Column(name = "ROLE")
        protected var role: Role? = null

        constructor() {}

        constructor(userId: Long?, role: Role) {
            this.userId = userId
            this.role = role
        }

        companion object {
            private const val serialVersionUID = 1322120000551624359L
        }
    }
}
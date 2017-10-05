package com.example.demo.security.model.entities

import javax.persistence.*

@Entity
@Table(name = "APP_USER")
class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "username")
    var username: String? = null

    @Column(name = "password")
    var password: String? = null

    @OneToMany
    @JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID")
    var roles: List<UserRole>? = null

    constructor() {}

    constructor(username: String, password: String, roles: List<UserRole>, id: Long?) {
        this.id = id
        this.username = username
        this.password = password
        this.roles = roles
    }
}
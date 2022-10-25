package com.csruletka.repository

import com.csruletka.dto.user.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.reactive.ReactorCrudRepository

@Repository
interface UserRepository : ReactorCrudRepository<User, String> {

}
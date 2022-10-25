package com.csruletka.config.security

import com.csruletka.dto.steam.SteamLoginRequest
import com.csruletka.service.UserService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.PublisherCoroutine
import kotlinx.coroutines.reactive.publish
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Publisher

@Singleton
class AuthProvider(
    private val userService: UserService
) : AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>, authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
        val headers = httpRequest.headers

        return publish {
            userService.loginUser(
                SteamLoginRequest(
                    identity = authenticationRequest.identity as String,
                    sig = authenticationRequest.secret as String,
                    returnTo = headers.get("return_to")!!,
                    responseNonce = headers.get("response_nonce")!!,
                    assocHandle = headers.get("assoc_handle")!!,
                    signed = headers.get("signed")!!,
                )
            )
            println("hui")
            AuthenticationResponse.success(authenticationRequest.identity as String, listOf())
        }
    }
}
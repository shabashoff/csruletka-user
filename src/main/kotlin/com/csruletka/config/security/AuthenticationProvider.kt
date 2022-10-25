package com.csruletka.config.security

import com.csruletka.dto.steam.SteamLoginRequest
import com.csruletka.service.UserService
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import kotlinx.coroutines.reactor.mono
import org.reactivestreams.Publisher

@Singleton
class AuthProvider(
    private val userService: UserService
) : AuthenticationProvider {
    override fun authenticate(httpRequest: HttpRequest<*>, authenticationRequest: AuthenticationRequest<*, *>): Publisher<AuthenticationResponse> {
        val headers = httpRequest.headers
        return mono {
            val userId = userService.loginUser(
                SteamLoginRequest(
                    identity = authenticationRequest.identity as String,
                    sig = authenticationRequest.secret as String,
                    returnTo = headers.get("return_to")!!,
                    responseNonce = headers.get("response_nonce")!!,
                    assocHandle = headers.get("assoc_handle")!!,
                    signed = headers.get("signed")!!,
                )
            )
            if (userId != null) {
                AuthenticationResponse.success(userId)
            } else {
                AuthenticationResponse.failure()
            }
        }
    }
}
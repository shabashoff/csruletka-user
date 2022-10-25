package com.csruletka.config

import com.csruletka.dto.steam.SteamUserInfo
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.model.runtime.convert.AttributeConverter
import java.io.IOException
import javax.persistence.Converter

private val objectMapper: ObjectMapper = ObjectMapper()


@Converter(autoApply = true)
open class JpaConverterJson : AttributeConverter<SteamUserInfo, String?> {
    override fun convertToPersistedValue(entityValue: SteamUserInfo, context: ConversionContext): String? {
        return try {
            objectMapper.writeValueAsString(entityValue)
        } catch (ex: JsonProcessingException) {
            throw IllegalArgumentException(ex)
        }
    }

    override fun convertToEntityValue(persistedValue: String?, context: ConversionContext): SteamUserInfo {
        return try {
            objectMapper.readValue(persistedValue, SteamUserInfo::class.java)
        } catch (ex: IOException) {
            throw IllegalArgumentException(ex)
        }
    }
}
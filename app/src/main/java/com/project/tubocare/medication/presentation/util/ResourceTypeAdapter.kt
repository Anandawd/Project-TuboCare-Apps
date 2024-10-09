package com.project.tubocare.medication.presentation.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.project.tubocare.core.util.Resource
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResourceTypeAdapter<T> : JsonSerializer<Resource<*>>, JsonDeserializer<Resource<*>> {

    override fun serialize(src: Resource<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonObject = JsonObject()
        when (src) {
            is Resource.Success -> {
                jsonObject.addProperty("status", "success")
                jsonObject.add("data", context.serialize(src.data))
            }
            is Resource.Error -> {
                jsonObject.addProperty("status", "error")
                jsonObject.addProperty("message", src.message)
            }
            is Resource.Loading -> {
                jsonObject.addProperty("status", "loading")
            }
        }
        return jsonObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Resource<Any> {
        val jsonObject = json.asJsonObject
        val status = jsonObject.get("status").asString
        return when (status) {
            "success" -> {
                val dataType = (typeOfT as ParameterizedType).actualTypeArguments[0]
                val data = context.deserialize<Any>(jsonObject.get("data"), dataType)
                Resource.Success(data)
            }
            "error" -> {
                val message = jsonObject.get("message").asString
                Resource.Error(message)
            }
            "loading" -> Resource.Loading()
            else -> throw JsonParseException("Unknown resource status")
        }
    }
}
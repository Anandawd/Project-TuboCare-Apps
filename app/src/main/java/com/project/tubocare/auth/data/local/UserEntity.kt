package com.project.tubocare.auth.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.tubocare.auth.domain.model.User
import java.util.Date

@Entity(tableName = "users")
data class UserEntity @JvmOverloads constructor(
    @PrimaryKey val userId: String = "",
    val name: String = "",
    val email: String = "",
    val puskesmas: String = "",
    val role: String = "",
    val bdate: Date? = null,
    val gender: String = "",
    val address: String = "",
    val phone: String = "",
    var imageUrl: String = "",
    var localImagePath: String? = null
) {
    fun toUser(): User {
        return User(
            userId = userId,
            name = name,
            email = email,
            puskesmas = puskesmas,
            role = role,
            bdate = bdate,
            gender = gender,
            address = address,
            phone = phone,
            imageUrl = imageUrl,
            localImagePath = localImagePath
        )
    }

    companion object {
        fun fromUser(user: User): UserEntity {
            return UserEntity(
                userId = user.userId,
                name = user.name,
                email = user.email,
                puskesmas = user.puskesmas,
                role = user.role,
                bdate = user.bdate,
                gender = user.gender,
                address = user.address,
                phone = user.phone,
                imageUrl = user.imageUrl,
                localImagePath = user.localImagePath
            )
        }
    }
}


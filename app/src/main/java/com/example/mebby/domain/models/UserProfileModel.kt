package com.example.mebby.domain.models

import android.os.Parcel
import android.os.Parcelable
import com.example.mebby.domain.models.city.CityModel

data class UserProfileModel(
    val KYC: Boolean = false,
    val about: String?,
    val birthday: Long,
    val city: CityModel,
    val dateOfRegistration: Long,
    val find: String?,
    val gender: String?,
    val interests: List<InterestModel>,
    val phoneNumber: String?,
    val generalImage: String? = null,
    val profileImage: List<ImageModel>?,
    val show: String?,
    val username: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readLong(),
        TODO("city"),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        TODO("interests"),
        parcel.readString(),
        parcel.readString(),
        TODO("profileImage"),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (KYC) 1 else 0)
        parcel.writeString(about)
        parcel.writeLong(birthday)
        parcel.writeLong(dateOfRegistration)
        parcel.writeString(find)
        parcel.writeString(gender)
        parcel.writeString(phoneNumber)
        parcel.writeString(show)
        parcel.writeString(username)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfileModel> {
        override fun createFromParcel(parcel: Parcel): UserProfileModel {
            return UserProfileModel(parcel)
        }

        override fun newArray(size: Int): Array<UserProfileModel?> {
            return arrayOfNulls(size)
        }
    }
}
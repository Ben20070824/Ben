package com.example.ben.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "account")
    var account: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "nickname")
    var nickname: String = "昵称",

    @ColumnInfo(name = "gender")
    var gender: String = "性别",

    @ColumnInfo(name = "age")
    var age: Int = 0,

    @ColumnInfo(name = "birth")
    var birth: String = "生日",

    @ColumnInfo(name = "city")
    var city: String = "城市",

    @ColumnInfo(name = "university")
    var university: String = "大学",

    @ColumnInfo(name = "signature")
    var signature: String = "个性签名",
)

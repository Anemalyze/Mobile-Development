package com.insulin.app.data.model

data class Article(
    var image_url: String = "",
    var title: String = "",
    var provider_name: String = "",
    var url: String = ""
)
//import com.google.firebase.database.PropertyName

/*data class Article (

    @PropertyName("image_url")
    var image_url: String = "",

    @PropertyName("image_url")
    var title: String = "",

    @PropertyName("provider_name")
    var provider_name: String = "",

    @PropertyName("url")
    var url: String = "",
)*/

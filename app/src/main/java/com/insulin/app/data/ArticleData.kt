package com.insulin.app.data

data class AnemiaArticle(
    val imageUrl: String,
    val title: String,
    val providerName: String,
    val url: String
)

object ArticleData {
    val anemiaArticles = listOf(
        AnemiaArticle(
            imageUrl = "https://ayosehat.kemkes.go.id/imagex/content/2eceaa08anemia.png",
            title = "Mengenal Gejala Anemia pada Remaja",
            providerName = "Kementerian Kesehatan",
            url = "https://ayosehat.kemkes.go.id/mengenal-gejala-anemia-pada-remaja"
        ),
        AnemiaArticle(
            imageUrl = "https://cdn.hellosehat.com/wp-content/uploads/2016/05/359002766.jpg?w=828&q=75",
            title = "Gejala Anemia dari yang Paling Umum hingga Khas per Jenisnya",
            providerName = "Hellosehat",
            url = "https://hellosehat.com/kelainan-darah/anemia/gejala-anemia/"
        ),
        AnemiaArticle(
            imageUrl = "https://res.cloudinary.com/dk0z4ums3/image/upload/v1591606969/attached_image/anemia-0-alodokter.jpg",
            title = "Gejala Anemia yang Harus Diketahui",
            providerName = "Alodokter",
            url = "https://www.alodokter.com/anemia"
        ),
        AnemiaArticle(
            imageUrl = "https://dinkes.kalteng.go.id/wp-content/uploads/2024/09/3-18.png",
            title = "CEGAH ANEMIA REMAJA SEJAK DINI",
            providerName = "Dinkes Kalteng",
            url = "https://dinkes.kalteng.go.id/berita/cegah-anemia-remaja-sejak-dini/"
        ),
        AnemiaArticle(
            imageUrl = "https://www.mitrakeluarga.com/_next/image?url=https%3A%2F%2Fd3uhejzrzvtlac.cloudfront.net%2Fcompro%2FarticleDesktop%2Ff97e2dad-ee59-4533-b12f-89b01495489b.webp&w=1920&q=100",
            title = "Mengenal Anemia Aplastik, Penyakit Kurang Darah karena Autoimun yang Langka",
            providerName = "Mitra Keluarga",
            url = "https://www.mitrakeluarga.com/artikel/anemia-aplastik"
        ),
        AnemiaArticle(
            imageUrl = "https://dk4fkkwa4o9l0.cloudfront.net/production/uploads/article/image/2507/Anemia.png",
            title = "Alami 5L? Waspada Anemia!",
            providerName = "Hermina Hospital",
            url = "https://herminahospitals.com/id/articles/alami-5l-waspada-anemia.html"
        ),
        AnemiaArticle(
            imageUrl = "https://assets.unileversolutions.com/v1/49571714.jpg?im=Resize,width=1100,height=486",
            title = "Bukan Sekadar Kurang Darah, Kenali Penyebab Lain Anemia",
            providerName = "lifebuoy",
            url = "https://www.lifebuoy.co.id/semua-artikel/berita-kesehatan/bukan-sekadar-kurang-darah--kenali-penyebab-lain-anemia.html"
        )
    )
}

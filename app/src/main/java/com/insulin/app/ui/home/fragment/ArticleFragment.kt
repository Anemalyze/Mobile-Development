package com.insulin.app.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.data.ArticleData
import com.insulin.app.data.model.Article
import com.insulin.app.databinding.FragmentArticleBinding
import com.insulin.app.utils.Helper

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    private val listArticle: ArrayList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)

        // Load artikel dari ArticleData
        loadAnemiaArticles()

        // Tampilkan artikel di RecyclerView
        Helper.loadArticleData(
            context = requireContext(),
            rv = binding.rvArticle,
            articleList = listArticle,
            progressBar = binding.progressBarArticle
        )

        return binding.root
    }

    private fun loadAnemiaArticles() {
        // Bersihkan list sebelum menambahkan data baru
        listArticle.clear()

        // Tambahkan artikel ke dalam listArticle
        ArticleData.anemiaArticles.forEach { anemiaArticle ->
            val article = Article(
                image_url = anemiaArticle.imageUrl,
                title = anemiaArticle.title,
                provider_name = anemiaArticle.providerName,
                url = anemiaArticle.url
            )
            listArticle.add(article)
        }
    }
}

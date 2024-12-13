package com.insulin.app.ui.home.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.insulin.app.R
import com.insulin.app.adapter.article.ArticleAdapter
import com.insulin.app.data.ArticleData
import com.insulin.app.data.model.Article
import com.insulin.app.databinding.FragmentHomeBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.maps.MapsActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val listArticle: ArrayList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Set up shortcut buttons
        setupShortcutButtons()

        // Load articles into RecyclerView
        setupArticleRecyclerView()

        // Set up "Lihat Lainnya" button
        setupSeeMoreButton()

        return binding.root
    }

    private fun setupShortcutButtons() {
        val context = requireContext()
        binding.let {
            /* Rumah Sakit Terdekat */
            it.shortuctHospital.setOnClickListener {
                if (Helper.isPermissionGranted(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    val intent = Intent(context, MapsActivity::class.java)
                    startActivity(intent)
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), Constanta.LOCATION_PERMISSION_CODE
                    )
                }
            }

            /* Deteksi Anemia */
            it.shortuctDetection.setOnClickListener {
                (activity as? MainActivity)?.redirectToDetectAnemia()
            }
        }
    }

    private fun setupArticleRecyclerView() {
        // Clear the list and add only the first 3 articles
        listArticle.clear()
        ArticleData.anemiaArticles.take(3).forEach {
            listArticle.add(
                Article(
                    title = it.title,
                    provider_name = it.providerName,
                    image_url = it.imageUrl,
                    url = it.url
                )
            )
        }

        // Initialize the adapter and bind it to the RecyclerView
        val articleAdapter = ArticleAdapter(listArticle, R.drawable.custom_background_rv)
        binding.rvArticle.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }

        // Hide ProgressBar after the RecyclerView is ready
        binding.progressBarArticle.isVisible = false
    }

    private fun setupSeeMoreButton() {
        binding.btnMoreArticle.setOnClickListener {
            (activity as? MainActivity)?.selectMenu(R.id.navigation_article)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

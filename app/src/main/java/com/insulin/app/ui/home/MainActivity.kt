package com.insulin.app.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.ui.home.fragment.ArticleFragment
import com.insulin.app.ui.home.fragment.HistoryFragment
import com.insulin.app.ui.home.fragment.HomeFragment
import com.insulin.app.ui.home.fragment.ProfileFragment
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* init layout */
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        /* hide abnormal layer in bottom nav */
        activityMainBinding.bottomNavigationView.background = null

        /* disable dark mode */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /* set action for bottom nav */
        activityMainBinding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    switchFragment(fragmentHome)
                    true
                }
                R.id.navigation_article -> {
                    switchFragment(fragmentArticle)
                    true
                }
                R.id.navigation_history -> {
                    switchFragment(fragmentHistory)
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(fragmentProfile)
                    true
                }
                else -> false
            }
        }

        /* button detection clicked */
        activityMainBinding.fab.setOnClickListener { redirectToDetectAnemia() }

        /* init firebase remote config for force user to update app while there updates */
        initRemoteConfig()

        switchFragment(fragmentHome)
    }

    /* init remote config to check updates -> force user to update app if there updates */
    private fun initRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 21600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        checkAppUpdates()
    }

    fun redirectToDetectAnemia() =
        startActivity(Intent(this@MainActivity, DetectionActivity::class.java))

    fun selectMenu(itemId: Int) {
        activityMainBinding.bottomNavigationView.selectedItemId = itemId
    }

    @SuppressLint("SetTextI18n")
    private fun checkAppUpdates() {
        // Implementation for checking updates (if necessary)
    }

    /* switch current fragments into new fragments */
    private fun switchFragment(fragment: Fragment) = supportFragmentManager
        .beginTransaction()
        .replace(activityMainBinding.container.id, fragment)
        .commit()

    /* request a permission */
    fun requestPermission(permissions: Array<String>, permissionCode: Int) {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    /* handle permissions results */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constanta.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(this, "Berikan aplikasi izin mengakses kamera")
                }
            }
            Constanta.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin lokasi untuk membaca lokasi"
                    )
                }
            }
            Constanta.STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin storage untuk membaca dan menyimpan story"
                    )
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        /* init main activity fragments */
        val fragmentHome = HomeFragment()
        val fragmentArticle = ArticleFragment()
        val fragmentHistory = HistoryFragment()
        val fragmentProfile = ProfileFragment()
    }
}

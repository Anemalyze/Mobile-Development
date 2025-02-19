package com.insulin.app.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.insulin.app.R
import com.insulin.app.adapter.article.ArticleAdapter
import com.insulin.app.data.model.Article
import com.insulin.app.data.model.Detection
import com.insulin.app.ui.webview.WebViewActivity
import java.text.SimpleDateFormat
import java.util.*


@Suppress("SameParameterValue")
object Helper {

    fun notifyGivePermission(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun isPermissionGranted(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun openSettingPermission(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }

    /* -------------------------
       * GEOLOCATION & GEOCODER
       * ------------------------- */

    /* parse lat lon coordinate into readable address */
    fun parseAddressLocation(
        context: Context,
        lat: Double,
        lon: Double
    ): String {
        val geocoder = Geocoder(context)
        val geoLocation =
            geocoder.getFromLocation(lat, lon, 1)
        return if (geoLocation.size > 0) {
            val location = geoLocation[0]
            location.getAddressLine(0)
        } else {
            "Location Unknown"
        }
    }

    /* -------------------------
    *  CUSTOM DIALOG
    * ------------------------- */

    fun parseBooleanAnswerDetection(answer: Boolean): String {
        return if (answer) "Ya" else "Tidak"
    }

    fun parseGenderAnswerDetection(answer: Boolean): String {
        return if (answer) "Pria" else "Wanita"
    }

    /* get greetings for home fragment -> Selamat pagi / siang / sore / malam */
    @SuppressLint("SimpleDateFormat")
    fun getWelcomeGreetings(name: String): String {
        val c: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH")
        val status = when (sdf.format(c.time).toInt()) {
            in 0..10 -> "Selamat Pagi, "
            in 11..14 -> "Selamat Siang, "
            in 15..17 -> "Selamat Sore, "
            in 18..23 -> "Selamat Malam, "
            else -> "Selamat Datang, "
        }
        return StringBuilder(status).append(name).toString()
    }

    private fun removeDetectionHistoryFromDialog(
        context: Context,
        data: Detection,
        dialogDetection: Dialog
    ) {
        val alertdialogbuilder = AlertDialog.Builder(context)
        alertdialogbuilder.setTitle("Hapus Riwayat")
        alertdialogbuilder.setMessage("Apakah anda ingin menghapus data deteksi ini?")
        alertdialogbuilder.setPositiveButton("Ya") { alertDialog, _ ->
            alertDialog.dismiss()
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: Constanta.TEMP_UID
            FirebaseDatabase.getInstance().reference
                .child("detection_history")
                .child(uid)
                .child(data.detection_id).setValue(null).addOnSuccessListener {
                    dialogDetection.dismiss()
                    Toast.makeText(context, "Riwayat deteksi dihapus", Toast.LENGTH_SHORT)
                        .show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Error : ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
        }
        alertdialogbuilder.setNegativeButton("Tidak") { alertDialog, _ ->
            alertDialog.dismiss()
        }
        alertdialogbuilder.create().show()
    }

    /* dialog info builder for dialog instance invocation -> with customized ok button action*/
    fun dialogInfoBuilder(
        context: Context,
        title: String,
        body: String
    ): Dialog {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.window!!.apply {
            attributes.windowAnimations = android.R.transition.fade
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog.setContentView(R.layout.custom_dialog_info)
        val dialogTitle: TextView = dialog.findViewById(R.id.dialog_title)
        val dialogBody: TextView = dialog.findViewById(R.id.dialog_body)
        dialogTitle.text = title
        dialogBody.text = body
        return dialog
    }

    /* -------------------------
    *  STRING MANIPULATION
    * ------------------------- */


    /* generate some random string for general purposes */
    private fun getRandomString(len: Int = 20): String {
        val alphabet: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(len) { alphabet.random() }.joinToString("")
    }

    /* -------------------------
    *  DATE FORMAT
    * ------------------------- */
    private const val simpleDatePattern = "dd MMM yyyy HH.mm"
    private var defaultDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    private var simpletDate = SimpleDateFormat("dd MMM yyyy | HH:mm", Locale.getDefault())
    private var randomIdDate = SimpleDateFormat("yyMMdd_HHmmssSSSS", Locale.getDefault())

    /*
    * DATE INSTANCE
    * */

    /* current date as Date format */
    private fun getCurrentDate(): Date {
        return Date()
    }

    /* current date as String Format */
    fun getCurrentDateString(): String = defaultDate.format(getCurrentDate())

    fun reformatDateToSimpleDate(dateString: String): String {
        val dateValue = defaultDate.parse(dateString) as Date
        return simpletDate.format(dateValue)
    }

    fun reformatDateToSimpleDate(date: Date): String {
        return simpletDate.format(date)
    }


    /* build date format for diagnose ID purposes */
    private fun getRandomIdDateString(): String = randomIdDate.format(getCurrentDate())

    /* build diagnose ID format to Firebase DB (save history) */
    fun getUniqueUserRelatedId(uid: String): String {
        // Format Diagnose ID -> FirebaseUID_220530_1205001234_AbCDeFgHIj
        return StringBuilder(uid).append("_").append(getRandomIdDateString()).append("_")
            .append(getRandomString(10)).toString()
    }
    fun loadArticleData(
        context: Context,
        rv: RecyclerView,
        articleList: ArrayList<Article>,
        progressBar: ProgressBar
    ) {
        // Menampilkan daftar artikel
        val articleAdapter = ArticleAdapter(articleList)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = articleAdapter

        // Menyembunyikan ProgressBar setelah data artikel berhasil dimuat
        progressBar.visibility = View.GONE
    }

    /*fun loadArticleData(
        context: Context,
        rv: RecyclerView,
        articleList: ArrayList<Article>,
        reference: Query,
        reversed: Boolean? = false,
        progressBar: ProgressBar? = null,
    ) {
        progressBar?.isVisible = true
        val TAG = "FIREBASE"
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                articleList.clear()
                for (article in dataSnapshot.children) {
                    val data = article.getValue<Article>()
                    data?.let {
                        articleList.add(it)
                    }
                }
                rv.let {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(context)
                    it.isNestedScrollingEnabled = false
                    if (reversed == true) {
                        articleList.reverse()
                    }
                    it.adapter = ArticleAdapter(articleList)
                    progressBar?.isVisible = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.e(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }*/


//    fun loadHistoryData(
//        context: Context,
//        containerNeverDetecting: LinearLayout,
//        btnDetect: TextView,
//        progressBar: ProgressBar,
//        recyclerView: RecyclerView,
//        listHistory: ArrayList<Detection>,
//        btnMoreDetection: TextView? = null,
//        limit: Int? = null
//    ) {
//        progressBar.isVisible = true
//        val tag = "FIREBASE"
//        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: Constanta.TEMP_UID
//        val database =
//            if (limit != null) {
//                Firebase.database.reference.child("detection_history")
//                    .child(uid)
//                    .limitToLast(limit)
//            } else {
//                Firebase.database.reference.child("detection_history")
//                    .child(uid)
//            }
//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                listHistory.clear()
//                if (!dataSnapshot.hasChildren()) {
//                    btnMoreDetection?.isVisible = false
//                    containerNeverDetecting.isVisible = true
//                    progressBar.isVisible = false
//                    recyclerView.isVisible = false
//                    btnDetect.setOnClickListener {
//                        (context as MainActivity).redirectToDetectDiabetes()
//                    }
//                } else {
//                    btnMoreDetection?.isVisible = true
//                    containerNeverDetecting.isVisible = false
//                    for (history in dataSnapshot.children) {
//                        val data = history.getValue<Detection>()
//                        data?.let {
//                            listHistory.add(it)
//                        }
//                    }
//                    recyclerView.let {
//                        it.isVisible = true
//                        it.setHasFixedSize(true)
//                        it.layoutManager = LinearLayoutManager(context)
//                        it.isNestedScrollingEnabled = false
//                        listHistory.reverse()
//                        it.adapter = HistoryAdapter(listHistory)
//                    }
//                    progressBar.isVisible = false
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.e(tag, "loadPost:onCancelled", databaseError.toException())
//            }
//        })
//    }

    @SuppressLint("NewApi")
    fun getRupiahFormat(value: Int): String {
        val formatter: NumberFormat = DecimalFormat("###,###,###")
        return StringBuilder("Rp ").append(formatter.format(value).replace(",", ".")).toString()
    }

    fun openLinkInBrowser(context: Context, url: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }

    fun openLinkInWebView(context: Context, url: String) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, url)
        context.startActivity(intent)
    }

    fun versionCompare(v1: String, v2: String): Int {
        /* sources : https://www.geeksforgeeks.org/compare-two-version-numbers/ */
        var vnum1 = 0
        var vnum2 = 0
        var i = 0
        var j = 0
        while (i < v1.length
            || j < v2.length
        ) {
            while (i < v1.length
                && v1[i] != '.'
            ) {
                vnum1 = (vnum1 * 10
                        + (v1[i] - '0'))
                i++
            }
            while (j < v2.length
                && v2[j] != '.'
            ) {
                vnum2 = (vnum2 * 10
                        + (v2[j] - '0'))
                j++
            }
            if (vnum1 > vnum2) return 1
            if (vnum2 > vnum1) return -1
            vnum2 = 0
            vnum1 = vnum2
            i++
            j++
        }
        return 0
    }

}
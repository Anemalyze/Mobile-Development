package com.insulin.app.ui.home.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.insulin.app.databinding.FragmentProfileBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.reminderNotifications.ReminderNotificationsActivity

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser
        user?.let {
            Glide.with(this)
                .load(user.photoUrl)
                .into(binding.civUserProfile)
            binding.name.text = user.displayName
            binding.email.text = user.email
        }

        binding.btnLogout.setOnClickListener {
            // Logout tanpa fitur tambahan
            Firebase.auth.signOut()
            activity?.finish() // Opsional: menutup aktivitas jika diperlukan
        }

        binding.reminder.setOnClickListener {
            val intent = Intent((activity as MainActivity), ReminderNotificationsActivity::class.java)
            startActivity(intent)
        }

        binding.permission.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context?.packageName, null)
            startActivity(intent)
        }

        return binding.root
    }
}

package com.example.noteme.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.noteme.R
import com.example.noteme.ui.adapter.OnboardingAdapter
import com.google.android.material.button.MaterialButton

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnGetStarted: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        indicatorLayout = findViewById(R.id.indicatorLayout)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        btnGetStarted = findViewById(R.id.btnGetStarted)
        val btnSkip: TextView = findViewById(R.id.btnSkip)

        // Daftar gambar untuk 3 slide
        val onboardingImages = listOf(
            R.drawable.img_onboarding_2,
            R.drawable.img_onboarding_1,
            R.drawable.img_onboarding_3
        )

        val adapter = OnboardingAdapter(onboardingImages)
        viewPager.adapter = adapter

        // Memastikan indikator dan konten terisi saat pertama kali dibuka
        viewPager.post {
            updateContent(0)
            updateIndicators(0)
        }

        // Callback saat user menggeser layar
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateContent(position)
                updateIndicators(position)
            }
        })

        btnGetStarted.setOnClickListener {
            if (viewPager.currentItem == onboardingImages.size - 1) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }

        btnSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun updateContent(position: Int) {
        when (position) {
            0 -> {
                tvTitle.text = getString(R.string.onboarding_title_1)
                tvDescription.text = getString(R.string.onboarding_desc_1)
                btnGetStarted.text = getString(R.string.get_started)
            }
            1 -> {
                tvTitle.text = getString(R.string.onboarding_title_2)
                tvDescription.text = getString(R.string.onboarding_desc_2)
                btnGetStarted.text = getString(R.string.get_started)
            }
            2 -> {
                tvTitle.text = getString(R.string.onboarding_title_3)
                tvDescription.text = getString(R.string.onboarding_desc_3)
                btnGetStarted.text = getString(R.string.finish)
            }
        }
    }

    private fun updateIndicators(position: Int) {
        indicatorLayout.removeAllViews()
        val count = 3 // Jumlah total halaman onboarding

        for (i in 0 until count) {
            val view = View(this)
            
            // Konversi DP ke PX untuk ukuran titik
            val width = if (i == position) dpToPx(24) else dpToPx(8)
            val height = dpToPx(8)
            
            val layoutParams = LinearLayout.LayoutParams(width, height)
            layoutParams.setMargins(dpToPx(4), 0, dpToPx(4), 0)
            
            view.layoutParams = layoutParams
            view.background = ContextCompat.getDrawable(this, R.drawable.bg_circle)
            
            // Warna titik (Magenta jika aktif, Abu-abu jika tidak)
            val color = if (i == position) R.color.magenta_noteme else android.R.color.darker_gray
            view.backgroundTintList = ContextCompat.getColorStateList(this, color)
            
            indicatorLayout.addView(view)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
package com.example.noteme.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteme.R

class OnboardingAdapter(private val onboardingImages: List<Int>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onboardingImages[position])
    }

    override fun getItemCount(): Int = onboardingImages.size

    class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivOnboarding: ImageView = view.findViewById(R.id.ivOnboarding)

        fun bind(imageResId: Int) {
            ivOnboarding.setImageResource(imageResId)
        }
    }
}

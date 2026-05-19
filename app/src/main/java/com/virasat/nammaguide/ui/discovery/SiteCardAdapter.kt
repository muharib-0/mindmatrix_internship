package com.virasat.nammaguide.ui.discovery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virasat.nammaguide.data.model.HeritageSite
import com.virasat.nammaguide.databinding.ItemSiteCardBinding

/**
 * RecyclerView adapter for the site discovery grid.
 * Uses ListAdapter with DiffUtil for efficient updates.
 */
class SiteCardAdapter(
    private val onSiteClick: (siteId: String) -> Unit
) : ListAdapter<HeritageSite, SiteCardAdapter.SiteCardViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiteCardViewHolder {
        val binding = ItemSiteCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SiteCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SiteCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SiteCardViewHolder(
        private val binding: ItemSiteCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(site: HeritageSite) {
            binding.textSiteName.text = site.nameEn
            binding.textTagline.text = site.taglineEn
            binding.textDistance.text = String.format("%.1f km", site.distanceKm)

            // Set placeholder color based on site id for visual distinction
            val colorRes = when (site.id) {
                "KA001" -> com.virasat.nammaguide.R.color.site_color_1
                "KA002" -> com.virasat.nammaguide.R.color.site_color_2
                "KA003" -> com.virasat.nammaguide.R.color.site_color_3
                "KA004" -> com.virasat.nammaguide.R.color.site_color_4
                else -> com.virasat.nammaguide.R.color.site_color_5
            }
            binding.imageSiteThumbnail.setBackgroundColor(
                binding.root.context.getColor(colorRes)
            )

            // Site initial letter as placeholder text
            binding.textSiteInitial.text = site.nameEn.first().toString()

            binding.root.setOnClickListener { onSiteClick(site.id) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HeritageSite>() {
            override fun areItemsTheSame(oldItem: HeritageSite, newItem: HeritageSite) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HeritageSite, newItem: HeritageSite) =
                oldItem == newItem
        }
    }
}

package com.virasat.nammaguide.ui.passport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.virasat.nammaguide.data.db.CheckInEntity
import com.virasat.nammaguide.databinding.ItemPassportStampBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for the Passport screen.
 * Displays each check-in as a stamp-style card.
 */
class PassportAdapter :
    ListAdapter<CheckInEntity, PassportAdapter.PassportViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassportViewHolder {
        val binding = ItemPassportStampBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PassportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassportViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PassportViewHolder(
        private val binding: ItemPassportStampBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        fun bind(checkIn: CheckInEntity) {
            binding.textStampSiteName.text = checkIn.siteName
            binding.textStampTimestamp.text = dateFormatter.format(Date(checkIn.timestamp))

            // Show stamp number (position + 1)
            binding.textStampNumber.text = "#${absoluteAdapterPosition + 1}"
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckInEntity>() {
            override fun areItemsTheSame(oldItem: CheckInEntity, newItem: CheckInEntity) =
                oldItem.siteId == newItem.siteId

            override fun areContentsTheSame(oldItem: CheckInEntity, newItem: CheckInEntity) =
                oldItem == newItem
        }
    }
}

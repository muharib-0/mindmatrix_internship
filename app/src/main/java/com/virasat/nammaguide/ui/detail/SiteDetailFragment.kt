package com.virasat.nammaguide.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.virasat.nammaguide.R
import com.virasat.nammaguide.databinding.FragmentSiteDetailBinding
import com.virasat.nammaguide.viewmodel.CheckInViewModel
import com.virasat.nammaguide.viewmodel.SiteViewModel

/**
 * SiteDetailFragment — the Information Hub for a single heritage site.
 *
 * Features:
 * - Receives siteId via Safe Args (from discovery or QR scanner)
 * - Displays name, description, architectural note, local legend
 * - Language toggle: EN ↔ KN
 * - Audio guide play/pause (MediaPlayer managed in ViewModel.onCleared)
 * - "Check In" button persists visit to Room DB
 */
class SiteDetailFragment : Fragment() {

    private var _binding: FragmentSiteDetailBinding? = null
    private val binding get() = _binding!!

    private val args: SiteDetailFragmentArgs by navArgs()
    private val siteViewModel: SiteViewModel by viewModels()
    private val checkInViewModel: CheckInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        siteViewModel.loadSite(args.siteId)

        observeSite()
        observeAudioState()
        observeCheckInState()
        setupClickListeners()
    }

    private fun observeSite() {
        siteViewModel.currentSite.observe(viewLifecycleOwner) { site ->
            if (site == null) return@observe

            // Prepare audio
            siteViewModel.prepareAudio(site.audioResId)

            // Default: show English content
            updateContent(siteViewModel.isKannada.value ?: false)
        }

        // Language toggle reactivity
        siteViewModel.isKannada.observe(viewLifecycleOwner) { isKannada ->
            updateContent(isKannada)
        }
    }

    private fun updateContent(isKannada: Boolean) {
        val site = siteViewModel.currentSite.value ?: return

        binding.apply {
            textSiteName.text = if (isKannada) site.nameKn else site.nameEn
            textDescription.text = if (isKannada) site.descriptionKn else site.descriptionEn
            textArchitecturalNote.text = if (isKannada) site.architecturalNoteKn else site.architecturalNote
            textLocalLegend.text = if (isKannada) site.localLegendKn else site.localLegend
            textHiddenFact.text = if (isKannada) site.hiddenFactKn else site.hiddenFact

            // Update toggle button label
            btnToggleLanguage.text = if (isKannada)
                getString(R.string.label_switch_english)
            else
                getString(R.string.label_switch_kannada)

            // Site thumbnail placeholder color
            val colorRes = when (site.id) {
                "KA001" -> R.color.site_color_1
                "KA002" -> R.color.site_color_2
                "KA003" -> R.color.site_color_3
                "KA004" -> R.color.site_color_4
                else -> R.color.site_color_5
            }
            imageSiteDetail.setBackgroundColor(requireContext().getColor(colorRes))
            textSiteDetailInitial.text = site.nameEn.first().toString()
        }
    }

    private fun observeAudioState() {
        siteViewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            binding.btnAudioGuide.text = if (isPlaying)
                getString(R.string.label_pause_audio)
            else
                getString(R.string.label_play_audio)

            binding.btnAudioGuide.setIconResource(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )
        }
    }

    private fun observeCheckInState() {
        checkInViewModel.isCheckedIn(args.siteId).observe(viewLifecycleOwner) { isCheckedIn ->
            binding.btnCheckIn.isEnabled = !isCheckedIn
            binding.btnCheckIn.text = if (isCheckedIn)
                getString(R.string.label_already_checked_in)
            else
                getString(R.string.label_check_in)
        }
    }

    private fun setupClickListeners() {
        binding.btnAudioGuide.setOnClickListener {
            siteViewModel.toggleAudio()
        }

        binding.btnToggleLanguage.setOnClickListener {
            siteViewModel.toggleLanguage()
        }

        binding.btnCheckIn.setOnClickListener {
            val site = siteViewModel.currentSite.value ?: return@setOnClickListener
            checkInViewModel.checkIn(site)
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_checked_in, site.nameEn),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

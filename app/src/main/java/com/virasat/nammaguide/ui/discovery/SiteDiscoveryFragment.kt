package com.virasat.nammaguide.ui.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.virasat.nammaguide.databinding.FragmentSiteDiscoveryBinding
import com.virasat.nammaguide.viewmodel.SiteViewModel

/**
 * SiteDiscoveryFragment — the home/discovery screen.
 *
 * Shows a 2-column grid of heritage site cards.
 * Data is simulated (hardcoded in SiteRepository).
 * Tapping a card navigates to SiteDetailFragment via Safe Args.
 */
class SiteDiscoveryFragment : Fragment() {

    private var _binding: FragmentSiteDiscoveryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SiteViewModel by viewModels()
    private lateinit var adapter: SiteCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSiteDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SiteCardAdapter { siteId ->
            val action = SiteDiscoveryFragmentDirections
                .actionSiteDiscoveryFragmentToSiteDetailFragment(siteId)
            findNavController().navigate(action)
        }
        binding.recyclerViewSites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewSites.adapter = adapter
        adapter.submitList(viewModel.allSites)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

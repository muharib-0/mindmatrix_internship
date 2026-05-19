package com.virasat.nammaguide.ui.passport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.virasat.nammaguide.databinding.FragmentPassportBinding
import com.virasat.nammaguide.viewmodel.CheckInViewModel

/**
 * PassportFragment — the user's Travel Passport / Check-In history.
 *
 * Shows all sites visited (persisted in Room DB).
 * Updates reactively via Flow → LiveData → Observer.
 * Badge count in toolbar reflects total check-ins.
 */
class PassportFragment : Fragment() {

    private var _binding: FragmentPassportBinding? = null
    private val binding get() = _binding!!

    private val checkInViewModel: CheckInViewModel by viewModels()
    private lateinit var passportAdapter: PassportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passportAdapter = PassportAdapter()
        binding.recyclerViewPassport.adapter = passportAdapter

        checkInViewModel.allCheckIns.observe(viewLifecycleOwner) { checkIns ->
            if (checkIns.isEmpty()) {
                binding.recyclerViewPassport.visibility = View.GONE
                binding.layoutEmptyPassport.visibility = View.VISIBLE
            } else {
                binding.recyclerViewPassport.visibility = View.VISIBLE
                binding.layoutEmptyPassport.visibility = View.GONE
                passportAdapter.submitList(checkIns)
            }

            // Update badge count
            binding.textBadgeCount.text = checkIns.size.toString()
            binding.textStampsLabel.text = if (checkIns.size == 1)
                "1 stamp collected"
            else
                "${checkIns.size} stamps collected"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

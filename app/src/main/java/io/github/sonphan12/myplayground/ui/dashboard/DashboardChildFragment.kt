package io.github.sonphan12.myplayground.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import io.github.sonphan12.myplayground.R
import io.github.sonphan12.myplayground.databinding.FragmentDashBoardChildBinding
import io.github.sonphan12.myplayground.databinding.FragmentHomeBinding
import io.github.sonphan12.myplayground.databinding.FragmentHomeChildBinding
import io.github.sonphan12.myplayground.ui.MyBaseFragment

class DashboardChildFragment : MyBaseFragment() {

    private var _binding: FragmentDashBoardChildBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
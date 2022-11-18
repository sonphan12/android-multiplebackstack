package io.github.sonphan12.myplayground.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.sonphan12.myplayground.databinding.FragmentHomeChild2Binding
import io.github.sonphan12.myplayground.ui.MyBaseFragment

class HomeChild2Fragment : MyBaseFragment() {

    private var _binding: FragmentHomeChild2Binding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeChild2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
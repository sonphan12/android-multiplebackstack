package io.github.sonphan12.myplayground.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.sonphan12.myplayground.MainActivity
import io.github.sonphan12.myplayground.R
import io.github.sonphan12.myplayground.databinding.FragmentHomeBinding
import io.github.sonphan12.myplayground.ui.MyBaseFragment

class HomeFragment : MyBaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToHomeChild.setOnClickListener {
//            onBtnHomeChildClick()
//            onBtnHomeChildClickJetpackNav()

            (requireActivity() as MainActivity).navigator.openFragmentInCurrentTab(HomeChildFragment(), "homechild",
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)
//                true,
//                R.anim.slide_in_right,
//                R.anim.slide_out_left,
//                R.anim.slide_in_left,
//                R.anim.slide_out_right)
        }
    }

    private fun onBtnHomeChildClickJetpackNav() {
        findNavController().navigate(R.id.actionFromHomeToHomeChild)
    }

    private fun onBtnHomeChildClick() {
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            val frag = HomeChildFragment()
            replace(R.id.nav_host_fragment_activity_main_custom, frag)
//            Log.d("SONNE", "addToBackStack: home")
            addToBackStack("home")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
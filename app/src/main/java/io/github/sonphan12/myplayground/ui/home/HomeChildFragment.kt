package io.github.sonphan12.myplayground.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import io.github.sonphan12.myplayground.MainActivity
import io.github.sonphan12.myplayground.R
import io.github.sonphan12.myplayground.databinding.FragmentHomeBinding
import io.github.sonphan12.myplayground.databinding.FragmentHomeChildBinding
import io.github.sonphan12.myplayground.ui.MyBaseFragment

class HomeChildFragment : MyBaseFragment() {

    private var _binding: FragmentHomeChildBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("SONNE", "onCreate: $savedInstanceState")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        Log.d("SONNE", "onViewCreated: $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
        binding.btnToHomeChild2.setOnClickListener {
            (requireActivity() as MainActivity).navigator.openFragmentInCurrentTab(HomeChild2Fragment(), "homádechild",
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right)

//            requireActivity().supportFragmentManager.commit {
//                setReorderingAllowed(true)
//                setCustomAnimations(
//                    R.anim.slide_in_right,
//                    R.anim.slide_out_left,
//                    R.anim.slide_in_left,
//                    R.anim.slide_out_right
//                )
//                val frag = HomeChild2Fragment()
//                replace(R.id.nav_host_fragment_activity_main_custom, frag)
//                addToBackStack(null)
//            }

//            findNavController().navigate(R.id.actionFromHomeToHomeChild2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("SONNE", "onDestroy, isStateSaved: $isStateSaved")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        Log.d("SONNE", "onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        Log.d("SONNE", "onViewStateRestored: $savedInstanceState")
    }
}
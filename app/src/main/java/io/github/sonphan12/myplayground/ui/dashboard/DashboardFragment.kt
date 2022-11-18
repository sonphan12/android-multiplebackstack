package io.github.sonphan12.myplayground.ui.dashboard

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
import io.github.sonphan12.myplayground.databinding.FragmentDashboardBinding
import io.github.sonphan12.myplayground.ui.MyBaseFragment
import io.github.sonphan12.myplayground.ui.home.HomeChildFragment

class DashboardFragment : MyBaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToDashboardChild.setOnClickListener {
//            onBtnToDashboardChildClickJetPackNav()
//            onBtnToDashboardChildClick()
            (requireActivity() as MainActivity).navigator.openFragmentInCurrentTab(
                DashboardChildFragment(),
                "chiadasd",
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }

    private fun onBtnToDashboardChildClickJetPackNav() {
        findNavController().navigate(R.id.actionFromDashboardToDashboardChild)
    }

    private fun onBtnToDashboardChildClick() {
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            val frag = DashboardChildFragment()
            replace(R.id.nav_host_fragment_activity_main_custom, frag)
//            Log.d("SONNE", "addToBackStack: null")
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
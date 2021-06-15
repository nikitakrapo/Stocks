package com.nikitakrapo.android.stocks.view.news

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nikitakrapo.android.stocks.R
import com.nikitakrapo.android.stocks.databinding.FragmentNewsContainerBinding
import com.nikitakrapo.android.stocks.utils.ConnectionLiveData

class NewsContainerFragment : Fragment() {

    companion object{
        private const val TAG = "NewsContainerFragment"
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tabNames: List<String>
    private lateinit var binding: FragmentNewsContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentNewsContainerBinding>(
            inflater, R.layout.fragment_news_container, container, false
        )
        viewPager = binding.viewPager
        viewPager.adapter = NewsFragmentAdapter(this.requireActivity())

        tabLayout = binding.tabLayout

        binding.lifecycleOwner = this

        val connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "$it")
            binding.noInternetBanner.visibility = if (it) View.GONE else View.VISIBLE
        })

        initTabNames()

        TabLayoutMediator(tabLayout, viewPager) {tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return binding.root
    }

    private fun initTabNames(){
        tabNames =
            (viewPager.adapter as NewsFragmentAdapter).getTabNamesResourceIds().map{ stringResId ->
                getString(stringResId)
            }
    }
}
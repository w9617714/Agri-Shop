package com.example.agrishop.fragments.fragment_Shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agrishop.adapter.HomeViewPagerAdapter
import com.example.agrishop.databinding.FragmentHomeBinding
import com.example.agrishop.fragments.categoryFragment.Agricultural_InputsFragment
import com.example.agrishop.fragments.categoryFragment.EquipmentFragment
import com.example.agrishop.fragments.categoryFragment.MainCategory
import com.example.agrishop.fragments.categoryFragment.SeedsFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment() {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val categoriesFragment= arrayListOf<Fragment>(
                MainCategory(),
            SeedsFragment(),
            EquipmentFragment(),
            Agricultural_InputsFragment()
        )


        val viewPager2Adapter=HomeViewPagerAdapter(categoriesFragment,childFragmentManager,lifecycle)
        binding.viewpager.adapter=viewPager2Adapter
        TabLayoutMediator(binding.tab,binding.viewpager){tab,position->
                when(position){
                    0->tab.text="Main"
                    1->tab.text="Seeds"
                    2->tab.text="Equipments"
                    3->tab.text=" Inputs"
                }
        }.attach()
    }
}
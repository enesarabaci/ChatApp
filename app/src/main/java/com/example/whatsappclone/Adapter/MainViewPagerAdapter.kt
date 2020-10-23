package com.example.whatsappclone.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsappclone.View.LoginFragment
import com.example.whatsappclone.View.MainActivity
import com.example.whatsappclone.View.RegisterFragment

class MainViewPagerAdapter(fm : FragmentManager, val activity : MainActivity) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        var fragment : Fragment
        if (position == 0) {
            fragment = LoginFragment(activity)
        }else {
            fragment = RegisterFragment(activity)
        }
        return fragment
    }

    override fun getCount(): Int {
        return 2
    }

}
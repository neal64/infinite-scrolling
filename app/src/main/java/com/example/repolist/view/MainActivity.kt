package com.example.repolist.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.repolist.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = NavHostFragment.create(R.navigation.mobile_nav)
        supportFragmentManager.beginTransaction().add(R.id.navigation_view, host).setPrimaryNavigationFragment(host).commit()
    }

}


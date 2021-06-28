package com.project.commoditiesCurrentPrice.userInterface.activity

import android.os.Bundle
import com.project.commoditiesCurrentPrice.R
import com.project.commoditiesCurrentPrice.databinding.ActivityMainBinding
import com.project.commoditiesCurrentPrice.userInterface.base.BaseActivity
import com.project.commoditiesCurrentPrice.userInterface.fragment.MainFragment

class MainActivity : BaseActivity() {

    override fun onCreate(bundle : Bundle?) {
        super.onCreate(bundle)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.sample_content_fragment, MainFragment.getInstance(binding)).commit()
    }
}


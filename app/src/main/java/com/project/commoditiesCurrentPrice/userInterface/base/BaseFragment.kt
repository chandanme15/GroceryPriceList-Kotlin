package com.project.commoditiesCurrentPrice.userInterface.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.project.commoditiesCurrentPrice.repository.Repository

abstract class BaseFragment<T : ViewModel> : Fragment() {
    protected var viewModel : T? = null

    protected abstract fun createViewModel() : T?

    override fun onAttach(context : Context){
        super.onAttach(context);
        viewModel = createViewModel();
    }
}


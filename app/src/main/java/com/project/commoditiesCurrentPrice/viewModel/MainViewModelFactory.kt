package com.project.commoditiesCurrentPrice.viewModel

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.project.commoditiesCurrentPrice.repository.Repository;


class MainViewModelFactory(private val repository: Repository?, private val listener: MainViewModel.DatabaseProcessInterface) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, listener) as T
        }
        throw IllegalArgumentException ("Unknown view model class")
    }
}


package com.project.commoditiesCurrentPrice.userInterface.fragment

import android.content.ContentValues.TAG
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.commoditiesCurrentPrice.MyApplication

import com.project.commoditiesCurrentPrice.R;
import com.project.commoditiesCurrentPrice.adapter.MainAdapter;
import com.project.commoditiesCurrentPrice.databinding.ActivityMainBinding;
import com.project.commoditiesCurrentPrice.databinding.MainFragmentBinding;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.userInterface.base.BaseFragment;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.utils.Util;
import com.project.commoditiesCurrentPrice.viewModel.MainViewModel;
import com.project.commoditiesCurrentPrice.viewModel.MainViewModelFactory;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.util.Objects;


class MainFragment(private val activityMainBinding : ActivityMainBinding):
        BaseFragment<MainViewModel>(), SwipeRefreshLayout.OnRefreshListener, MainViewModel.DatabaseProcessInterface {

    private var mainFragmentBinding : MainFragmentBinding? = null
    private lateinit var mAdapter : MainAdapter
    private lateinit var mLayoutManager : RecyclerView.LayoutManager
    private var m_bIsAPIDataLoading = false
    private var m_bIsDataLoadedFromDB = false
    private lateinit var mOnScrollListener : RecyclerView.OnScrollListener

    companion object {
        fun getInstance(binding : ActivityMainBinding) = MainFragment(binding)
    }

    override fun createViewModel() : MainViewModel? {
        val factory = MainViewModelFactory(Repository.instance, this);
        return activity?.let { ViewModelProvider(it, factory).get(MainViewModel::class.java)};
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        mainFragmentBinding = MainFragmentBinding.inflate(inflater, container, false)
        return mainFragmentBinding?.root;
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        intVariables();
        setupListeners();
        setupRecycler();
        setLiveDataObserver();
        LoadData();
    }

    private fun initViews() {
        mainFragmentBinding?.swipeRefreshLayout?.setColorSchemeResources(android.R.color.black);
        showLoading(true);
        showError(false);
    }

    private fun intVariables() {
        m_bIsAPIDataLoading = false;
        m_bIsDataLoadedFromDB = false;
        Constants.PAGE_COUNT = 1;
    }

    private fun setupListeners() {
        mainFragmentBinding?.swipeRefreshLayout?.setOnRefreshListener(this);
        activityMainBinding.retryButton.setOnClickListener(this::retry);
        mOnScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView : RecyclerView, newState : Int) {
                if (m_bIsAPIDataLoading) {
                    return;
                }
                val totalItemCount = mLayoutManager.itemCount
                val lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition();

                if (totalItemCount > 1 && lastVisibleItem >= totalItemCount - 1) {
                    if (Util.isNetworkAvailable(MyApplication.instance)) {
                        if (!m_bIsDataLoadedFromDB) {
                            Constants.PAGE_COUNT++;
                        }
                        displaySnackbar(false, getString(R.string.loading));
                        LoadRecordsFromAPI();
                    }
                    //else displaySnackbar(true,"No internet Connection ! ");
                }
            }
        }
    }

    private fun setLiveDataObserver() {
        viewModel?.apiData?.observe(getViewLifecycleOwner(), {
            m_bIsAPIDataLoading = false;
            if (Constants.PAGE_COUNT == 1) {
                mAdapter.clearData();
                this@MainFragment.displaySnackbar(false, this@MainFragment.getString(R.string.data_updated));
                m_bIsDataLoadedFromDB = false;
            }
            mAdapter.addData(it);
            this@MainFragment.updateRefreshLayout(false);
            viewModel?.insertRecordsToDatabase(it, Constants.PAGE_COUNT);
        });
        viewModel?.error?.observe(getViewLifecycleOwner(), {
            m_bIsAPIDataLoading = false;
            if (it) {
                displaySnackbar(true, getString(R.string.cant_load_records));
                updateRefreshLayout(false);
                if (mAdapter.getData().isEmpty()) {
                    showError(true);
                }
            }
        });
    }

    private fun LoadData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                when {
                    viewModel?.bIsDatabaseExpired() == false -> {
                        m_bIsAPIDataLoading = false
                        viewModel?.bIsLoadDataFromDatabase()
                    }
                    Util.isNetworkAvailable(MyApplication.instance) -> LoadRecordsFromAPI()
                    else -> {
                        m_bIsAPIDataLoading = false
                        displaySnackbar(true, getString(R.string.network_not_available_error))
                        viewModel?.bIsLoadDataFromDatabase()
                    }
                }
            } catch (e : Exception) {
                e.printStackTrace();
                displaySnackbar(true, getString(R.string.some_error_occurred));
            }
        }
    }

    private fun LoadRecordsFromAPI() {
        m_bIsAPIDataLoading = true;
        viewModel?.loadRecords();
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater) {
        inflater.inflate(R.menu.menu_bar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean{
        when(item.itemId) {
            R.id.menu_sort_by_price -> mAdapter.sortDataByModalPrice()
            R.id.menu_sort_by_states -> mAdapter.sortDataByState()
            R.id.menu_refresh -> {
                mainFragmentBinding?.recyclerView?.scrollToPosition(0)
                onRefresh();
            }
            else -> super.onOptionsItemSelected(item);
        }
        return true;
    }

    private fun setupRecycler() {
        mLayoutManager = LinearLayoutManager(MyApplication.instance)
        mAdapter = MainAdapter()
        val recyclerView = mainFragmentBinding?.recyclerView
        recyclerView?.layoutManager = mLayoutManager
        recyclerView?.adapter = mAdapter
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.scrollToPosition(0)
        recyclerView?.addItemDecoration(DividerItemDecoration(MyApplication.instance, DividerItemDecoration.VERTICAL))
        recyclerView?.addOnScrollListener(mOnScrollListener)
    }

    private fun retry(view : View) {
        Constants.PAGE_COUNT = 1
        m_bIsDataLoadedFromDB = false
        showLoading(true)
        showError(false)
        LoadData()
    }

    override fun onRefresh() {
        Constants.PAGE_COUNT = 1;
        updateRefreshLayout(true);
        m_bIsDataLoadedFromDB = false;

        //During Refresh we try to fetch data from Web first
        if (Util.isNetworkAvailable(MyApplication.instance)) {
            LoadRecordsFromAPI();
        } else {
            LoadData();
        }
    }

    override fun onReadRecordFromDBCompleted(recordList : List<Record>?) {
        viewModel?.viewModelScope?.launch(Dispatchers.Main){
            when {
                recordList?.isEmpty() == false -> {
                    m_bIsDataLoadedFromDB = true;
                    if (Constants.PAGE_COUNT == 1) {
                        mAdapter.clearData();
                    }
                    mAdapter.addData(recordList);
                    showError(false);
                    updateRefreshLayout(false);
                }
                Util.isNetworkAvailable(MyApplication.instance) -> LoadRecordsFromAPI()
                else -> {
                    showError(true);
                    updateRefreshLayout(false);
                }
            }
        }
    }

    private fun updateRefreshLayout(refresh : Boolean) {
        if (!refresh) {
            showLoading(refresh);
        }
        mainFragmentBinding?.swipeRefreshLayout?.setRefreshing(refresh);
    }

    private fun showError(visibility : Boolean) {
        activityMainBinding.error.visibility = if(visibility) View.VISIBLE else View.GONE
        activityMainBinding.retryButton.visibility = if(visibility) View.VISIBLE else View.GONE
    }

    private fun showLoading(visibility : Boolean) {
        activityMainBinding.loading.visibility = if(visibility) View.VISIBLE else View.GONE
    }

    private fun displaySnackbar(isError : Boolean, message : String) {
        Util.showSnack(mainFragmentBinding?.root as View, isError, message);
    }

    override fun onDestroyView() {
        super.onDestroyView();
        mainFragmentBinding = null
    }

    override fun onDestroy() {
        super.onDestroy();
        viewModel?.clearRepo();
    }

    fun printTestLog() {
        Log.d(TAG, "TestLog");
    }
}
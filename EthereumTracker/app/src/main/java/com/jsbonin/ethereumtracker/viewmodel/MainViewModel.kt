package com.jsbonin.ethereumtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val ethereumTickerModel : MutableLiveData<Double> = MutableLiveData(0.0)

}
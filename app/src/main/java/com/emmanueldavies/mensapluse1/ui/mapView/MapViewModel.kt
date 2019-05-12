package com.emmanueldavies.mensapluse1.ui.mapView

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.emmanueldavies.mensapluse1.Utility.ICityNameGeoCoder
import com.emmanueldavies.mensapluse1.Utility.INetworkManager
import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.LocationData
import com.emmanueldavies.mensapluse1.domain.interactor.LoadCanteenUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.spacenoodles.makingyourappreactive.viewModel.state.ViewState
import javax.inject.Inject

class MapViewModel  @Inject constructor(
    private var geoCoder: ICityNameGeoCoder,
    private var netWorkManager: INetworkManager,
    private var loadCanteenUseCase: LoadCanteenUseCase,
application: Application

) :
    AndroidViewModel(application) {
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var hasInternet = false
    var canteenNames: MutableLiveData<MutableList<String>> = MutableLiveData()
    var canteens: MutableLiveData<List<Canteen>> = MutableLiveData()
    var state: MutableLiveData<ViewState> = MutableLiveData()


    fun getCanteens(locationData: LocationData) {
        addSub(

            geoCoder.convertLatLonToCityName(
                locationData.Latitude,
                locationData.Longitude
            )?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())?.subscribe({ cityName ->

                    locationData.cityName = cityName
                    netWorkManager.hasInternetConnection().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({

                            loadCanteenUseCase.execute(CanteenObserver(), locationData)

                        },{
                            state.postValue(ViewState.noInternet())

                        })

                },
                    {
                        state.postValue(ViewState.noLocationFound())

                    })

        )

    }

    private inner class CanteenObserver : DisposableSingleObserver<List<Canteen>>() {

        override fun onSuccess(t: List<Canteen>) {
            state.postValue(ViewState.loading())
            canteens.postValue(t.toMutableList())
        }

        override fun onError(e: Throwable) {
            state.postValue(ViewState.noInternet())
        }
    }



    @Synchronized
    private fun addSub(disposable: Disposable?) {
        if (disposable == null) return
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }
}


package io.spacenoodles.makingyourappreactive.viewModel.state


data class MainActivityState private constructor(val status: Status,
                                   val error: Throwable? = null) {
    companion object {

        fun loading(): MainActivityState {
            return MainActivityState(Status.LOADING)
        }

        fun success(): MainActivityState {
            return MainActivityState(Status.SUCCESS)
        }

        fun complete(): MainActivityState {
            return MainActivityState(Status.COMPLETE)
        }

        fun noLocationFound(): MainActivityState {
            return MainActivityState(Status.NO_LOCATION_FOUND)
        }

        fun noInternet(): MainActivityState {
            return MainActivityState(Status.NO_INTERNET)
        }

        fun error(error: Throwable): MainActivityState {
            return MainActivityState(Status.ERROR, error)
        }
    }
}
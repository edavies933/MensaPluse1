package io.spacenoodles.makingyourappreactive.viewModel.state


data class ViewState private constructor(val status: Status,
                                         val error: Throwable? = null) {
    companion object {

        fun loading(): ViewState {
            return ViewState(Status.LOADING)
        }

        fun success(): ViewState {
            return ViewState(Status.SUCCESS)
        }

        fun noDataFound(): ViewState {
            return ViewState(Status.NO_DATA_FOUND)
        }

        fun noLocationFound(): ViewState {
            return ViewState(Status.NO_LOCATION_FOUND)
        }

        fun noInternet(): ViewState {
            return ViewState(Status.NO_INTERNET)
        }

        fun error(error: Throwable): ViewState {
            return ViewState(Status.ERROR, error)
        }
    }
}
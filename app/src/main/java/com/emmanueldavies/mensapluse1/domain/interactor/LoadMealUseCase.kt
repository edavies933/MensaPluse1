package com.emmanueldavies.mensapluse1.domain.interactor

import com.emmanueldavies.mensapluse1.domain.model.Meal
import com.emmanueldavies.mensapluse1.data.resipotory.IRepository
import com.emmanueldavies.mensapluse1.domain.interactor.base.SingleUseCase
import com.emmanueldavies.mensapluse1.domain.model.MenuAtDate
import com.usecase.reactiveusecasessample.domain.executor.PostExecutionThread
import com.usecase.reactiveusecasessample.domain.executor.ThreadExecutor
import io.reactivex.Single
import javax.inject.Inject

class LoadMealUseCase
@Inject
constructor(
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread,
    private val contentRepository: IRepository
) : SingleUseCase<List<Meal>, MenuAtDate>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseSingle(menuAtDate: MenuAtDate?): Single<List<Meal>> {

        if (menuAtDate!!.hasInternet) {
            return contentRepository.getMealsByCanteenId(menuAtDate.canteenId, menuAtDate.date).map {

                for (meal in it) {
                    meal.name = meal.name?.trim()
                }
                it
            }.toSingle()
        }
        return contentRepository.getMealDirectlyFromDb(menuAtDate.canteenId, menuAtDate.date).toSingle()
    }
}
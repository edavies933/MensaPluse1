package com.emmanueldavies.newMensaplus.resipotory

import com.emmanueldavies.mensapluse1.domain.model.Canteen
import com.emmanueldavies.mensapluse1.domain.model.Meal
import com.emmanueldavies.mensapluse1.data.rooom.CanteenDao
import com.emmanueldavies.mensapluse1.data.rooom.MensaDao
import io.reactivex.Maybe


class LocalDataSource(private val mensaDao: MensaDao, private val canteenDao: CanteenDao) :
    ILocalDataSource {

    override fun saveMealsToDataBase(meals: List<Meal>) {
//        mensaDao.deleteMealOnDate(meals[0]?.canteenId,meals[0]?.date)

        mensaDao.getAllMeal().subscribe()

        for (meal in meals) {
            mensaDao.insert(meal)
        }
    }

    override fun saveCanteensToDataBase(canteens: List<Canteen>) {
        canteenDao.deleteAllEntries()
        for (canteen in canteens) {
            canteenDao.insert(canteen)
        }
    }

    override fun queryForMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return mensaDao.getMealsWithCanteenIdAndDate(canteenId, date)
    }

    override fun queryForCanteensWithCity(cityName: String): Maybe<List<Canteen>> {

        return canteenDao.getCanteenWithCity(cityName)
    }
}

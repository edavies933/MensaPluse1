package com.example.emmanueldavies.newMensaplus.resipotory
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.rooom.CanteenDao
import com.example.emmanueldavies.mensapluse1.rooom.MensaDao
import io.reactivex.Maybe


class LocalDataSource(private val mensaDao: MensaDao,private val canteenDao: CanteenDao) :
    ILocalDataSource {

    override fun saveMealsToDataBase(meals: List<Meal>) {
        for (meal in meals) {
            mensaDao.insert(meal)
        }
    }

    override fun saveCanteensToDataBase(canteens: List<Canteen>) {
        for (canteen in canteens) {
            canteenDao.insert(canteen)
        }
    }

    override fun queryForMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return mensaDao.getMealsWithCanteenIdAndDate(canteenId, date)
    }

    override fun queryForCanteensWithCity(cityName:String): Maybe<List<Canteen>>{

        return canteenDao.getCanteenWithCity(cityName)
    }
}

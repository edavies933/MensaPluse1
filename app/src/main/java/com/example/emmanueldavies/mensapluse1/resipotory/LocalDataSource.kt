package com.example.emmanueldavies.newMensaplus.resipotory
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.rooom.MealDao
import io.reactivex.Maybe


class LocalDataSource(private val mealDao: MealDao) :
    ILocalDataSource {

    override fun saveMealsToDataBase(meals: List<Meal>) {
        for (meal in meals) {
            mealDao.insert(meal)
        }
    }

    override fun queryForMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return mealDao.getMealsWithCanteenIdAndDate(canteenId, date)
    }
}

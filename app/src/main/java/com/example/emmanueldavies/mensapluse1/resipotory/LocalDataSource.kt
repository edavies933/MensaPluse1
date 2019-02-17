package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.rooom.MealDao
import com.example.emmanueldavies.mensapluse1.rooom.MealDatabase
import io.reactivex.Maybe

class LocalDataSource(val mealDao: MealDao) :
    IDataSource<List<Canteen>> {

    fun  saveMealToDataBase (meal: Meal){

        mealDao.insert(meal)
    }

    fun  getAllMealsFromDb () : Maybe<Meal> {

        return  mealDao.getAllMeals()
    }
}
package com.emmanueldavies.mensapluse1.Utility

import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.Meal
import com.emmanueldavies.mensapluse1.databinding.RecyclerViewItemBinding
import com.emmanueldavies.mensapluse1.databinding.SpinnerItemBinding
import java.util.*

class MealAdapter(var listOfMeals: MutableList<Meal>) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MealViewHolder {
        val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return MealViewHolder(binding)
    }

    override fun getItemCount(): Int =
        listOfMeals.count()


    override fun onBindViewHolder(viewHolder: MealViewHolder, position: Int) = viewHolder.bind(listOfMeals[position])

    fun clearAdapter() {
        listOfMeals.clear()
        notifyDataSetChanged()
    }

    class MealViewHolder(private val binding: com.emmanueldavies.mensapluse1.databinding.RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Meal) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

            binding.meal = item
            binding.colorView.setBackgroundColor(color)
            binding.executePendingBindings()
        }


    }
}

class CanteenAdapter(var listOfCanteen: MutableList<Canteen>) :
    RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder>() {

    var selectedCanteen: MutableLiveData<Canteen> = MutableLiveData()
    private lateinit var binding: SpinnerItemBinding

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CanteenViewHolder {
         binding = SpinnerItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)


        return CanteenViewHolder(binding)
    }

    override fun getItemCount(): Int =
        listOfCanteen.count()


    override fun onBindViewHolder(viewHolder: CanteenViewHolder, position: Int) {

        binding.spinnerCanteenName.setOnClickListener {
            selectedCanteen.postValue(listOfCanteen[position])
        }

        binding.spinnerCanteenName.setOnClickListener {
            selectedCanteen.postValue(listOfCanteen[position])
        }
        viewHolder.bind(listOfCanteen[position])

    }

    fun clearAdapter() {
        listOfCanteen.clear()
        notifyDataSetChanged()
    }

    class CanteenViewHolder(private val binding: com.emmanueldavies.mensapluse1.databinding.SpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Canteen) {
            binding.canteen = item
        }

    }
}
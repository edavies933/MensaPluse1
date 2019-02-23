package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.view.MenuItem
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.support.v4.view.ViewPager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.emmanueldavies.mensapluse1.MensaAppViewModelFactory
import com.example.emmanueldavies.mensapluse1.R
import com.example.emmanueldavies.mensapluse1.data.LocationData


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    MenuListFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {
    private var canteemNames: MutableList<String> = mutableListOf()
    @Inject
    lateinit var mensaAppViewModelFactory: MensaAppViewModelFactory
    lateinit var mensaViewModel: MensaViewModel

    override fun onFragmentInteraction(uri: Uri) {
    }

    @Inject
lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.emmanueldavies.mensapluse1.R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(com.example.emmanueldavies.mensapluse1.R.drawable.ic_menu_black_24dp)

        }
        mensaViewModel = ViewModelProviders.of(this, mensaAppViewModelFactory).get(MensaViewModel::class.java)
        mensaViewModel.getCanteenNames(LocationData(52.393535, 13.127814))
        mensaViewModel.canteenNames.observe(this, Observer { m ->

            updateSpinnerTitles(this, m)
            if (m != null) {
                canteemNames = m
            }
        })
        mensaViewModel.repository.getCanteenDataWithCoordinates(LocationData(52.393535, 13.127814))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        for (i in 0 until 7)
        {
            var formattedDate  =  mensaViewModel.getFormatedTitleDate(i)
            adapter.addFragment(
                MenuListFragment.newInstance(mensaViewModel.getFormattedDayName(i)),
                formattedDate.substring(0,formattedDate.length - 5))
        }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener (object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                mensaViewModel.mealAdapter.listOfMeals.clear()
                var formattedDate  =  mensaViewModel.getFormatedTitleDate(p0)
              mensaViewModel.getMealAtACertainDateInFuture(formattedDate!!)

            }

            override fun onPageScrollStateChanged(p0: Int) {
            }

        })
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter   (manager) {
        private val mFragmentList = mutableListOf<Fragment>()
        private val mFragmentTitleList = mutableListOf<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList.get(position)
        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var canteenName = canteemNames[position]
        mensaViewModel.getMeals(canteenName)
        setupViewPager(viewpager)
        tabLayout.setupWithViewPager(viewpager)
    }

    companion object {
        private fun updateSpinnerTitles(activity: MainActivity, canteenNames: List<String>?) {
           var spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter(
               activity, R.layout.spinner_item,
               canteenNames
           )
           spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)


           activity?.spinner  ?.adapter = spinnerArrayAdapter
           activity?.spinner  ?.onItemSelectedListener = activity
       }
    }


}

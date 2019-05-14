package com.emmanueldavies.mensapluse1.presentation.ui.MensaView

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BaseTransientBottomBar.LENGTH_INDEFINITE
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import com.emmanueldavies.mensapluse1.BuildConfig
import com.emmanueldavies.mensapluse1.LocaionManager.ILocationDetector
import com.emmanueldavies.mensapluse1.Utility.MensaAppViewModelFactory
import com.emmanueldavies.mensapluse1.R
import com.emmanueldavies.mensapluse1.presentation.ui.MensaViewModel
import com.emmanueldavies.mensapluse1.presentation.ui.MenuListFragment
import com.emmanueldavies.mensapluse1.presentation.ui.aboutView.AboutActivity
import com.emmanueldavies.mensapluse1.presentation.ui.mapView.MapActivity
import com.jakewharton.rxrelay2.BehaviorRelay
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.subjects.BehaviorSubject
import io.spacenoodles.makingyourappreactive.viewModel.state.Status
import io.spacenoodles.makingyourappreactive.viewModel.state.ViewState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_menu_list.*
import javax.inject.Inject
open class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    MenuListFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var viewSnap: ViewSnap
    private var canteenNames: MutableList<String> = mutableListOf()
    @Inject
    lateinit var mensaAppViewModelFactory: MensaAppViewModelFactory
    lateinit var mensaViewModel: MensaViewModel
    var viewState: MutableLiveData<ViewState> = MutableLiveData()
    private var hasLocationPermission: Boolean = false


    companion object {
        const val NUMBER_OF_DAYS = 7
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
        const val TRAILING_CHARACTER = 5
        const val TAG = "MainActivity"
    }


    override fun onFragmentInteraction(uri: Uri) {
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var locationDetector: ILocationDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        swiperefresh.setOnRefreshListener {
            infoTextView.visibility = View.GONE
            locationDetector.getLastKnowLocation(this)
        }

        spinner.background.setColorFilter(resources.getColor(R.color.primary_material_light), PorterDuff.Mode.SRC_ATOP)
        nav_view.setNavigationItemSelectedListener(this);

        mensaViewModel = ViewModelProviders.of(this, mensaAppViewModelFactory).get(MensaViewModel::class.java)

        mensaViewModel.state.observe(
            this, Observer {
                updateView(it!!)
            }
        )
        locationDetector.getLastKnowLocation(this).observe(this, Observer {
            if (it == null) {

                updateView(ViewState.noLocationFound())


            } else {
                mensaViewModel.getCanteenNames(it)
                locationDetector.stopListeningToLocationUpdate()
            }

        })

        mensaViewModel.canteenNames.observe(this, Observer { canteenNames ->

            swiperefresh.isRefreshing = false
            updateSpinnerTitles(this, canteenNames)
            if (canteenNames != null) {
                this.canteenNames = canteenNames
            }
        })
    }



    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_about -> {

                var aboutActivityIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutActivityIntent)
            }

            R.id.nav_map -> {

                var mapActivityIntent = Intent(this, MapActivity::class.java)
                startActivity(mapActivityIntent)
            }

            R.id.nav_canteen -> {

                var canteenActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(canteenActivityIntent)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        nav_view.menu.getItem(1).isChecked = true

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




    fun  runAnimations (){

        var context = recyclerView.context
      var  controller : LayoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        for (i in 0 until NUMBER_OF_DAYS) {
            var formattedDate = mensaViewModel.getFormattedTitleDate(i)
                .substring(0, mensaViewModel.getFormattedTitleDate(i).length - TRAILING_CHARACTER)

            adapter.addFragment(
                MenuListFragment.newInstance(
                    mensaViewModel.getFormattedDayName(
                        i
                    )
                ),
                formattedDate
            )
        }
        viewPager.adapter = adapter
        viewPager.currentItem = mensaViewModel.currentTabNumber
        getMealsAtDate(mensaViewModel.currentTabNumber)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                updateView(ViewState.loading())
                if (mensaViewModel.currentTabNumber != p0) {
                    viewPager.invalidate()
                    mensaViewModel.currentTabNumber = p0
                    getMealsAtDate(p0)
                }
            }

            override fun onPageScrollStateChanged(p0: Int) {
            }

        })
    }

    private fun getMealsAtDate(p0: Int) {
        var formattedDate = mensaViewModel.getFormattedApiDate(p0)
        mensaViewModel.getMealAtACertainDateInFuture(formattedDate)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
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
            return mFragmentTitleList[position]
        }
    }


    private var spinnerOnItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            updateView(ViewState.loading())
            var canteenName = canteenNames[position]
            mensaViewModel.getMeals(canteenName)
            setupViewPager(viewpager)
            tabLayout.setupWithViewPager(viewpager)

            mensaViewModel.currentCanteen = position
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }


    private fun updateSpinnerTitles(activity: MainActivity, canteenNames: List<String>?) {

        if (canteenNames != null) {
            var spinnerArrayAdapter = CustomSpinnerAdapter(
                this,
                R.layout.spinner_item,
                canteenNames
            )

            activity.spinner?.adapter = spinnerArrayAdapter
            activity?.spinner?.onItemSelectedListener = spinnerOnItemSelectedListener

            spinner.setSelection(mensaViewModel.currentCanteen)
        }

    }

    private fun updateView(state: ViewState) {
        when (state.status) {
            Status.LOADING -> {
                progressBar.visibility = View.VISIBLE

            }

            Status.NO_LOCATION_FOUND -> {
                infoTextView.visibility = View.VISIBLE
                noInternetTexView.visibility = View.GONE
                infoTextView.text = this.getString(R.string.no_location_detected)
                progressBar.visibility = View.GONE
            }

            Status.SUCCESS -> {
                infoTextView.visibility = View.GONE
                noInternetTexView.visibility = View.GONE
                progressBar.visibility = View.GONE

            }

            Status.NO_DATA_FOUND -> {
                infoTextView.visibility = View.VISIBLE
                noInternetTexView.visibility = View.GONE
                infoTextView.text = this.getString(R.string.no_canteens_around_location)
                progressBar.visibility = View.GONE
                swiperefresh.isRefreshing = false

            }

            Status.NO_INTERNET -> {
                infoTextView.visibility = View.GONE
                noInternetTexView.visibility = View.VISIBLE
                noInternetTexView.text = this.getString(R.string.no_internet_message)
                progressBar.visibility = View.GONE
            }
        }
    }


    class ViewSnap @Inject constructor() {
        lateinit var snackBar: Snackbar

        fun showSnackBar(
            activity: Activity,
            snackStrId: Int,
            actionStrId: Int = 0,
            listener: View.OnClickListener? = null
        ) {


            snackBar = Snackbar.make(
                activity.findViewById(android.R.id.content), activity.getString(snackStrId),
                LENGTH_INDEFINITE
            )
            if (actionStrId != 0 && listener != null) {
                snackBar.setAction(activity.getString(actionStrId), listener)
            } else {

                snackBar.setAction(activity.getString(R.string.snackbar_ok_button)) { snackBar.dismiss() }
            }
            snackBar.show()
        }

    }


    /**
     * Callback received when a permissions request has been completed.
     */

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission was granted. Kick off the process of building and connecting
                    // GoogleApiClient.
                {

                    if (!hasLocationPermission) {
                        locationDetector.getLastKnowLocation(this)
                        hasLocationPermission = true
                    }

                }

                else -> // Permission denied.
                {
                    hasLocationPermission = false
                    viewSnap.showSnackBar(this, R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }

                            startActivity(intent)
                        })
                }
                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

            }
        }
    }


}


class CustomSpinnerAdapter : ArrayAdapter<String> {
    private var CustomSpinnerItems: List<String>

    constructor(context: Context, resource: Int, pickerItems: List<String>) : super(
        context,
        resource,
        pickerItems
    ) {
        this.CustomSpinnerItems = pickerItems
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return CustomSpinnerView(position, parent)
    }

    private fun CustomSpinnerView(position: Int, parent: ViewGroup): View {
        //Getting the Layout Inflater Service from the system
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //Inflating out custom spinner viewz
        val customView = layoutInflater.inflate(R.layout.spinner_item, parent, false)
        //Declaring and initializing the widgets in custom layout
        val textView = customView?.findViewById(R.id.spinner_canteen_name) as? TextView
        textView?.text = CustomSpinnerItems[position]
        return customView
    }


}
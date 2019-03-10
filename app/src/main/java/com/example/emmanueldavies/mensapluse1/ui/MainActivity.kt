package com.example.emmanueldavies.mensapluse1.ui

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BaseTransientBottomBar.LENGTH_INDEFINITE
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
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.emmanueldavies.mensapluse1.BuildConfig.APPLICATION_ID
import com.example.emmanueldavies.mensapluse1.LocaionManager.ILocationDetector
import com.example.emmanueldavies.mensapluse1.MensaAppViewModelFactory
import com.example.emmanueldavies.mensapluse1.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.spacenoodles.makingyourappreactive.viewModel.state.MainActivityState
import io.spacenoodles.makingyourappreactive.viewModel.state.Status
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    MenuListFragment.OnFragmentInteractionListener {
    @Inject
    lateinit var viewSnap: ViewSnap
    private var canteenNames: MutableList<String> = mutableListOf()
    @Inject
    lateinit var mensaAppViewModelFactory: MensaAppViewModelFactory
    lateinit var mensaViewModel: MensaViewModel
    var mainActivityState: MutableLiveData<MainActivityState> = MutableLiveData()

    var currentTabNumber = 0

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
        swiperefresh.setDistanceToTriggerSync(100)
        swiperefresh.setOnRefreshListener {

            locationDetector.getLastKnowLocation(this)
        }

        mensaViewModel = ViewModelProviders.of(this, mensaAppViewModelFactory).get(MensaViewModel::class.java)

        mensaViewModel.state.observe(
            this, Observer {
                updateView(it!!)
            }
        )
        locationDetector.getLastKnowLocation(this).observe(this, Observer {
            if (it == null) {

                updateView(MainActivityState.noLocationFound())
            } else {
                mensaViewModel.getCanteenNames(it)

            }

        })
//
        mensaViewModel.canteenNames.observe(this, Observer { canteenNames ->

            swiperefresh.isRefreshing = false
            updateSpinnerTitles(this, canteenNames)
            if (canteenNames != null) {
                this.canteenNames = canteenNames
            }
        })
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

        for (i in 0 until NUMBER_OF_DAYS) {
            var formattedDate = mensaViewModel.getFormattedTitleDate(i)
                .substring(0, mensaViewModel.getFormattedTitleDate(i).length - TRAILING_CHARACTER)

            adapter.addFragment(
                MenuListFragment.newInstance(mensaViewModel.getFormattedDayName(i)),
                formattedDate
            )
        }
        viewPager.adapter = adapter
        viewPager.currentItem = currentTabNumber
        getMealsAtDate(currentTabNumber)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
//                getMealsAtDate(0)

            }

            override fun onPageSelected(p0: Int) {

                if (currentTabNumber != p0) {
                    viewPager.invalidate()
                    currentTabNumber = p0
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


    var spinnerOnItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            var canteenName = canteenNames[position]
            mensaViewModel.getMeals(canteenName)
            setupViewPager(viewpager)
            tabLayout.setupWithViewPager(viewpager)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }


    private fun updateSpinnerTitles(activity: MainActivity, canteenNames: List<String>?) {

        if (canteenNames != null) {
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, canteenNames)


            var spinnerArrayAdapter = CustomSpinnerAdapter(this, R.layout.spinner_item, canteenNames)
//            var spinnerArrayAdapter    = ArrayAdapter(
//            activity,android.R.layout.simple_spinner_item,
//            canteenNames
//        )
//            spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            activity.spinner?.adapter = spinnerArrayAdapter
            activity?.spinner?.onItemSelectedListener = spinnerOnItemSelectedListener


        }

    }

    private fun updateView(state: MainActivityState) {
        when (state.status) {
            Status.LOADING -> {


            }

            Status.NO_LOCATION_FOUND -> {

                infoTextView.visibility = View.VISIBLE
                noInternetTexView.visibility = View.GONE
                infoTextView.text = this.getString(R.string.no_location_detected)
            }

            Status.SUCCESS -> {

                infoTextView.visibility = View.GONE
                noInternetTexView.visibility = View.GONE

            }

            Status.NO_DATA_FOUND -> {

                infoTextView.visibility = View.VISIBLE
                noInternetTexView.visibility = View.GONE
                infoTextView.text = this.getString(R.string.no_meal)

            }

            Status.NO_INTERNET -> {

                infoTextView.visibility = View.GONE
                noInternetTexView.visibility = View.VISIBLE
                noInternetTexView.text = this.getString(R.string.no_internet_message)

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

        fun dismissSnackBar() {
            if (snackBar?.isShown) {
                snackBar.dismiss()
            }
        }

    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")

                // Permission granted.
                (grantResults[0] == PERMISSION_GRANTED) -> locationDetector.getLastKnowLocation(this)

                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                else -> {
                    viewSnap.showSnackBar(this, R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
                }
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
        //Inflating out custom spinner view
        val customView = layoutInflater.inflate(R.layout.spinner_item, parent, false)
        //Declaring and initializing the widgets in custom layout
        val textView = customView?.findViewById(R.id.spinner_canteen_name) as? TextView
        textView?.text = CustomSpinnerItems[position]
        return customView
    }


}
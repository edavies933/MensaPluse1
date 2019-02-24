package com.example.emmanueldavies.mensapluse1.ui


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BaseTransientBottomBar.LENGTH_INDEFINITE
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.emmanueldavies.mensapluse1.BuildConfig.APPLICATION_ID
import com.example.emmanueldavies.mensapluse1.LocaionManager.LocationDetector
import com.example.emmanueldavies.mensapluse1.MensaAppViewModelFactory
import com.example.emmanueldavies.mensapluse1.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.spacenoodles.makingyourappreactive.viewModel.state.MainActivityState
import io.spacenoodles.makingyourappreactive.viewModel.state.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_menu_list.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    MenuListFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {
    private var canteemNames: MutableList<String> = mutableListOf()
    @Inject
    lateinit var mensaAppViewModelFactory: MensaAppViewModelFactory
    lateinit var mensaViewModel: MensaViewModel
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    var mainActivityState: MutableLiveData<MainActivityState> = MutableLiveData()

    override fun onFragmentInteraction(uri: Uri) {
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var locationDetector: LocationDetector

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




        mensaViewModel = ViewModelProviders.of(this, mensaAppViewModelFactory).get(MensaViewModel::class.java)

        mensaViewModel.state.observe(
            this, Observer {
                update(it!!)
            }
        )
        locationDetector.getLastKnowLocation(this).observe(this, Observer {

            mensaViewModel.getCanteenNames(it!!)


        })

        mensaViewModel.canteenNames.observe(this, Observer { canteenNames ->

            //            swiperefresh.isRefreshing = false
            updateSpinnerTitles(this, canteenNames)
            if (canteenNames != null) {
                canteemNames = canteenNames
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

        for (i in 0 until 7) {
            var formattedDate = mensaViewModel.getFormatedTitleDate(i)
            adapter.addFragment(
                MenuListFragment.newInstance(mensaViewModel.getFormattedDayName(i)),
                formattedDate.substring(0, formattedDate.length - 5)
            )
        }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {

                mensaViewModel.mealAdapter.listOfMeals.clear()
                var formattedDate = mensaViewModel.getFormatedTitleDate(p0)
                mensaViewModel.getMealAtACertainDateInFuture(formattedDate)

            }

            override fun onPageScrollStateChanged(p0: Int) {
            }

        })
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
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
            activity.spinner?.adapter = spinnerArrayAdapter
            activity?.spinner?.onItemSelectedListener = activity
        }
    }

    private fun update(state: MainActivityState) {
        when (state.status) {
            Status.LOADING -> {

                mainActivityState.postValue(state)
            }

            Status.NO_LOCATION_FOUND -> {
                mainActivityState.postValue(state)

            }

            Status.SUCCESS -> {
                mainActivityState.postValue(state)

            }

            Status.COMPLETE -> {
                mainActivityState.postValue(state)
            }

            Status.ERROR -> {
                mainActivityState.postValue(state)
            }
        }
    }


    /**
     * Shows a [Snackbar].
     *
     * @param snackStrId The id for the string resource for the Snackbar text.
     * @param actionStrId The text of the action item.
     * @param listener The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), getString(snackStrId),
            LENGTH_INDEFINITE
        )
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
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
                (grantResults[0] == PERMISSION_GRANTED) -> locationDetector.getLastLocation()

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
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
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

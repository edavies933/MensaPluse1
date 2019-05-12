package com.emmanueldavies.mensapluse1.ui.aboutView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.emmanueldavies.mensapluse1.BuildConfig
import com.emmanueldavies.mensapluse1.R
import kotlinx.android.synthetic.main.activity_about.*
import android.content.Intent
import android.net.Uri


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        about_toolbar.title = getString(R.string.aboutActivityToolBarTitle)
        setSupportActionBar(about_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        versionNumberValueId.text = BuildConfig.VERSION_NAME

        developerInfoLinkId.setOnClickListener {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.developer_link)))
            startActivity(browserIntent)
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

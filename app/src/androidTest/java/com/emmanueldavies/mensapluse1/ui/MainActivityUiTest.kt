package com.emmanueldavies.mensapluse1.ui


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import com.emmanueldavies.mensapluse1.R
import com.emmanueldavies.mensapluse1.presentation.ui.MensaView.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun verifyMenuTitle() {

        val textView = onView(
            allOf(
                withId(R.id.date_text_view), withText(R.string.todays_menu_title),
                childAtPosition(
                    withParent(withId(R.id.viewpager)),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText(R.string.todays_menu_title)))

        val tabView = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabLayout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())

        val viewPager = onView(
            allOf(
                withId(R.id.viewpager),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.swiperefresh),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        viewPager.perform(swipeLeft())

        val textView2 = onView(
            allOf(
                withId(R.id.date_text_view), withText(R.string.tomorrow_menu_title),
                childAtPosition(
                    withParent(withId(R.id.viewpager)),
                    0
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText(R.string.tomorrow_menu_title)))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    @Test
    fun verifySpinnerTitle(){
        onView(
            allOf(isAssignableFrom(TextView::class.java),
                withParent(isAssignableFrom(Spinner::class.java))))
            .check(matches(withId(R.id.spinner_canteen_name)))

    }

//    @Test
//    fun verifyMealItemIsOnView (){
//        onView(withId(R.id.recyclerView))
//            .perform(RecyclerViewActions.actionOnItemAtPosition<androidx.recyclerview.widget.RecyclerView.ViewHolder>(0,
//                click()))
//    }
}

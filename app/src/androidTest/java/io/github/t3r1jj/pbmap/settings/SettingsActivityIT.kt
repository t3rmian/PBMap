package io.github.t3r1jj.pbmap.settings

import android.content.Intent
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import io.github.t3r1jj.pbmap.R
import io.github.t3r1jj.pbmap.main.MapActivity
import io.github.t3r1jj.pbmap.main.drawer.MapsDrawerFragmentIT
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule
import io.github.t3r1jj.pbmap.testing.TestUtils.withIndex
import io.github.t3r1jj.pbmap.testing.TestUtils.withIntents
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SettingsActivityIT {

    private val mainActivityRule = ActivityTestRule(MapActivity::class.java, true, false)

    @Rule
    @JvmField
    val testRule: RuleChain = RuleChain
            .outerRule(mainActivityRule)
            .around(ScreenshotOnTestFailedRule())

    @Before
    fun setUp() {
        clearPreferences()
    }

    @After
    fun tearDown() {
        clearPreferences()
    }

    private fun clearPreferences() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        PreferenceManager.getDefaultSharedPreferences(ctx)
                .edit().clear().commit()
    }

    @Test
    @LargeTest
    fun testDrawer_Navigation_SettingsActivity_ChangeLanguage() {
        MapsDrawerFragmentIT.autoOpenDrawerReturningPreferences(true)
        mainActivityRule.launchActivity(Intent())
        for (i in 0..19) {
            onView(withIndex(withId(R.id.design_menu_item_text), 1)).perform(swipeUp())
        }
        withIntents {
            onView(withText(R.string.settings)).perform(click())
            intended(hasComponent(SettingsActivity::class.java.name))
        }
        onView(withText("Settings")).check(matches(isDisplayed()))

        onView(CheckBoxMatcher("English")).check(matches(isDisplayed()))
        onView(CheckBoxMatcher("English")).check(matches(isChecked()))
        onView(CheckBoxMatcher("English")).check(matches(not(isEnabled())))

        onView(CheckBoxMatcher("Polski")).check(matches(isDisplayed()))
        onView(CheckBoxMatcher("Polski")).check(matches(not(isChecked())))
        onView(CheckBoxMatcher("Polski")).check(matches(isEnabled()))

        onView(withText(io.github.t3r1jj.pbmap.R.string.LANGUAGES_pl)).perform(click())

        onView(CheckBoxMatcher("English")).check(matches(not(isChecked())))
        onView(CheckBoxMatcher("English")).check(matches(isEnabled()))

        onView(CheckBoxMatcher("Polski")).check(matches(isChecked()))
        onView(CheckBoxMatcher("Polski")).check(matches(not(isEnabled())))

        onView(withText("Opcje")).check(matches(isDisplayed()))
        pressBack()
        onView(withText("MapaPB")).check(matches(isDisplayed()))
    }

    class CheckBoxMatcher(private val text: String) : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("checkbox with text: $text")
        }

        override fun matchesSafely(item: View): Boolean {
            if (android.R.id.checkbox != item.id) {
                return false
            }
            if (item.parent is ViewGroup) {
                val parent = item.parent as ViewGroup
                if (parent.parent is ViewGroup) {
                    val grandParent = parent.parent as ViewGroup
                    for (i in 0..grandParent.childCount) {
                        val uncle = grandParent.getChildAt(i)
                        if (uncle is ViewGroup) {
                            for (j in 0..uncle.childCount) {
                                val cousin = uncle.getChildAt(j)
                                if (cousin is TextView) {
                                    if (text == cousin.text) {
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false
        }
    }
}
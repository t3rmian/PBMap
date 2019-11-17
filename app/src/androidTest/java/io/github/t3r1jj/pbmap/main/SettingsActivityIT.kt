package io.github.t3r1jj.pbmap.main

import android.app.SearchManager
import android.content.Intent
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import io.github.t3r1jj.pbmap.R
import io.github.t3r1jj.pbmap.main.MapActivitySearchIT.getUnFormattedString
import io.github.t3r1jj.pbmap.main.drawer.MapsDrawerFragmentIT
import io.github.t3r1jj.pbmap.settings.SettingsActivity
import io.github.t3r1jj.pbmap.testing.RetryRunner
import io.github.t3r1jj.pbmap.testing.ScreenshotOnTestFailedRule
import io.github.t3r1jj.pbmap.testing.TestUtils.withIndex
import io.github.t3r1jj.pbmap.testing.TestUtils.withIntents
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(RetryRunner::class)
class SettingsActivityIT {

    companion object {
        private const val TIMEOUT_MS = 3 * 60 * 1000L
    }

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

        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_default))).check(matches(isDisplayed()))
        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_default))).check(matches(isChecked()))
        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_default))).check(matches(not(isEnabled())))

        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_pl))).check(matches(isDisplayed()))
        onView(SettingsMatcher(android.R.id.checkbox, "Polski")).check(matches(not(isChecked())))
        onView(SettingsMatcher(android.R.id.checkbox, "Polski")).check(matches(isEnabled()))

        onView(withText(R.string.LANGUAGES_pl)).perform(click())

        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_default))).check(matches(not(isChecked())))
        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_default))).check(matches(isEnabled()))

        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_pl))).check(matches(isChecked()))
        onView(SettingsMatcher(android.R.id.checkbox, getUnFormattedString(R.string.LANGUAGES_pl))).check(matches(not(isEnabled())))

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        onView(withText("Opcje")).check(matches(isDisplayed()))
        pressBackUnconditionally()

        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wait(Until.findObject(By.text("MapaPB")), TIMEOUT_MS)
    }

    @Test
    @LargeTest
    fun testDrawer_Navigation_SettingsActivity_EnableDebug() {
        MapsDrawerFragmentIT.autoOpenDrawerReturningPreferences(true)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEARCH
        sendIntent.putExtra(SearchManager.QUERY, "wc@pb_wa")
        mainActivityRule.launchActivity(Intent(sendIntent))
        for (i in 0..19) {
            onView(withIndex(withId(R.id.design_menu_item_text), 1)).perform(swipeUp())
        }
        withIntents {
            onView(withText(R.string.settings)).perform(click())
            intended(hasComponent(SettingsActivity::class.java.name))
        }
        onView(withText("Settings")).check(matches(isDisplayed()))
        onView(withText(R.string.LANGUAGES_ja)).perform(swipeUp())
        onView(withText(R.string.LANGUAGES_ru)).perform(swipeUp())
        onView(withText(R.string.unit_system)).perform(swipeUp())
        onView(withText(R.string.debug)).perform(swipeUp())

        val debugText = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.show_all_routes)
        onView(SettingsMatcher(android.R.id.switch_widget, debugText)).check(matches(isDisplayed()))
        onView(SettingsMatcher(android.R.id.switch_widget, debugText)).check(matches(isEnabled()))
        onView(SettingsMatcher(android.R.id.switch_widget, debugText)).check(matches(not(isChecked())))

        val controller = mainActivityRule.activity.controller
        onView(SettingsMatcher(android.R.id.switch_widget, debugText)).perform(click())
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wait(Until.findObject(By.text("PBMap")), TIMEOUT_MS)
        assertFalse(controller == mainActivityRule.activity.controller)
    }

    class SettingsMatcher(private val interactiveId: Int, private val text: String) : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("checkbox with text: $text")
        }

        override fun matchesSafely(item: View): Boolean {
            if (interactiveId != item.id) {
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
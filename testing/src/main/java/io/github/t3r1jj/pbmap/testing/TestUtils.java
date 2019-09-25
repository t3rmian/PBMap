package io.github.t3r1jj.pbmap.testing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class TestUtils {
    public static Matcher<View> withMenuIdOrContentDescription(@IdRes int id, @StringRes int menuText) {
        Matcher<View> matcher = withId(id);
        try {
            onView(matcher).check(matches(isDisplayed()));
            return matcher;
        } catch (NoMatchingViewException noId) {
            try {
                openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
            } catch (NoMatchingViewException noBar) {
                return withContentDescription(menuText);
            }
        }
        return withContentDescription(menuText);
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex;
            int viewObjHash;

            @SuppressLint("DefaultLocale")
            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("with index: %d ", index));
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view) && currentIndex++ == index) {
                    viewObjHash = view.hashCode();
                }
                return view.hashCode() == viewObjHash;
            }
        };
    }

    public static void withIntents(Runnable runnable) {
        Intents.init();
        try {
            runnable.run();
        } finally {
            Intents.release();
        }
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("position " + childPosition + " of parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) return false;
                ViewGroup parent = (ViewGroup) view.getParent();

                return parentMatcher.matches(parent)
                        && parent.getChildCount() > childPosition
                        && parent.getChildAt(childPosition).equals(view);
            }
        };
    }

    public static class CaseInsensitiveSubstringMatcher extends TypeSafeMatcher<String> {

        private final String subString;

        private CaseInsensitiveSubstringMatcher(final String subString) {
            this.subString = subString;
        }

        @Override
        protected boolean matchesSafely(final String actualString) {
            return actualString.toLowerCase().contains(this.subString.toLowerCase());
        }

        @Override
        public void describeTo(final Description description) {
            description.appendText("containing substring \"" + this.subString + "\"");
        }

        @Factory
        public static Matcher<String> containsIgnoringCase(final String subString) {
            return new CaseInsensitiveSubstringMatcher(subString);
        }
    }

    public static void pressDoubleBack(UiDevice device) {
        try {
            device.waitForIdle();
            device.pressBack();
            device.waitForIdle();
            device.pressBack();
        } catch (Exception e) {
            Log.w("DoubleBackPress", e);
        }
    }

    public static void allowPermissionsIfNeeded(String permissionNeeded) throws UiObjectNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasNeededPermission(permissionNeeded)) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector()
                    .clickable(true)
                    .checkable(false)
                    .index(1));
            if (allowPermissions.exists()) {
                allowPermissions.click();
            }
        }
    }

    private static boolean hasNeededPermission(String permissionNeeded) {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int permissionStatus = ContextCompat.checkSelfPermission(context, permissionNeeded);
        return permissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    public static void allowMockLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand("appops set "
                    + getInstrumentation().getTargetContext().getPackageName()
                    + " android:mock_location allow");
        }
    }
}

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.test.espresso.action;

import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;

public final class ViewActionsExt {

    private ViewActionsExt() {
    }

    private static final float EDGE_FUZZ_FACTOR = 0.083f;

    /**
     * Returns an action that performs a swipe right-to-left across the vertical center of the view.
     * The swipe doesn't start at the very edge of the view, but is a bit offset.<br>
     * <br>
     * View constraints:
     *
     * <ul>
     *   <li>must be displayed on screen (1% at least)
     *       <ul>
     */
    public static ViewAction swipeLeftExt() {
        return actionWithAssertions(
                new GeneralSwipeActionExt(
                        Swipe.FAST,
                        GeneralLocation.translate(GeneralLocation.CENTER_RIGHT, -EDGE_FUZZ_FACTOR, 0),
                        GeneralLocation.CENTER_LEFT,
                        Press.FINGER));
    }

    /**
     *
     * Returns an action that performs a swipe left-to-right across the vertical center of the view.
     * The swipe doesn't start at the very edge of the view, but is a bit offset.<br>
     * <br>
     * View constraints:
     *
     * <ul>
     *   <li>must be displayed on screen (1% at least)
     *       <ul>
     */
    public static ViewAction swipeRightExt() {
        return actionWithAssertions(
                new GeneralSwipeActionExt(
                        Swipe.FAST,
                        GeneralLocation.translate(GeneralLocation.CENTER_LEFT, EDGE_FUZZ_FACTOR, 0),
                        GeneralLocation.CENTER_RIGHT,
                        Press.FINGER));
    }

    /**
     * Returns an action that performs a swipe top-to-bottom across the horizontal center of the view.
     * The swipe doesn't start at the very edge of the view, but has a bit of offset.<br>
     * <br>
     * View constraints:
     *
     * <ul>
     *   <li>must be displayed on screen (1% at least)
     *       <ul>
     */
    public static ViewAction swipeDownExt() {
        return actionWithAssertions(
                new GeneralSwipeActionExt(
                        Swipe.FAST,
                        GeneralLocation.translate(GeneralLocation.TOP_CENTER, 0, EDGE_FUZZ_FACTOR),
                        GeneralLocation.BOTTOM_CENTER,
                        Press.FINGER));
    }

    /**
     * Returns an action that performs a swipe bottom-to-top across the horizontal center of the view.
     * The swipe doesn't start at the very edge of the view, but has a bit of offset.<br>
     * <br>
     * View constraints:
     *
     * <ul>
     *   <li>must be displayed on screen (1% at least)
     *       <ul>
     */
    public static ViewAction swipeUpExt() {
        return actionWithAssertions(
                new GeneralSwipeActionExt(
                        Swipe.FAST,
                        GeneralLocation.translate(GeneralLocation.BOTTOM_CENTER, 0, -EDGE_FUZZ_FACTOR),
                        GeneralLocation.TOP_CENTER,
                        Press.FINGER));
    }

}

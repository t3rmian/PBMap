package io.github.t3r1jj.pbmap.search

import android.net.Uri
import android.util.Patterns
import androidx.annotation.Nullable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Utility for parsing in-app links
 */
class WebUriParser {
    companion object {

        /**
         * @return true if the parameter is a web URL
         */
        @JvmStatic
        fun isWebUrl(uri: String?): Boolean {
            return if (uri == null) {
                false
            } else {
                Patterns.WEB_URL.matcher(uri).matches()
            }
        }

        /**
         * @return a string query that can be passed to the [Search]
         */
        @Nullable
        @JvmStatic
        fun parseIntoCommonFormat(uri: Uri): String? {
            val startingSegmentDetected = AtomicBoolean()
            return uri.pathSegments
                    .filter {
                        if (!startingSegmentDetected.get()) {
                            if ("mobile" == it) {
                                startingSegmentDetected.getAndSet(true)
                            } else {
                                false
                            }
                        } else {
                            true
                        }
                    }
                    .take(2)
                    .reversed()
                    .joinToString("@")
                    .let {
                        if (it.isEmpty()) {
                            null
                        } else {
                            it
                        }
                    }
        }
    }
}
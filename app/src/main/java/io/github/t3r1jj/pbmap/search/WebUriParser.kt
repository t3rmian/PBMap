package io.github.t3r1jj.pbmap.search

import android.net.Uri
import androidx.annotation.Nullable
import java.util.concurrent.atomic.AtomicBoolean

class WebUriParser {
    companion object {
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
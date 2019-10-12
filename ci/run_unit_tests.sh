#!/bin/bash
if [ "$TEST_SUITE" != "medium" ] && [ "$TEST_SUITE" != "large" ] && [ "$TEST_SUITE" != "flaky" ]; then
    bundle exec fastlane unit_tests
fi
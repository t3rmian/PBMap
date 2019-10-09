#!/bin/bash
if [ "$TEST_SUITE" != "medium" ] && [ "$TEST_SUITE" != "large" ]; then
    bundle exec fastlane unit_tests
fi
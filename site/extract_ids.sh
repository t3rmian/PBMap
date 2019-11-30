#!/bin/bash

set -o errexit
set -o pipefail

if [[ $# == 0 ]]; then
    echo "Usage: $0 \$file"
    echo "* \$file: path to a xml file for which id attribute values will be extracted"
    exit 0
fi

grep -h 'id="' $1 | awk -F'id="' '{print $2}' | awk -F'"' '{print $1}' | awk '{print tolower($0)}' | tr - _ | tr / _ | tr ' ' _
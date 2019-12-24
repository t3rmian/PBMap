#!/bin/bash

set -o errexit
set -o pipefail

if [[ $# == 0 ]]; then
    echo "Usage: $0 \$@"
    echo "* \$@: multiple files present in the git repository for which a single hash will be returned"
    exit 0
fi

while read filename;
do
    meta="$meta$(git log -1 --format="%ct" -- ${filename}) $filename "
done <<< $(git ls-tree -r --name-only HEAD $@)
echo "$meta" | sha1sum | awk '{ print $1 }'
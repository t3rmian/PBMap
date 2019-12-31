#!/bin/bash

set -o errexit
set -o pipefail

if [[ $# == 0 ]]; then
    echo "Usage: $0 \$file \$index"
    echo "* \$file: path to a xml file for which a link will be created in build/\$index file"
    exit 0
fi

file=$1
index=$2
path=$(echo "$file" | cut -d/ -f2,3,4)
place=$(echo "$path" | cut -d/ -f3)@
space=$(echo "$path" | cut -d/ -f2)
if [[ "$space" == "index.html" ]]
then
    exit 0
fi
if [[ "$place" == "index.html@" ]]
then
    place=""
fi
link="$link<li class=\"hide-default\"><a href=\"/$path\" data-id=\"$place$space\"></a></li>"
echo ${link} > build/${index}
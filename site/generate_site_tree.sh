#!/bin/bash

set -o errexit
set -o pipefail

if [[ $# == 0 ]]; then
    echo "Usage: $0 \$file"
    echo "* \$file: path to a xml file for which a partial site tree will be generated"
    exit 0
fi

file=$1
echo "=== Processing $file ==="
map=$(./extract_ids.sh $file | head -n 1)
#echo "Generating public/mobile/$map"
mkdir -p "public/mobile/$map"
rootUrl="https://play.google.com/store/apps/details?id=io.github.t3r1jj.pbmap\&referrer=https%3A%2F%2Fpbmap.termian.dev%2Fmobile"
url="$rootUrl%2F$map"
sed "s|%URL%|$url|g" template.html > "public/mobile/$map/index.html"
for place in $(./extract_ids.sh $file | tail -n+2)
do
    #echo "Generating public/mobile/$map/$place"
   	mkdir -p "public/mobile/$map/$place"
    url="$rootUrl%2F$map%2F$place"
	sed "s|%URL%|$url|g" template.html > "public/mobile/$map/$place/index.html"
done
sed "s|%URL%|$rootUrl|g" template.html > "public/mobile/index.html"
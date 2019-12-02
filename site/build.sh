#!/bin/bash

rm -rf public
mkdir public

echo "=== Generating hash ==="
./hash.sh ../app/src/main/assets/data/* ./*.sh ./*.html ./*.png > public/.hash
cat public/.hash
echo "=== Downloading hash from remote ==="
curl https://pbmap.termian.dev/.hash > .hash
cat .hash
echo "=== Comparing hashes ==="
DIFF=$(diff .hash public/.hash)
if [[ "$DIFF" == "" ]]
then
    echo "=== The hashes are equal, exiting ==="
    exit 0
fi
echo "=== The hashes are different, continuing the build ==="
cp logo.png public/logo.png

PARALLELISM=8
for file in ../app/src/main/assets/data/*
do
    ((i=i%PARALLELISM)); ((i++==0)) && wait
    ./generate_site_tree.sh $file &
done
wait

echo "Generating main index..."
cp index.html public/index.html
for file in public/*.html public/*/*.html public/*/*/*.html public/*/*/*/*.html;
do
    path=$(echo "$file" | cut -d/ -f2,3,4)
	place=$(echo "$path" | cut -d/ -f3)@
	space=$(echo "$path" | cut -d/ -f2)
	if [[ "$space" == "index.html" ]]
    then
        continue
    fi
	if [[ "$place" == "index.html@" ]]
    then
        place=""
    fi
    link="$link<li class=\"hide-default\"><a href=\"/$path\">$place$space</a></li>"
done
sed -i "s|%APPEND%|$link&|g" public/index.html
echo "Finished generating main index"
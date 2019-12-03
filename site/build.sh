#!/bin/bash

rm -rf public
mkdir public

echo "=== Generating hash ==="
./hash.sh ../app/src/main/assets/data/* ./*.sh ./*.html ./*.png ../app/src/main/res/values*/*data.xml > public/.hash
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

echo "=== Generating main index... ==="
cp index.html public/index.html
for file in public/*.html public/*/*.html public/*/*/*.html public/*/*/*/*.html
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
    link="$link<li class=\"hide-default\"><a href=\"/$path\" data-id=\"$place$space\"></a></li>"
done
sed -i "s|%APPEND%|$link|g" public/index.html
echo "=== Finished generating main index ==="

echo "=== Preparing i18n files... ==="
for file in ../app/src/main/res/values*/*data.xml
do
    echo "=== Processing $file ==="
    values_name=$(echo "$file" | cut -d/ -f6)
    lang=$(echo "$values_name" | cut -d- -f2)
    region=$(echo "$values_name" | cut -d- -f3 | cut -c2- | awk '{print tolower($0)}')

    if [[ "$region" == "" ]]
    then
        hreflang=$lang
    else
        hreflang="$lang-$region"
    fi

    if [[ "$lang" == "values" ]]
    then
        sed '/<string name="address_/,/<\/string>/d' $file > public/i18n.xml
    else
        mkdir -p public/$hreflang
        cp public/index.html public/$hreflang/index.html
        sed '/<string name="address_/,/<\/string>/d' $file > public/$hreflang/i18n.xml
    fi
done
echo "=== Finished preparing i18n files ==="
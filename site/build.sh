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

echo "=== Generating main index, this might take a while... ==="
rm -rf build
mkdir -p build
for file in public/*/*.html public/*/*/*.html public/*/*/*/*.html
do
    ((j++%PARALLELISM==0)) && wait
    ./generate_links.sh $file $j &
done
wait
links=`cat build/*`
awk -v r="$links" '{gsub(/%APPEND%/,r)}1' index.html > public/index.html

echo "=== Finished generating main index ==="

echo "=== Preparing i18n links... ==="
for file in ../app/src/main/res/values*/*data.xml
do
    echo "=== Processing $file ==="
    values_name=$(echo "$file" | cut -d/ -f6)
    lang=$(echo "$values_name" | cut -d- -f2)
    region=$(echo "$values_name" | cut -d- -f3 | cut -c2- | awk '{print tolower($0)}')

    if [[ "$region" == "" ]]
    then
        hreflang=$lang
    elif [[ "$lang-$region" == "en-gb" ]]
    then
      continue
    else
        hreflang="$lang-$region"
    fi

    if [[ "$lang" == "values" ]]
    then
        hreflang="en-gb"
    fi
    langs[((k++))]=$hreflang
done
IFS=$'\n' sorted=($(sort <<<"${langs[*]}"))
unset IFS
for lang in "${langs[@]}"
do
    length=$((${#langs[@]}/2))
    if [[ "$((m++))" == "$length" ]]
    then
        nav="$nav<span></span>"
    fi
    if [[ "$lang" == "en-gb" ]]
    then
        nav="$nav<a hreflang=\"$lang\" href=\"/\">$lang</a>"
        hrefs="$hrefs<link rel=\"alternate\" hreflang=\"x-default\" href=\"/\"/>"
        hrefs="$hrefs<link rel=\"alternate\" hreflang=\"$lang\" href=\"/\"/>"
    else
        nav="$nav<a hreflang=\"$lang\" href=\"/$lang\">$lang</a>"
        hrefs="$hrefs<link rel=\"alternate\" hreflang=\"$lang\" href=\"/$lang\"/>"
    fi
done
sed -i "s|%I18N%|$nav|g" public/index.html
sed -i "s|%HREFS%|$hrefs|g" public/index.html
echo "=== Finished preparing i18n links ==="
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
    elif [[ "$lang-$region" == "en-gb" ]]
    then
        continue
    else
        hreflang="$lang-$region"
    fi

    if [[ "$lang" == "values" ]]
    then
        sed '/<string name="address_/,/<\/string>/d' $file > public/i18n.xml
    else
        mkdir -p public/$hreflang
        sed "s|%LANG%|$lang|g" public/index.html > public/$hreflang/index.html
        sed '/<string name="address_/,/<\/string>/d' $file > public/$hreflang/i18n.xml
    fi
done
sed -i "s|%LANG%|en-gb|g" public/index.html
echo "=== Finished preparing i18n files ==="

echo "=== Preparing title translations... ==="
for file in ../app/src/main/res/values*/*strings.xml
do
    echo "=== Processing $file ==="
    values_name=$(echo "$file" | cut -d/ -f6)
    lang=$(echo "$values_name" | cut -d- -f2)
    region=$(echo "$values_name" | cut -d- -f3 | cut -c2- | awk '{print tolower($0)}')

    if [[ "$region" == "" ]]
    then
        hreflang=$lang
    elif [[ "$lang-$region" == "en-gb" ]]
    then
        continue
    else
        hreflang="$lang-$region"
    fi

    title=`grep '<string name="name_app">' $file`
    subtitle=`grep '<string name="about_description">' $file`
    hint=`grep '<string name="search_hint">' $file`
    if [[ "$title" == "" ]]
    then
        title=`grep '<string name="name_app">' ../app/src/main/res/values/strings.xml`
    fi
    if [[ "$subtitle" == "" ]]
    then
        subtitle=`grep '<string name="about_description">' ../app/src/main/res/values/strings.xml`
    fi
    title=`echo "$title" | cut -d\> -f2 | cut -d\< -f1`
    subtitle=`echo "$subtitle" | cut -d\> -f2 | cut -d\< -f1`
    subtitle=${subtitle: : -1}
    hint=`echo "$hint" | cut -d\> -f2 | cut -d\< -f1`
    if [[ "$lang" == "values" ]]
    then
        sed -i "s|%TITLE%|$title|g" public/index.html
        sed -i "s|%SUBTITLE%|$subtitle|g" public/index.html
        sed -i "s|%HINT%|$hint|g" public/index.html
    else
        sed -i "s|%TITLE%|$title|g" public/$hreflang/index.html
        sed -i "s|%SUBTITLE%|$subtitle|g" public/$hreflang/index.html
        sed -i "s|%HINT%|$hint|g" public/$hreflang/index.html
    fi
done
echo "=== Finished title translations ==="
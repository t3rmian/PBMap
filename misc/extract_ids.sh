#!/bin/bash
grep -h 'id="' ../app/src/main/assets/data/* | awk -F'id="' '{print $2}' | awk -F'"' '{print $1}' | awk '{print tolower($0)}' | tr - _ | tr / _ | tr ' ' _ | sort | uniq > place_ids.txt
grep [a-z] place_ids.txt > named_place_ids.txt
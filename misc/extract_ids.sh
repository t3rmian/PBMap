#!/bin/bash
grep -h 'id="' ../app/src/main/assets/data/* | awk -F'id="' '{print $2}' | awk -F'"' '{print $1}' | awk '{print tolower($0)}' | sort | uniq > place_ids.txt

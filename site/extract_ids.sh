#!/bin/bash

grep -h 'id="' $1 | awk -F'id="' '{print $2}' | awk -F'"' '{print $1}' | awk '{print tolower($0)}' | tr - _ | tr / _ | tr ' ' _
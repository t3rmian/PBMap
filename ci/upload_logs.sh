#!/bin/bash
tar -czf `date "+%Y.%m.%dT%H.%M.%S_pbmap-logs.tar.gz"` $1
curl --upload-file `ls -A1 *logs.tar.gz` https://transfer.sh
#!/bin/bash
tar -czf pbmap_`date "+%Y.%m.%dT%H.%M.%S_logs.tar.gz"` $1
curl --upload-file `ls -A1 *logs.tar.gz` https://transfer.sh
#!/bin/bash
tar -czf `date "+%Y.%m.%dT%H.%M.%S_ss.tar.gz"` $2
curl -X POST https://content.dropboxapi.com/2/files/upload \
  --header "Authorization: Bearer $1" \
  --header "Dropbox-API-Arg: {\"path\": \"/ss/`ls -A1 *_ss.tar.gz`\"}" \
  --header "Content-Type: application/octet-stream" \
  --data-binary @`ls -A1 *_ss.tar.gz`
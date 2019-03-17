#!/bin/bash
curl -X POST https://content.dropboxapi.com/2/files/download \
  --header "Authorization: Bearer $1" \
  --header "Dropbox-API-Arg: {\"path\": \"/$2\"}" \
  -o "./$2" \
  && unzip -o $2 \
  && rm $2
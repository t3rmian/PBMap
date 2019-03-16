#!/bin/bash
gcloud firebase test android run \
  --type instrumentation \
  --app $1 \
  --test $2 \
  --device-ids Nexus6 \
  --os-version-ids 21 \
  --locales en \
  --orientations portrait
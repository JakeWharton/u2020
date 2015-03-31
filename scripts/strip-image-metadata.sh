#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "$0" )" && pwd )"
REPO_DIR=$(dirname $SCRIPT_DIR)

set -ex

find $REPO_DIR -path '*/src/*/res/*' -name '*.png' -print -exec exiftool -overwrite_original -all= {} \;

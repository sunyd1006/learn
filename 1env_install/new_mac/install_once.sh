#!/bin/bash

scriptdir="$(cd "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

echo "scriptdir: $scriptdir"

ret=$(LOAD_FIRST=true $scriptdir/bash_common)
echo "ret: $ret"

#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
bin=${DIR}/../bin
lib=${DIR}/../lib

usage="Usage: mysqles-runner.sh <statefile.json>  \n"

if [ $# -lt 1 ]; then
   echo -e  $usage
   exit 1
fi

statefile=$1

java \
    -cp "${lib}/*" \
    org.xbib.tools.Runner \
    org.xbib.tools.JDBCImporter \
    $statefile

#!/usr/bin/env bash

#TODO, check the GEMFIRE_HOME env is set
if [[ -z $GEMFIRE_HOME ]]; then
  echo "GEMFIRE_HOME must be set"
  exit 0
fi

export OLDPWD=$(pwd)

export PATH=$GEMFIRE_HOME/bin:$PATH
export GFPATH=$(dirname $0)

#TODO, configure the region as partitioned, explain this in the article.
echo "Booting demo Gemfire environment"

cd $GFPATH

mkdir server1
mkdir server2

gfsh run --file=script-startup.gf

echo "Gemfire example environment is running"

cd $OLDPWD

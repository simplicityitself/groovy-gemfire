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
echo "Shutting down Gemfire environment"

cd $GFPATH

gfsh run --file=script-shutdown.gf

echo "Gemfire example environment is shutdown"

cd $OLDPWD

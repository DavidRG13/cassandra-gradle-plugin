#!/bin/bash

rm -rf temp
mkdir temp

DIRECTORY=cassandra-unit-3.1.4.0-SNAPSHOT

if [ ! -d "$DIRECTORY" ]; then
tar -xvf build/resources/main/cassandra-unit-3.1.4.0-SNAPSHOT-bin.tar.gz
fi

#sh cassandra-unit-3.1.4.0-SNAPSHOT-bin/bin/cu-starter -p {port} -t {timeout} -s {schema}
#!/bin/bash

echo "USE sampleCassandra;" > create-entities.cql
cat /cql/*_added_entity_*.cql >> create-entities.cql

cqlsh -f create-entities.cql

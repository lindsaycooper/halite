#!/bin/sh

set -e

javac "$1".java
javac "$2".java

./halite --replay-directory replays/ -vvv --width 32 --height 32 "java $1" "java $2"

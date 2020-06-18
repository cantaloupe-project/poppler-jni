#!/bin/sh
#
# Builds JNI headers on Linux & macOS.
#
# Invoke from the project root directory.
#

javac src/main/java/edu/illinois/library/poppler/*.java \
    -d /tmp \
    -h src/main/cpp

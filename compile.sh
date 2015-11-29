#!/bin/bash

JAR=checkDNS.jar
CLASS_DIR=classes2
mkdir -p $CLASS_DIR
rm -rf $CLASS_DIR/*


javac -d $CLASS_DIR  -sourcepath src  $(find src -name "*.java")

rm -rf $JAR
jar cf $JAR -C $CLASS_DIR .

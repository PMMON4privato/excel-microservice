#!/usr/bin/env bash
name=$(grep artifactId pom.xml | sed -e s'/.*>\([^<]*\)<.*/\1/' | head -n 1)
version=$(grep '<version' pom.xml | sed -e s'/.*>\([^<]*\)<.*/\1/' | head -n 1)
docker build -t sysunite/${name}:${version} .

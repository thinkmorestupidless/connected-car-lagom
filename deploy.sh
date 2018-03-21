#!/bin/bash

VERSION=$1

sbt -DbuildTarget=marathon -DbuildVersion=$VERSION docker:publishLocal
docker tag connected-car-lagom-impl:$VERSION thinkmorestupidless/connected-car-lagom-impl:$VERSION
docker push thinkmorestupidless/connected-car-lagom-impl:$VERSION

   

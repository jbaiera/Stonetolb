#!/bin/bash

java -cp $(mvn dependency:build-classpath | grep -v INFO):target/stonetolb-0.5-SNAPSHOT.jar -Djava.library.path=target/natives/ com.stonetolb.Stonetolb -x 800 -y 600 -m com.stonetolb.game.module.WorldModule 

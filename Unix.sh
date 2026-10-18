#!/bin/sh

SYSTEM_JAVA="$(which java)"

if [ -z $SYSTEM_JAVA ]; then
    echo "You need to install Java to run this application, the minimum required version is Java 11."
else

    SYSTEM="$(uname -s)"
    MACHINE="$(uname -m)"

    if [ $SYSTEM = "Darwin" ]; then
        $SYSTEM_JAVA -XstartOnFirstThread -Djava.library.path=lib/native/$SYSTEM/$MACHINE -jar Blackshades.jar
    else
        $SYSTEM_JAVA -Djava.library.path=lib/native/$SYSTEM/$MACHINE -jar Blackshades.jar
    fi

fi

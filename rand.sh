# !/bin/bash

if [ $# -ne 2 ]; then
    echo Usage: $0 "[seed] [n]"
    exit 1
fi

javac MT19937pp.java

echo "$1"'\n'"$2" | java MT19937pp > nums.out

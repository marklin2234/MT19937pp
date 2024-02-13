# !/bin/bash

if [ $# -ne 2 ]; then
    echo Usage: $0 "[seed] [n]"
    exit 1
fi

echo "$1"'\n'"$2" | java ThreadedMT19937 > nums.out
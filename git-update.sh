#!/bin/bash
#merge
git config pull.rebase false 
git add .
git add -u .
git commit -m "Adoldo Linux"
git pull
git commit
git push
echo .
echo .
echo .
echo "battere invio per continuare"
read

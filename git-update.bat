@echo off
git config pull.rebase false 
git add .
git add -u .
git commit -m "Adolfo IGeriv"
git pull
git commit
git push
echo .
echo .
echo .
echo "battere invio per continuare"
pause

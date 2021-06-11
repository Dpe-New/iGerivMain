@ECHO Off
setlocal

set disco=C:
set org=C:\igeriv\dl\dati\IN
set dst=C:\igeriv\dl\dati\IN
set tmp=C:\igeriv\dl\dati\IN
set prg=C:\igeriv\dl\dati\IN\Programmi

:L0
echo --------------------------------------------------
date/t
time/t
echo Inizio Procedura Ricezione

if exist %tmp%\RICEZIONE_ELENCO.TXT del %tmp%\RICEZIONE_ELENCO.TXT
if exist %tmp%\RICEZIONE_ERRORI.TXT del %tmp%\RICEZIONE_ERRORI.TXT
if exist %tmp%\RICEZIONE_OK.TXT del %tmp%\RICEZIONE_OK.TXT
if exist %tmp%\RICEZIONE_SQL_LOG.TXT del %tmp%\RICEZIONE_SQL_LOG.TXT

if not exist %org%\U*.* goto L1

echo --------------------------------------------------
date/t
time/t
echo Copia Files da ORG a TMP

%disco%
CD %org%

REM for %%f in (U*.*) do if not exist %tmp%\%%f move %org%\%%f %tmp%\
REM if not errorlevel 1 goto L1
REM set ames=DPEWEB Errore durante la move da ORG a TMP !
REM goto ERRORE

:L1
if not exist %tmp%\U*.* goto FINE

echo --------------------------------------------------
date/t
time/t
echo Creazione File RICEZIONE_ELENCO.TXT

dir/b/o:n %tmp%\U*.* > %tmp%\RICEZIONE_ELENCO.TXT

echo --------------------------------------------------
date/t
time/t
echo Elaborazione procedura PL/SQL

set errorlevel = 0
sqlplus igeriv_test/igeriv_test @%prg%\CARICA_DATI_DL.SQL %tmp%\RICEZIONE_SQL_LOG.TXT

if not exist %tmp%\RICEZIONE_ERRORI.TXT goto L2
set ames=Errore nella ricezione dei dati WEB ! Stampare il file RICEZIONE_ERRORI.TXT
goto ERRORE
:L2

if exist %tmp%\RICEZIONE_OK.TXT goto L3
set ames=Errore nella ricezione dei dati WEB ! Non esiste il file RICEZIONE_OK.TXT
goto ERRORE
:L3

:FINE
echo --------------------------------------------------
date/t
time/t
echo Fine Procedura Ricezione (attesa ciclo successivo)

%prg%\SLEEP 3000

goto L0


:ERRORE
echo --------------------------------------------------
date/t
time/t
msg * %ames%

endlocal

pause

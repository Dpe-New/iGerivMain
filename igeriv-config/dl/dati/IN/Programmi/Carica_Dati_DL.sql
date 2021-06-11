SPOOL &1
PROMPT
PROMPT          Inizio Caricamento Dati. Attendere ...
PROMPT
whenever sqlerror exit 1;
SET SERVEROUTPUT ON;
EXEC IGERIV_TEST.P_INTERFACCE.P_IMPORTA;
EXIT;

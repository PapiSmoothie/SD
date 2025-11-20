@echo off
echo ===============================
echo Iniciando Crossing Cr2...
echo Porta: 6102
echo ===============================

mvn compile exec:java -D exec.mainClass="sd.traffic.crossing.CrossingProcess" -D exec.args="Cr2 6102"

pause
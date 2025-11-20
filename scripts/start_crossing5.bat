@echo off
echo ===============================
echo Iniciando Crossing Cr5...
echo Porta: 6105
echo ===============================

mvn compile exec:java -D exec.mainClass="sd.traffic.crossing.CrossingProcess" -D exec.args="Cr5 6105"

pause
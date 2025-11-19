@echo off
echo ===============================
echo Iniciando Crossing Cr1...
echo Porta: 6101
echo ===============================

mvn compile exec:java -D exec.mainClass="sd.traffic.crossing.CrossingProcess" -D exec.args="Cr1 6101"

pause

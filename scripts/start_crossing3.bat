@echo off
echo ===============================
echo Iniciando Crossing Cr3...
echo Porta: 6103
echo ===============================

mvn compile exec:java -D exec.mainClass="sd.traffic.crossing.CrossingProcess" -D exec.args="Cr3 6103"

pause
@echo off
echo ===============================
echo Iniciando Crossing Cr4...
echo Porta: 6104
echo ===============================

mvn compile exec:java -D exec.mainClass="sd.traffic.crossing.CrossingProcess" -D exec.args="Cr4 6104"

pause
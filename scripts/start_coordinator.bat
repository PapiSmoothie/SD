@echo off
echo ===============================
echo Iniciando Coordinator Server...
echo Porta: 6000
echo ===============================

mvn clean compile exec:java -Dexec.mainClass=sd.traffic.coordinator.CoordinatorServer

pause

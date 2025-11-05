@echo off
echo ===============================
echo Iniciando DashboardHub...
echo Porta: 5050
echo ===============================

mvn clean compile exec:java -Dexec.mainClass=sd.traffic.dashboard.DashboardHub

pause
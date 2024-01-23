@echo off

REM Start Angular Frontend
start cmd /c "cd ./frontend/ && npm install"

REM Start 
cd ./frontend
npm install date-fns

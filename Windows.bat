@echo off
where java >nul 2>nul
if %errorlevel%==1 (
    @echo Java is not installed.
    exit
)
else (
    java.exe -Djava.library.path=lib\native\Windows\%PROCESSOR_ARCHITECTURE% -jar Blackshades.jar
)
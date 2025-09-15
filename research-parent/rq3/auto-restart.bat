@echo off
setlocal enabledelayedexpansion

REM Auto-restart script for Maven execution with heap error handling
REM Usage: auto-restart.bat [starting_output_number] [max_retries]

set "OUTPUT_NUMBER=%1"
set "MAX_RETRIES=%2"

REM Set default values if not provided
if "%OUTPUT_NUMBER%"=="" set OUTPUT_NUMBER=9
if "%MAX_RETRIES%"=="" set MAX_RETRIES=50

set "RETRY_COUNT=0"
set "LOG_PREFIX=output"

echo Starting auto-restart script...
echo Starting output number: %OUTPUT_NUMBER%
echo Max retries: %MAX_RETRIES%
echo Current directory: %CD%
echo.

REM Create summary log
set "SUMMARY_LOG=%LOG_PREFIX%-summary.txt"
echo === Auto-restart session started at %DATE% %TIME% === >> %SUMMARY_LOG%

:RETRY_LOOP
if %RETRY_COUNT% GEQ %MAX_RETRIES% goto MAX_RETRIES_REACHED

set /A RETRY_COUNT+=1
set "CURRENT_OUTPUT=%LOG_PREFIX%!OUTPUT_NUMBER!.txt"

echo === Attempt %RETRY_COUNT% of %MAX_RETRIES% ===
echo Output file: !CURRENT_OUTPUT!
echo Command: mvn clean compile exec:java
echo.

REM Log the attempt
echo Attempt %RETRY_COUNT% started at %DATE% %TIME% - Output: !CURRENT_OUTPUT! >> %SUMMARY_LOG%

REM Run Maven command
mvn clean compile exec:java > !CURRENT_OUTPUT! 2>&1

REM Check for heap space error in output
findstr /C:"java.lang.OutOfMemoryError" /C:"Java heap space" /C:"BUILD FAILURE" !CURRENT_OUTPUT! >nul
if %ERRORLEVEL% EQU 0 (
    echo Detected heap space error or build failure. Preparing to restart...
    echo Attempt %RETRY_COUNT% failed at %DATE% %TIME% ^(Heap error or build failure^) >> %SUMMARY_LOG%
    
    echo Waiting 30 seconds before restart...
    timeout /T 30 /NOBREAK >nul
    
    set /A OUTPUT_NUMBER+=1
    goto RETRY_LOOP
) else (
    echo SUCCESS! Maven completed successfully without heap errors.
    echo Attempt %RETRY_COUNT% completed successfully at %DATE% %TIME% >> %SUMMARY_LOG%
    echo === Session completed successfully after %RETRY_COUNT% attempts === >> %SUMMARY_LOG%
    goto END
)

:MAX_RETRIES_REACHED
echo Maximum retries (%MAX_RETRIES%) reached. Stopping execution.
echo === Session stopped after reaching maximum retries (%MAX_RETRIES%) === >> %SUMMARY_LOG%

:END
echo === Auto-restart session ended at %DATE% %TIME% === >> %SUMMARY_LOG%
echo.
echo Script execution completed. Check %SUMMARY_LOG% for a summary of all attempts.
set /A LAST_OUTPUT_NUMBER=OUTPUT_NUMBER-1
echo Last output file: %LOG_PREFIX%!LAST_OUTPUT_NUMBER!.txt

pause

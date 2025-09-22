# Auto-restart script for Maven execution with heap error handling
# This script will automatically restart the Maven command when it encounters OutOfMemoryError
# and increment the output file number each time

param(
    [int]$StartingOutputNumber = 1,  # Starting output number (change this as needed)
    [int]$MaxRetries = 50,           # Maximum number of retries before giving up
    [string]$LogPrefix = "output"    # Prefix for log files
)

$ErrorActionPreference = "Continue"
$outputNumber = $StartingOutputNumber
$retryCount = 0

Write-Host "Starting auto-restart script..." -ForegroundColor Green
Write-Host "Starting output number: $outputNumber" -ForegroundColor Yellow
Write-Host "Max retries: $MaxRetries" -ForegroundColor Yellow
Write-Host "Current directory: $(Get-Location)" -ForegroundColor Cyan

# Create a summary log file to track all attempts
$summaryLogFile = "$LogPrefix-summary.txt"
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
Add-Content -Path $summaryLogFile -Value "=== Auto-restart session started at $timestamp ==="

while ($retryCount -lt $MaxRetries) {
    $currentOutputFile = "$LogPrefix$outputNumber.txt"
    $retryCount++
    
    Write-Host "`n=== Attempt $retryCount of $MaxRetries ===" -ForegroundColor Magenta
    Write-Host "Output file: $currentOutputFile" -ForegroundColor Yellow
    Write-Host "Command: mvn clean compile exec:java" -ForegroundColor Cyan
    
    # Log the attempt
    $attemptTimestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    Add-Content -Path $summaryLogFile -Value "Attempt $retryCount started at $attemptTimestamp - Output: $currentOutputFile"
    
    # Run the Maven command and capture exit code
    # Use cmd /c to properly redirect both stdout and stderr to the same file
    $process = Start-Process -FilePath "cmd" -ArgumentList "/c", "mvn clean compile exec:java > `"$currentOutputFile`" 2>&1" -Wait -PassThru -NoNewWindow
    
    $exitCode = $process.ExitCode
    $endTimestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    
    Write-Host "Maven process completed with exit code: $exitCode" -ForegroundColor $(if ($exitCode -eq 0) { "Green" } else { "Red" })
    
    # Check if the output file contains heap space error
    if (Test-Path $currentOutputFile) {
        $content = Get-Content $currentOutputFile -Raw
        $hasHeapError = $content -match "java\.lang\.OutOfMemoryError.*heap space" -or $content -match "Java heap space"
        $hasBuildFailure = $content -match "BUILD FAILURE"
        
        if ($exitCode -eq 0 -and -not $hasHeapError) {
            Write-Host "SUCCESS! Maven completed successfully without heap errors." -ForegroundColor Green
            Add-Content -Path $summaryLogFile -Value "Attempt $retryCount completed successfully at $endTimestamp"
            Add-Content -Path $summaryLogFile -Value "=== Session completed successfully after $retryCount attempts ==="
            break
        }
        elseif ($hasHeapError -or ($hasBuildFailure -and $exitCode -ne 0)) {
            Write-Host "Detected heap space error or build failure. Preparing to restart..." -ForegroundColor Red
            Add-Content -Path $summaryLogFile -Value "Attempt $retryCount failed at $endTimestamp (Heap error or build failure)"
            
            # Wait a bit before restarting to let system recover
            Write-Host "Waiting 30 seconds before restart..." -ForegroundColor Yellow
            Start-Sleep -Seconds 30
            
            $outputNumber++
            
            # Optionally run garbage collection (though this is more of a placebo)
            Write-Host "Attempting to free memory..." -ForegroundColor Yellow
            [System.GC]::Collect()
            [System.GC]::WaitForPendingFinalizers()
            [System.GC]::Collect()
            
            continue
        }
        else {
            Write-Host "Maven completed but with unknown status. Check $currentOutputFile for details." -ForegroundColor Yellow
            Add-Content -Path $summaryLogFile -Value "Attempt $retryCount completed with unknown status at $endTimestamp"
            break
        }
    }
    else {
        Write-Host "Output file $currentOutputFile not found. Something went wrong." -ForegroundColor Red
        Add-Content -Path $summaryLogFile -Value "Attempt $retryCount failed at $endTimestamp (Output file not found)"
        break
    }
}

if ($retryCount -ge $MaxRetries) {
    Write-Host "`nMaximum retries ($MaxRetries) reached. Stopping execution." -ForegroundColor Red
    Add-Content -Path $summaryLogFile -Value "=== Session stopped after reaching maximum retries ($MaxRetries) ==="
}

$finalTimestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
Add-Content -Path $summaryLogFile -Value "=== Auto-restart session ended at $finalTimestamp ==="

Write-Host "`nScript execution completed. Check $summaryLogFile for a summary of all attempts." -ForegroundColor Green
Write-Host "Last output file: $LogPrefix$($outputNumber-1).txt" -ForegroundColor Yellow

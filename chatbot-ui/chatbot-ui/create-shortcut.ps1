# PowerShell script to create a desktop shortcut for the Chatbot application

# Get the path to the current script directory
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path

# Define the target batch file
$targetPath = Join-Path $scriptPath "ChatbotLauncher.bat"

# Define the desktop path
$desktopPath = [System.Environment]::GetFolderPath("Desktop")
$shortcutPath = Join-Path $desktopPath "Chatbot.lnk"

# Create the shortcut
$WshShell = New-Object -comObject WScript.Shell
$shortcut = $WshShell.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $targetPath
$shortcut.WorkingDirectory = $scriptPath
$shortcut.IconLocation = "shell32.dll,13"
$shortcut.Save()

Write-Host "Desktop shortcut created successfully!"
Write-Host "You can now launch the Chatbot application by double-clicking the 'Chatbot' icon on your desktop."
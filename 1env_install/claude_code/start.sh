#!/bin/bash

# Script to link Claude settings.json to 1env_install/claude_code/.claude path
# This allows using the config on another computer by executing this script

# Define source and destination paths
SOURCE_SETTINGS="$HOME/.claude/settings.json"
DEST_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.claude"
DEST_SETTINGS="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/.claude/settings.json"

echo "Linking Claude settings.json..."
echo "Source: $SOURCE_SETTINGS"
echo "Destination: $DEST_SETTINGS"

# Check if source file exists
if [ -f "$SOURCE_SETTINGS" ]; then
    echo "Error: Source settings.json already exists at $SOURCE_SETTINGS"
    echo "Please handle it manually before running this script."
    exit 1
fi

# Create destination directory if it doesn't exist
mkdir -p "$DEST_DIR"

# Create soft link from source to destination
echo "Creating soft link from $SOURCE_SETTINGS to $DEST_SETTINGS..."
ln -sf "$DEST_SETTINGS" "$SOURCE_SETTINGS"

echo "Settings.json linked successfully!"
echo "To use this configuration on another computer:"
echo "1. Clone this repository"
echo "2. Run this script: ./1env_install/claude_code/start.sh"
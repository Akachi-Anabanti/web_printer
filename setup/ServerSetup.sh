#!/usr/bin/env bash

# Install Python 3
sudo apt install python3-full

# Remove existing web_printer directory if it exists
if [ -d "$HOME/web_printer" ]; then
    rm -rf "$HOME/web_printer"
fi

# Clone the web_printer repository
git clone --branch main https://www.github.com/Akachi-Anabanti/web_printer.git "$HOME/web_printer"

# Change to the web_printer directory
cd "$HOME/web_printer" || exit

# Create a virtual environment 
python3 -m venv printerapp_env

# Activate the virtual environment
source printerapp_env/bin/activate

# Add Ansible to the project
pip3 install ansible

# Function to clean up the virtual environment
cleanup() {
    deactivate
    rm -rf printerapp_env
    echo "Removed virtual environment."
}

# Set trap to clean up the virtual environment on script exit
trap cleanup EXIT

# Run Ansible playbook
ansible-playbook -i inventory master.yml --ask-become-pass

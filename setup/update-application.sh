#!/usr/bin/env bash
# Update the application witht the latest changes

# Change to the web_printer directory
cd "$HOME/web_printer" || exit

# Create a virtual environment 
python3 -m venv /tmp/printerapp_env_update

# Activate the virtual environment
source /tmp/printerapp_env_update/bin/activate

# Add Ansible to the project
pip3 install ansible

# Function to clean up the virtual environment
cleanup() {
    deactivate
    sudo rm -rf /tmp/printerapp_env
    echo "Removed virtual environment."
}

# Set trap to clean up the virtual environment on script exit
trap cleanup EXIT

# Run Ansible playbook
ansible-playbook update-deploy-playbook.yml --ask-become-pass

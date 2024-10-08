#!/usr/bin/env bash

# Install Python 3
sudo apt install python3-full

# Remove existing web_printer directory if it exists
if [ -d "$HOME/web_printer" ]; then
    rm -rf "$HOME/web_printer"
fi

# Clone the web_printer repository
git clone --branch main https://www.github.com/Akachi-Anabanti/web_printer.git "$HOME/web_printer"

# Install Poetry
curl -sSL https://install.python-poetry.org | python3 -

# Add Poetry to PATH
echo 'export PATH="$HOME/.local/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc

# Change to the web_printer directory
cd "$HOME/web_printer"

# Initialize Poetry project
poetry init --no-interaction

# Add Ansible to the project
poetry add ansible

# Run Ansible playbook
poetry run ansible-playbook -i inventory SetupPlaybook.yaml --ask-become-pass

# Remove Poetry virtual environment
poetry env remove --all

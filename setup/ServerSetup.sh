sudo apt-get update
sudo apt-get upgrade -y

# Install python3-pip
sudo apt install python3-pip -y

# Install python ansible
pip install ansible
export PATH="$HOME/.local/bin:$PATH"
source ~/.bashrc

# Install Git
sudo apt-get install -y git
git config --global http.postBuffer 524288000

# Install CUPS for printer management
sudo apt-get install -y cups
  
# Clone your application repository

git clone --depth 1 --single-branch --branch main https://github.com/Akachi-Anabanti/web_printer ~/web_printer

# Use Ansible to setup the enivironment and application
cd ~/web_printer

ansible-playbook -i inventory SetupPlaybook.yml

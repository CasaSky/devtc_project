# create devtc directory
mkdir $HOME/.devtc
# set system env variable DEVTC_HOME
sudo echo 'DEVTC_HOME="$HOME/.devtc"' >> /etc/environment
# system steps
mkdir "$DEVTC_HOME/{tool}"
sudo echo '{TOOL}_HOME="$DEVTC_HOME/{tool}"' >> /etc/environment
sudo echo 'PATH=$PATH:${TOOL}_HOME/default_version' >> /etc/environment
curl -Lo ${TOOL}_HOME/{release_version}/{tool}.{file_extension} https://releases.hashicorp.com/terraform/{release_version}/terraform_{release_version}_{platform}.{file_extension}
curl -Lo /home/talal/.devtc/default/jdk/release_version/ https://corretto.aws/downloads/latest/amazon-corretto-{release_version}-{platform}-jdk.tar.gz
unzip -j $DEVTC_HOME/terraform/{release_version}/{tool}.{file_extension} -d $DEVTC_HOME/terraform/{release_version}
rm -rf terraform_{release_version}_{platform}.zip
mv "$(ls -Art | tail -n 1)" "{release_version}"
ln -s $DEVTC_HOME/{release_version} default_version
# maintainer steps
tweeks - configs - extra dependencies





#result = subprocess.run(["echo", "export DEVTC_HOME={}".format(DEVTC_HOME_DIRECTORY), "|", "tee", "-a", "{}/.bashrc".format(HOME_VARIABLE)])
        # result = subprocess.run(["echo", "export DEVTC_HOME={}".format(DEVTC_HOME_DIRECTORY), "|", "sudo", "tee", "-a", "{}/.bashrc".format(HOME_VARIABLE)])
        #result = subprocess.run(["sudo", "echo", "DEVTC_HOME={}".format(DEVTC_HOME_DIRECTORY), ">>", "{}/.bashrc".format(HOME_VARIABLE)])
        #print(result)
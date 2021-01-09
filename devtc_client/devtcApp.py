import os
import shutil
import stat
import sys
import tarfile
import zipfile

import urllib3

HOME_PATH = os.environ.get("HOME")
DEVTC_HOME_PATH = "{}/.devtc".format(HOME_PATH)
DEVTC_BIN_PATH = "{}/bin".format(DEVTC_HOME_PATH)
DEVTC_ENV_VARIABLE = "DEVTC_HOME"


def modify_env_variables(install=True):
    option = "Setting" if install else "Resetting"
    print("{} devtc environment variable {}={}...".format(option, DEVTC_ENV_VARIABLE, DEVTC_HOME_PATH))

    bash_rc_path = "{}/.bashrc".format(HOME_PATH)
    bash_rc_backup_path = "{}.bak".format(bash_rc_path)
    env_variable_content = '\n' + 'export {}={}\nexport PATH=$PATH:${}/bin\n'.format(DEVTC_ENV_VARIABLE, DEVTC_HOME_PATH, DEVTC_ENV_VARIABLE)

    try:

        file_exists = os.path.isfile(bash_rc_path)
        if file_exists:
            shutil.copyfile(bash_rc_path, bash_rc_backup_path)

            with open(bash_rc_path, 'r') as bash_rc_file:
                bash_rc_content = bash_rc_file.read()

            with open(bash_rc_path, 'wt') as bash_rc_file:
                set_env_var = install and DEVTC_ENV_VARIABLE not in bash_rc_content
                new_bash_rc_content = bash_rc_content + env_variable_content if set_env_var else bash_rc_content.replace(env_variable_content, '')
                bash_rc_file.write(new_bash_rc_content)

                print("devtc environment successfully modified")
        else:
            print("Could not set devtc environment variable - missing .bashrc user configuration")
    except RuntimeError as e:
        if os.path.isfile(bash_rc_backup_path):
            os.rename(bash_rc_backup_path, bash_rc_path)
        print("Could not set devtc environment variable", e)


def install_devtc():
    try:
        if HOME_PATH is None:
            raise ValueError("Could not install devtc - as no HOME Variable is set")

        if is_devtc_installed():
            print("devtc already installed in {}".format(DEVTC_HOME_PATH))
            return

        print("Installing devtc in {}...".format(DEVTC_HOME_PATH))

        os.mkdir(DEVTC_HOME_PATH)
        os.mkdir(DEVTC_BIN_PATH)

        modify_env_variables()

        print("devtc installed successfully")
    except RuntimeError as e:
        print("Could not install devtc", e)


def remove_devtc():
    if not is_devtc_installed():
        print("devtc is already removed!")
        return

    shutil.rmtree(DEVTC_HOME_PATH, ignore_errors=True)
    modify_env_variables(False)
    print("devtc successfully removed")


def is_devtc_installed():
    return os.path.isdir(DEVTC_HOME_PATH)


def process_devtc(process):
    if process is None or process not in ["install", "remove"]:
        print("Did you mean install?")
        usage()
        return

    install_devtc() if process == "install" else remove_devtc()


def install_tool(name, release_version, download_url, package_extension, package_binary_path):
    try:
        if not is_devtc_installed():
            print("Please install devtc first!")
            return

        if is_tool_installed(name):
            print("Last version of {} {} is already installed".format(name, release_version))
            return

        print("Installing {}...".format(name))

        tool_home_path = "{}/{}".format(DEVTC_HOME_PATH, name)
        tool_last_version_path = "{}/{}".format(tool_home_path, release_version)

        os.mkdir(tool_home_path)
        os.mkdir(tool_last_version_path)

        download(download_url, tool_last_version_path, package_extension)

        tool_sym_link_path = "{}/default".format(tool_home_path)
        os.symlink(tool_last_version_path, tool_sym_link_path)

        tool_binary_path = "{}/{}".format(tool_sym_link_path, package_binary_path)
        os.chmod(tool_binary_path, os.stat(tool_binary_path).st_mode | stat.S_IEXEC)  # guarantee execution
        devtc_tool_sym_link_path = "{}/{}".format(DEVTC_BIN_PATH, name)
        os.symlink(tool_binary_path, devtc_tool_sym_link_path)

        print("{} successfully installed in {}/{}".format(name, DEVTC_BIN_PATH, name))
    except RuntimeError as e:
        print("Could not install {}".format(name), e)


def remove_tool(name):

    if not is_tool_installed(name):
        print("{} is not installed!".format(name))
        return

    print("Removing {}...".format(name))

    tool_home_path = "{}/{}".format(DEVTC_HOME_PATH, name)
    shutil.rmtree(tool_home_path, ignore_errors=True)
    tool_link_path = "{}/{}".format(DEVTC_BIN_PATH, name)
    if os.path.islink(tool_link_path):
        os.unlink(tool_link_path)

    print("{} successfully removed".format(name))


def process_tool(name, process):
    if name not in ["java", "terraform", "vault"]:
        print("Unknown tool <{}>!".format(name))
        return
    if process not in ["install", "remove"]:
        print("Did you mean install?")
        usage()
        return

    install_tool(name, f_release_version(name), f_download_url(name), f_package_extension(name), f_package_binary_path(name)) if process == "install" else remove_tool(name)


def download(url, last_version_path, package_extension):
    print("Downloading tool to {}...".format(last_version_path))

    package_path = get_package_path(last_version_path, package_extension)

    http = urllib3.PoolManager()
    r = http.request('GET', url, preload_content=False)
    try:
        with open(package_path, 'wb') as out:
            while True:
                data = r.read(65536)
                if not data:
                    break
                out.write(data)
        r.release_conn()

        extract_binaries(last_version_path, package_extension)
        os.remove(package_path)
    except RuntimeError as e:
        print("Could not download and extract binaries successfully - ", e)


def get_package_path(last_version_path, package_extension):
    return "{}/package.{}".format(last_version_path, package_extension)


def extract_binaries(last_version_path, package_extension):
    print("Extracting Tool binaries...")

    package_path = get_package_path(last_version_path, package_extension)

    if package_extension == "zip":
        opener, mode = zipfile.ZipFile, 'r'
    elif package_extension == "tar":
        opener, mode = tarfile.open, 'r:'
    elif package_extension == "tar.gz":
        opener, mode = tarfile.open, 'r:gz'
    elif package_extension == "tar.bz2":
        opener, mode = tarfile.open, 'r:bz2'
    else:
        raise ValueError("Could not extract `%s` as no appropriate extractor is found" % package_path)
    with opener(package_path, mode) as file:
        file.extractall(last_version_path)
        print("Tool binaries are extracted to {}".format(last_version_path))


def is_tool_installed(name):
    home_path = "{}/{}".format(DEVTC_HOME_PATH, name)
    return os.path.isdir(home_path)


def usage():
    print("Usage: devtcApp.py [global options] [tool_to_install]\n" +
          "\n" +
          "The available commands are listed below.\n" +
          "Before installing any tool, you have to install devtcApp first.\n" +
          "\n" +
          "Examples:\n" +
          "devtcApp.py install              Installs devtcApp\n" +
          "devtcApp.py remove               Removes devtcApp with all installed tools\n" +
          "devtcApp.py install terraform    Installs terraform tool\n" +
          "devtcApp.py remove terraform     Removes terraform tool\n" +
          "\n" +
          "Main commands:\n" +
          "install     Prepare your working directory for other commands\n" +
          "remove      Check whether the configuration is valid"
          )


def f_release_version(name):
    if name == "java":
        return "15"
    elif name == "terraform":
        return "0.14.3"
    else:
        return "1.6.1"


def f_download_url(name):
    if name == "java":
        return "https://corretto.aws/downloads/latest/amazon-corretto-15-x64-linux-jdk.tar.gz"
    elif name == "terraform":
        return "https://releases.hashicorp.com/terraform/0.14.3/terraform_0.14.3_linux_amd64.zip"
    else:
        return "https://releases.hashicorp.com/vault/1.6.1/vault_1.6.1_linux_amd64.zip"


def f_package_extension(name):
    if name == "java":
        return "tar.gz"
    elif name == "terraform":
        return "zip"
    else:
        return "zip"


def f_package_binary_path(name):
    if name == "java":
        return "amazon-corretto-15.0.1.9.1-linux-x64/bin/java"
    elif name == "terraform":
        return "terraform"
    else:
        return "vault"


commands = sys.argv
number_of_commands = len(commands)
if number_of_commands == 1:
    usage()
elif number_of_commands == 2:
    process_devtc(commands[1])
elif number_of_commands == 3:
    process_tool(commands[1], commands[2])
else:
    print("Too many arguments!")
    usage()

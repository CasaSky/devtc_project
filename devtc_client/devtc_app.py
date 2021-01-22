import os
import shutil
import stat
import sys
import platform
import tarfile
import zipfile
from enum import Enum

import requests
import urllib3

import managed_tool

HOME_PATH = os.environ.get("HOME")
DEVTC_HOME_PATH = "{}/.devtc".format(HOME_PATH)
DEVTC_BIN_PATH = "{}/bin".format(DEVTC_HOME_PATH)
DEVTC_ENV_VARIABLE = "DEVTC_HOME"


class Process(Enum):
    INSTALL = 'install'
    REMOVE = 'remove'


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
    try:
        install_devtc() if Process(process) == Process.INSTALL else remove_devtc()
    except ValueError:  # in case of no valid input of process (install, remove) Enum generation will raise this exception
        print("Did you mean install?")
        usage()


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
    linux = "Linux"
    windows = "Windows"
    darwin = "Darwin"
    platform_code = platform.system()
    platform_param = ""
    if platform_code == linux:
        platform_param = linux
    elif platform_code == windows:
        platform_param = windows
    elif platform_code == darwin:
        platform_param = darwin
    else:
        print("Unknown platform <{}>!".format(platform_param))

    try:
        mt = retrieve_managed_tool(name, platform_param)
        if mt is None:
            print("Unknown tool <{}>!".format(name))
            return

        install_tool(mt.name, mt.last_release_version, mt.download_url, mt.package_extension, mt.package_binary_path) if Process(process) == Process.INSTALL else remove_tool(name)
    except ValueError:  # in case of no valid input of process (install, remove) Enum generation will raise this exception
        print("Did you mean install?")
        usage()


def download(url, last_version_path, package_extension):
    print("Downloading tool to {}...".format(last_version_path))

    package_path = get_package_path(last_version_path, package_extension)

    http = urllib3.PoolManager()
    response = http.request('GET', url, preload_content=False)
    try:
        with open(package_path, 'wb') as out:
            while True:
                data = response.read(65536)
                if not data:
                    break
                out.write(data)
        response.release_conn()

        extract_binaries(last_version_path, package_extension)
        os.remove(package_path)
    except RuntimeError as e:
        print("Could not download and extract binaries successfully - ", e)


def retrieve_managed_tool(name, platform_param):
    params = {'platform': platform_param}
    url = "http://localhost:9090/toolchain/{}".format(name)

    return managed_tool.single_mapper(requests.get(url, params).json())


def retrieve_all_managed_tools(platform_param):
    params = {'platform': platform_param}
    url = "http://localhost:9090/toolchain"

    return managed_tool.mapper(requests.get(url, params).json())


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
    print("Usage: devtc_app.py [global options] [tool_to_install]\n" +
          "\n" +
          "The available commands are listed below.\n" +
          "Before installing any tool, you have to install devtc first.\n" +
          "\n" +
          "Examples:\n" +
          "devtc_app.py install              Installs devtc\n" +
          "devtc_app.py remove               Removes devtc with all installed tools\n" +
          "devtc_app.py install terraform    Installs terraform tool\n" +
          "devtc_app.py remove terraform     Removes terraform tool\n" +
          "\n" +
          "Main commands:\n" +
          "install     Prepare your working directory for other commands\n" +
          "remove      Check whether the configuration is valid"
          )


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

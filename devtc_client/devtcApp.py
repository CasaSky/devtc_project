import os
import shutil
import stat
import tarfile
import zipfile

import urllib3

HOME_PATH = os.environ.get("HOME")
DEVTC_HOME_PATH = "{}/.devtc".format(HOME_PATH)
DEVTC_BIN_PATH = "{}/bin".format(DEVTC_HOME_PATH)
DEVTC_ENV_VARIABLE = "DEVTC_HOME"


def set_env_variable():
    print("Setting devtc environment variable {}={}...".format(DEVTC_ENV_VARIABLE, DEVTC_HOME_PATH))

    try:
        bash_rc_path = "{}/.bashrc".format(HOME_PATH)
        file_exists = os.path.isfile(bash_rc_path)
        if file_exists:
            bash_rc_tmp_path = "{}_tmp".format(bash_rc_path)

            with open(bash_rc_path, 'rt') as bash_rc_file:
                bash_rc_content = bash_rc_file.read()
                if DEVTC_ENV_VARIABLE not in bash_rc_content:
                    new_bash_rc_content = bash_rc_content + '\n' + 'export {}={}\nexport PATH=$PATH:${}/bin\n'.format(DEVTC_ENV_VARIABLE, DEVTC_HOME_PATH, DEVTC_ENV_VARIABLE)
                    with open(bash_rc_tmp_path, 'wt') as bash_rc_tmp_file:
                        bash_rc_tmp_file.write(new_bash_rc_content)
                        os.rename(bash_rc_tmp_path, bash_rc_path)

            print("devtc environment set successfully")
        else:
            print("Could not set devtc environment variable - missing .bashrc user configuration")
    except RuntimeError as e:
        print("Could not set devtc environment variable", e)


def install_devtc():
    try:
        if HOME_PATH is None:
            raise ValueError("Could not install devtc - as no HOME Variable is set")

        if os.path.isdir(DEVTC_HOME_PATH):
            print("devtc already installed in {}".format(DEVTC_HOME_PATH))
            return

        print("Installing devtc in {}...".format(DEVTC_HOME_PATH))

        os.mkdir(DEVTC_HOME_PATH)
        os.mkdir(DEVTC_BIN_PATH)

        set_env_variable()

        print("devtc installed successfully")
    except RuntimeError as e:
        print("Could not install devtc", e)


def remove_devtc():
    shutil.rmtree(DEVTC_HOME_PATH, ignore_errors=True)
    # todo unset environment variable
    print("devtc removed successfully")


def install_tool(name, release_version, url, package_extension, package_binary_path):
    try:
        if is_tool_installed(name):
            print("Last version of {} {} is already installed".format(name, release_version))
            return

        print("Installing {}...".format(name))

        tool_home_path = "{}/{}".format(DEVTC_HOME_PATH, name)
        tool_last_version_path = "{}/{}".format(tool_home_path, release_version)

        os.mkdir(tool_home_path)
        os.mkdir(tool_last_version_path)

        download(url, tool_last_version_path, package_extension)

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
    tool_home_path = "{}/{}".format(DEVTC_HOME_PATH, name)
    shutil.rmtree(tool_home_path, ignore_errors=True)
    tool_link_path = "{}/{}".format(DEVTC_BIN_PATH, name)
    if os.path.islink(tool_link_path):
        os.unlink(tool_link_path)

    print("{} removed successfully".format(name))


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


install_devtc()
# remove_devtc()

java = "java"
java_release_version = "15"
java_package_extension = "tar.gz"
java_package_binary_path = "amazon-corretto-15.0.1.9.1-linux-x64/bin/java"
java_package_url = "https://corretto.aws/downloads/latest/amazon-corretto-15-x64-linux-jdk.tar.gz"

install_tool(java, java_release_version, java_package_url, java_package_extension, java_package_binary_path)
# remove_tool(java)

terraform = "terraform"
terraform_release_version = "0.14.3"
terraform_package_extension = "zip"
terraform_package_binary_path = "terraform"
terraform_package_url = "https://releases.hashicorp.com/terraform/0.14.3/terraform_0.14.3_linux_amd64.zip"


install_tool(terraform, terraform_release_version, terraform_package_url, terraform_package_extension, terraform_package_binary_path)
# remove_tool(terraform)

vault = "vault"
vault_release_version = "1.6.1"
vault_package_extension = "zip"
vault_package_binary_path = "vault"
vault_package_url = "https://releases.hashicorp.com/vault/1.6.1/vault_1.6.1_linux_amd64.zip"

install_tool(vault, vault_release_version, vault_package_url, vault_package_extension, vault_package_binary_path)
# remove_tool(vault)

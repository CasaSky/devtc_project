class ManagedTool(object):
    def __init__(self, response):
        self.__dict__ = response
        self.name = response['name']
        self.last_release_version = response['lastReleaseVersion']
        self.download_url = response['downloadUrl']
        self.package_binary_path = response['packageBinaryPath']
        self.package_extension = response['packageExtension']

    def __repr__(self):
        return self.__dict__.__repr__()

    def __contains__(self, name):
        return self.name == name


def mapper(response):
    managed_tools = []
    for mt in response:
        managed_tools.append(ManagedTool(mt))

    return managed_tools


def single_mapper(response):
    return ManagedTool(response)

import json


class Maintenance(object):

    def __init__(self, maintainer_name, docs_url, download_url_template, package_binary_path_template, package_extension, release_version, supported_platform_codes, instructions):
        self.maintainerName = maintainer_name
        self.docsUrl = docs_url
        self.downloadUrlTemplate = download_url_template
        self.packageBinaryPathTemplate = package_binary_path_template
        self.packageExtension = package_extension
        self.releaseVersion = release_version
        self.supportedPlatformCodes = supported_platform_codes
        self.instructions = instructions

    def json(self):
        return json.dumps(self.__dict__)
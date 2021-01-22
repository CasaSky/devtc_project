curl -v -X POST localhost:9090/tools -H "Content-Type:application/json" -d '{"name":"java"}'

curl -v -X POST localhost:9090/tools -H "Content-Type:application/json" -d '{"name":"terraform"}'

curl -v -X POST localhost:9090/tools -H "Content-Type:application/json" -d '{"name":"vault"}'

curl -v -X POST localhost:9090/tools/java/maintenance -H "Content-Type:application/json" -d '{"maintainerName":"Tester", "docsUrl":"https://docs.oracle.com/en/java/javase/15/docs/api", "downloadUrlTemplate":"https://corretto.aws/downloads/latest/amazon-corretto-{release-version}-{platform-code}-jdk.{package-extension}", "packageBinaryPathTemplate":"amazon-corretto-{release-version}", "packageExtension":"tar.gz", "releaseVersion":"15", "supportedPlatformCodes":["x64-linux","x64-windows"]}'

curl -v -X POST localhost:9090/tools/terraform/maintenance -H "Content-Type:application/json" -d '{"maintainerName":"Tester", "docsUrl":"https://www.terraform.io/docs", "downloadUrlTemplate":"https://releases.hashicorp.com/terraform/{release-version}/terraform_{release-version}_{platform-code}.{package-extension}", "packageBinaryPathTemplate":"terraform", "packageExtension":"zip", "releaseVersion":"0.14.5", "supportedPlatformCodes":["linux_amd64","windows_amd64"]}'

curl -v -X POST localhost:9090/tools/vault/maintenance -H "Content-Type:application/json" -d '{"maintainerName":"Tester", "docsUrl":"https://www.vaultproject.io/docs", "downloadUrlTemplate":"https://releases.hashicorp.com/vault/{release-version}/vault_{release-version}_{platform-code}.{package-extension}", "packageBinaryPathTemplate":"vault", "packageExtension":"zip", "releaseVersion":"1.6.1", "supportedPlatformCodes":["linux_amd64","windows_amd64"]}'
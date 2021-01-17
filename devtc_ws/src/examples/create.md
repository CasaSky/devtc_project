curl -X POST localhost:9090/tools -H "Content-Type:application/json" -d '{"name":"java"}'


curl -X POST localhost:9090/tools/1/maintenance -H "Content-Type:application/json" -d '{"maintainerName":"Tester", "docsUrl":"https://docs.oracle.com/en/java/javase/15/docs/api/index.html", "downloadUrlTemplate":"https://corretto.aws/downloads/latest/amazon-corretto-{release-version}-{platform-code}-jdk.{package-extension}", "packageBinaryPathTemplate":"amazon-corretto-{release-version}", "packageExtension":"ZIP", "releaseVersion":"15", "releaseVersionFormat":"//d", "supportedPlatformCodes":["x64-linux","x64-windows"]}'
{"timestamp":"2021-01-17T16:17:33.891+00:00","status":500,"error":"Internal Server Error","message":"","path":"/tools/maintenance/1"

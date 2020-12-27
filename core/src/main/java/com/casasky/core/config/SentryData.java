package com.casasky.core.config;


class SentryData {

    final String webservice;
    final String release;
    final String branch;

    SentryData(String webservice, String release, String branch) {

        this.webservice = webservice;
        this.release = release;
        this.branch = branch;

    }

}

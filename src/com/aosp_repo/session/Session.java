package com.aosp_repo.session;

import com.aosp_repo.credentials.CredentialsData;

import java.io.File;

public class Session {

    private final File aospDir;
    private final CredentialsData credentialsData;

    protected Session(File aospDir, CredentialsData credentialsData) {
        this.aospDir = aospDir;
        this.credentialsData = credentialsData;
    }

}

# AOSP FTP Client
Just an AOSP Client written for FTP Servers in Java. Nothing big, really. Initially called 'AOSP Repo Client'.

## Default-side Build
I won't go into detail about building the AOSP with their repo client. For full detail, visit
their detailed [Website](https://source.android.com/docs/setup/start) on building the AOSP.

## Client-side Build
Download the required packages. Since it is meant to be written on all platforms (Debian (Ubuntu), Arch etc.), you
are required to install them manually using the package manager of the System Choice. On Debian-based systems, the
package manager is `apt`, Arch-based includes `pacman` and the list goes on.

After that, find an FTP server or host one and save the credentials in the AOSP Root as `credentials.cfg`. In there,
you need to link the Username, Password, URL and the Root Directory of the AOSP Root Directory. See the format in
`src/com/aosp_repo/res/configs/credentials.cfg`. This will override the in-built credentials file with your saved
credentials file.

When the FTP Credentials File has been configured properly, look for your device in the configs/devices folder
and start a Linux Terminal and type these commands:
```
## If not done yet
$ java -jar repo-client.jar --init
$ java -jar repo-client.jar --device-add [Brand:google] [Codename:sunfish]
$ java -jar repo-client.jar --device-set [Codename:sunfish]
```

## Credential Initialization
You can obtain a blank `credentials.cfg` file by running this following command:
```
$ java -jar repo-client.jar --obtain credentials
What is the username to the FTP Server? > [Username prompt]
What is the password to the FTP Server? > [Password prompt]

Connection to the FTP Server (URL Address)
> [FTP URL Server Prompt]

Root Directory to the AOSP Source Code (enter '.' if it is in the root directory)
> [Directory Prompt]
```
After executing the command and filling out the fields, your file will be stored in the
`(AOSP Directory)/.ftp-client/credentials.cfg`, sitting and waiting for any code download.

## Build Information
For now, I will be working on the FTP Client, but as Skill and Time progresses, I will introduce the default way
of initializing and downloading the Source Code from GitHub (as most AOSP source code are stored in GitHub). It
will not read any JSON or XML files, as parsing them might not be the job for me, but if someone is willing to make
an XML/JSON Parser, I'm all open.

## Additional Information
Please reach out to me if someone is willing to test this out on their FTP servers since I don't have one to
this day.
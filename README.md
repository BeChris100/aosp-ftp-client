# AOSP FTP Client
Just an AOSP Client written for FTP Servers in Java. Nothing big, really. Initially called 'AOSP Repo Client'.

## Default-side Build
I won't go into detail about building the AOSP with their repo client. For full detail, visit
their detailed [Website](https://source.android.com/docs/setup/start) on building the AOSP.

## Client-side Build
This client will automatically install the missing packages using the APT (Advanced (not really)
Package Tool) if there are any. In case the packages have been already installed, it is recommended
that you do the updates via your Terminal of choice using `sudo apt full-upgrade`. Double check,
what packages are being upgraded in case of kernel or linux image upgrade. If there are any kernel
or linux image upgrade, it is recommended to restart your computer to refresh your drivers.

After that, find an FTP server or host one and save the credentials in the AOSP Root as `credentials.cfg`. In there,
you need to link the Username, Password, URL and the Root Directory of the AOSP Root Directory. See the format in
`src/com/aosp_repo/res/configs/credentials.cfg`. This will override the in-built credentials file with your saved
credentials file.

When the FTP Credentials File has been configured properly, look for your device in the configs/devices folder
and start a Linux Terminal and type these commands:
```
## If not done yet
$ chmod +x ftp_client.sh
$ java -jar AospFtpClient.jar --init $(cat credentials.cfg)
$ java -jar AospFtpClient.jar --device-add [Brand:google] [Codename:sunfish]
$ java -jar AospFtpClient.jar --device-set [Codename:sunfish]
```
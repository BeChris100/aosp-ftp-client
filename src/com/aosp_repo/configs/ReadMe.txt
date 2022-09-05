All environments are parsed from "aosp.env" and "aosp.json" file.

aosp.json File parses environments for downloads and other various tasks.
aosp.env File parses environments for your current build.

Having same environment names in "aosp.json" and "aosp.env" gives the effect that the "aosp.env" file will override the environments.

"vendor_images.json" File handles the binaries for their specific device that you are going to build.
Downloading and extracting them via the shell script is enabled by default, but it can be achieved manually.

"credentials.json" is being used for the FTP server.


#### Build Guide ####
1. Initialize your current working directory
Obviously, you did this already. Otherwise, you wouldn't be seeing this file.

2. Tweak the file settings
Inside the "aosp.json" file, you will need to modify the "FtpUrl" and "FtpUrlRoot" environments.
It handles the URL, where the AOSP is located. The "FtpUrlRoot" is handled from the AOSP root directory.
If your FTP Server lives on "ftp://192.168.0.144" and the AOSP files live on "files/sources/aosp", the FtpUrlRoot will be "ftp://192.168.0.144/files/sources/aosp".

3. Set up the AOSP build
With the help of "repo-ftp-client.jar", you need to launch it via the Terminal using [java -jar repo-ftp-client.jar --setup].
Depending, which build type you selected, it will go through the different routes.

#### Build Guide - Pure AOSP ####
4.
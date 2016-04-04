OpenAM Yubikey Authentication Module
====================================
This module validates Yubikey OTPs against YubiCloud validation platform.
See: http://www.yubico.com for more details

The module uses Yubico's Java client v2. 
See: https://github.com/Yubico/yubico-java-client

To use YubiCloud you need a client id and an API key that can be obtained from
https://upgrade.yubico.com/getapikey/

Build:

Run the following command:

mvn clean package

You can find the distribution zip file (openam-auth-yubikey-dist.zip) inside the target folder

Installation:

* Copy the distribution zip file to your OpenAM server and extract contents to a directory of your choice.

* Copy jar files (openam-auth-yubikey/lib/) to WEB-INF/lib
    $ cp openam-auth-yubikey/lib/ $OPENAM_ROOT/WEB-INF/lib
    
* Copy properties file:
    $ cp openam-auth-yubikey/config/amAuthYubikey.properties $OPENAM_ROOT/WEB-INF/classes

* Copy module descriptor:
    $ cp openam-auth-yubikey/config/Yubikey.xml $OPENAM_ROOT/config/auth/default
    
* Register service

* Register module

* Create module instance

* Update module properties

# xmppobservatory-cryptolyzer-poc
A Proof of Concept project proving [CryptoLyzer](https://gitlab.com/coroner/cryptolyzer)-based analysis of XMPP servers.

This project launches a webserver on port 8000. Based on data from a simple HTTP request, a CryptoLyzer's docker container is executed that analyzes the domain. Results are parsed through Pandoc to get basic HTML output.
 
This approach is rather silly for various reasons. Do not use this for anything important.

## Requirements
- Java ... 6? I've developed with OpenJDK 11, so that's certainly going to work.
- `/bin/sh`
- `docker` (on `$PATH`)
- `pandoc` (on `$PATH`)

## Compile
```
javac nl/goodbytes/xmpp/observatory/Server.java
```

## Run
```
java nl.goodbytes.xmpp.observatory.Server
```

## Use
Open a browser and visit `http://localhost:8000/scan/xmpp.co` where the last part of the URI is the TLD of the domain that you wish to scan.

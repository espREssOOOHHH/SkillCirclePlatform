# skill Share android

if faced error like 
```Unable to access 'https://github.com/*****': OpenSSL SSL_read: Connection was reset, errno 10054```

then try 
``` git config --global http.sslVerify "false" ```
before push manually


git proxy settings:
```git config --global http.proxy 127.0.0.1:7890```
```git config --global https.proxy 127.0.0.1:7890```
unset proxy settings:
```
git config --global --unset http.proxy
git config --global --unset https.proxy
```

show git global settings:
```git config --global -l```

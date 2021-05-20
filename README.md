# skill Share android

if faced error like 
```Unable to access 'https://github.com/*****': OpenSSL SSL_read: Connection was reset, errno 10054```

then try 
``` git config --global http.sslVerify "false" ```
before push manually
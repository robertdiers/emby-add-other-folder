# Emby add other folder to scan nested folders

Emby does not scan nested folders if media data is available, this tool will create a other folder to fix this.

before:

/A/data1.mp3 --> recognized successfully

/A/B/data2.mp3 --> not recognized successfully

/A/B/C/data3.mp3 --> not recognized successfully


after:

/A/other/data1.mp3 --> recognized successfully

/A/B/other/data2.mp3 --> recognized successfully

/A/B/C/data3.mp3 --> recognized successfully


### USAGE:
```shell
java -jar emby-add-other-folder.jar MyMusic mp3,flac,wav true
```

true/false flag is to activate DryRun mode - nothing changed with true, scan only

[Download](target/emby-add-other-folder.jar)

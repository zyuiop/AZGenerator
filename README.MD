AZGenerator
===========

This tool is here to generate compatible funcraft.net ips for offline players.
Offline players have to connect to a special IP on funcraft, which is hidden by the AZLauncher into the serverIP of the player.
This IP is `<base64>.offline.funcraft.net`. This tool is here to generate the specific base64 code needed to connect.

Code format
===========

The code used in the ip is not actually the base64 generated by the system.
It has the following form :

    [1 random char][60 chars from the base64 code][1 random char].[1 random char][rest of the base64 code][1 random char]
    
Please note that in the base64 is actually base64url, which means that `+` becomes `-` and `/` becomes `_`. 

Base64 Code content
===================

The code is generated using the following data :

- The hash of the launcher (16 bytes bytearray) (get it here : `http://www.azlauncher.nz/files/launcher/version.md5`)
- The timestamp in minutes (4 bytes integer)
- The length of the username of the player (1 byte)
- The username of the player (bytearray, utf8)(lenght given by previous parameter)
- 24 random bytes (max 8 identical bytes)
- a 8 bytes CRC32 control sum

Example
=======

- *Username used* : opgzerkfpoze
- *Generated String* : `JeNmRe6fjcQyKETb--9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed_S.UYsXioN3lsol1Z4y6AAAAAGwd1c8I`
- *Original Base64* : `eNmRe6fjcQyKETb--9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed_YsXioN3lsol1Z4y6AAAAAGwd1c8`
- *Classic Base64* : `eNmRe6fjcQyKETb++9jQKAF5rbIMb3BnemVya2Zwb3plG4URfkexpP3Q6ed/YsXioN3lsol1Z4y6AAAAAGwd1c8`
- *Hexadecimal content* : `78 d9 91 7b a7 e3 71 0c 8a 11 36 fe fb d8 d0 28 01 79 ad b2 0c 6f 70 67 7a 65 72 6b 66 70 6f 7a 65 1b 85 11 7e 47 b1 a4 fd d0 e9 e7 7f 62 c5 e2 a0 dd e5 b2 89 75 67 8c ba 00 00 00 00 6c 1d d5 cf`
- *Content details* :
    - `78d9917ba7e3710c8a1136fefbd8d028` : launcher hash
    - `0179adb2` : timestamp
    - `0c` : username length (12 here)
    - `6f70677a65726b66706f7a65` : username (12 bytes, hexadecimal)
    - `1b85 117e 47b1 a4fd d0e9 e77f 62c5 e2a0 dde5 b289 7567 8cba` : 24 random bytes
    - `0000 0000 6c1d d5cf`	: CRC32 control code

Tests to run
============

It could be interesting to know what parameters are actually used by funcraft to verify the user.
We should check :

1. The timelapse of the validity of the code (since the code contains its generation timestamp, we may expect that the code expire)
2. The ability to generate a code in the future that would be valid forever (code with timestamp in 2050 for example)
3. Whether FC checks or not :
    1. The timestamp (likely)
    2. The username (likely)
    3. The validity of the random 24 bytes (unlikely)
    4. The CRC32 sum (likely)
    5. The launcher hash
     
If we have `(2)` then it could be interesting to improve the source code to provide endless usable codes.

If we have `(3.3)` then it could be interesting to use always the samestring to avoid needing to generate one every time

If we have `(3.5)` then it could be interesting to remove the code that reads the hash and to use a fixed one (or generated on start)


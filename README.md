# Raspberry Pi

##FAQ
-----------------------------
**Raspberry Pi USB Current: Your device doesn't seem to run as expected on a Raspberry Pi.**

The USB ports on the standard Raspberry Pi are only capable of supplying around 100mA reliably. Since USB specification dictates 500mA of current maximum, many USB devices require several hundred mA to run smoothly. Since the Pi cannot supply this much current it is common to see buggy performance or complete failure to run at all. The get around this you should use a USB hub connected to the Pi that has it's own external power supply. This will allow the devices connected to have as much power as they require.

---------------------------------
##Autentisering

* user: **pi**
* pw: **notRandom**
* root pw: **650msDat**

##Installation
- [x] Ändra lösen
- [x] Updatera och uppgradera bibloteken
- [x] Installera Java och JDK
- [ ] Installera drirutiner för bluetooth
- [ ] Testa att ansluta med bluetooth 
- [x] Installera drirutiner för Phidgets sensor
- [x] Installera drirutiner för Yoctopuce sensor

##Drivrutiner TL-WN725N
OS ver. 4.1.13-v7+ #826
https://dl.dropboxusercontent.com/u/80256631/8188eu-v7-20151113.tar.gz
```
wget https://dl.dropboxusercontent.com/u/80256631/8188eu-v7-20151113.tar.gz &&
tar xzf 8188eu-v7-20151113.tar.gz &&
./install.sh
```
### Andra OS versioner
https://www.raspberrypi.org/forums/viewtopic.php?p=462982#p462982

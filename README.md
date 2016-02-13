# Raspberry Pi

##FAQ
-----------------------------
(Phidget)
**Raspberry Pi USB Current: Your device doesn't seem to run as expected on a Raspberry Pi.**

The USB ports on the standard Raspberry Pi are only capable of supplying around 100mA reliably. Since USB specification dictates 500mA of current maximum, many USB devices require several hundred mA to run smoothly. Since the Pi cannot supply this much current it is common to see buggy performance or complete failure to run at all. The get around this you should use a USB hub connected to the Pi that has it's own external power supply. This will allow the devices connected to have as much power as they require.

---------------------------------
##Bluetooth 
### Hantering av data (testning behövs)
I en byte array från klienten behandlas datan på följande sätt
* byte[0]   = vilken typ a förfrågan
* byte[1]   = vilken sensor
* byte[2-5] = vilken tid

#### värden för byte[0]
* FF Värden från alla sensorer 
* 01 Värden från en sensor  
#### värden för byte[1] 
* 01 Värden från temperatur sensorn 
* 02 Värden från ljus sensorn 
* 03 Värden från ljud sensorn
* 04 Värden från co2 sensorn
* 05 Värden från rörelse sensorn
* FF Värden från alla sensorer 
#### värden för byte[2-5]
* vilke tid i unix(epoch) 

##Autentisering

* user: **pi**
* pw: **notRandom**
* root pw: **650msDat**

##Installation
- [x] Ändra lösen
- [x] Updatera och uppgradera bibloteken
- [x] Installera Java och JDK
- [x] Installera drirutiner för bluetooth
- [x] Testa att ansluta med bluetooth 
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
### FAQ
Bluetooth servern får error
```
javax.bluetooth.ServiceRegistrationException: Can not open SDP session. [2] No such file or directory
```
blutoothd -> bluetoothd -c i /etc/init.d/bluetooth 

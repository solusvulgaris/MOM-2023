# module4: kafka

!CONFIGURATION_NOTES:

During development and testing, Docker was running under the WSL (Ubuntu on Windows, version: 2.0.9.0).
Thats why in the kafka configuration instead of "localhost" value was used $(wsl hostname -I) - for an initial development machine it was 172.22.248.180   


Ports for the Kafka and zookeeper containers were forwarded using the following commands executed under Windows shell as admin:
	netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=2181 connectaddress=172.22.248.180 connectport=2181
	netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=19092 connectaddress=172.22.248.180 connectport=19092
	netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=29092 connectaddress=172.22.248.180 connectport=29092
	netsh interface portproxy add v4tov4 listenaddress=0.0.0.0 listenport=39092 connectaddress=172.22.248.180 connectport=39092

If you are using a different docker configuration, this address (172.22.248.180) must be changed to the appropriate one.
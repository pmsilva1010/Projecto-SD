# Projecto-SD
WebServer e RMIServer

Para correr através do código fonte é so compilar usando javac RmiServer.java e o TCPServer.java e correr usando java RmiServer e java TCPServer

Para correr através dos executaveis é so usar java -jar dataserver (para o RMI) e java -jar server (para o TCP)

NOTA: Ambos os métodos precisam de ficheiros para executarem corretamente
	RMI:
		Necessita de um configs.txt:
			1ªlinha: directoria do ficheiro que guarda os users
			2ªlinha: directoria do ficheiro que guarda os leiloes
			3ªlinha: directoria do ficheiro que guarda o historico dos leiloes
			4ªlinha: directoria para o ficheiro idCount.txt
		Necessita de um idCount.txt:
			1ªlinha: proximo nº do id a ser usado
		Necessita de um rmiList.txt:
			1ªlinha: directoria do outro servidor RMI
		Necessita do ficheiro policy.all
	
	TCP:
		Necessita de um configs.txt:
			1ªlinha: nome do servidor
			2ªlinha: directoria do servidor RMI primario
			3ªlinha: directoria do servidor RMI secundario
			4ªlinha: server port

Para correr o WebServer:
	- Instalar o servidor Apache Tomcat
	- Inicializar o RMI Server
	- Iniciar o Tomcat com o war file ou através do Eclipse
	- Aceder ao endereço definido no web server (caso não tenha sido alterado deverá ser localhost:8080/WebServer)

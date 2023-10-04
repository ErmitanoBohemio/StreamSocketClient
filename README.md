# StreamSocketClient
Construyendo un cliente sencillo usando Stream Sockets

Requiere de cuatro pasos:

Paso 1: Crear un Socket para conectarse al Servidor, se especifica la dirección ip del servidor y el numero de puerto si resulta exitosa la conexión devuelve un Socket.

Paso 2: Obtiene los Socket's I/O Streams, el cliente usa los metodos getInputStream y getOutputStream para obtener referencia a los Sockets InputStream y OutputStream. El Cliente recibe la información del mismo tipo que el sevidor lo envía es decir si envia un ObjectOutputStream el cliente lee con ObjectInputStream.

Paso 3: Realiza el procesamiento, es la fase en la cual el cliente y servidor se comunican via objetos InputStream y OutputStream.

Paso 4: Cierre de la conexión, el cliente cierra la conexión cuando la transmisión es completada invocando el metodo close tanto en los streams como el Socket; para determinar cuando a finalizado el envío de información el metodo read de InputStream retorna -1 al detectar un fin de stream (también llamado EOF o fin de archivo).
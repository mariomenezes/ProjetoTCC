  //----------------------------------------------------------------------------------------------------------//

#include <SPI.h>

//#include <String.h>

#include <Ethernet.h>



//-------------------------------------------------------------------

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };//MAC padrão;

IPAddress ip(10, 10, 0, 50);//Define o endereco IPv4(trocar final);

IPAddress gateway(10, 10, 0, 1);      //Define o gateway

IPAddress subnet(255, 255, 255, 0); //Define a máscara de rede

EthernetServer server(80); // Porta de serviço

//-------------------------------------------------------------------

//int AA0 = A0;//Arduino analogica A0;

//int AA1 = A1;//Arduino analogica A1;

//int AA2 = A2;//Arduino analogica A2;

//int AA3 = A3;//Arduino analogica A3;

//int AA4 = A4;//Arduino analogica A4;

//-------------------------------------------------------------------

//int D2 = 2;//Arduino digital D2;
int tomada1 = 5;//Arduino digital D5;
int tomada2 = 6;//Arduino digital D6;
int tomada3 = 7;//Arduino digital D7;
int tomada4 = 8;//Arduino digital D8;
//-------------------------------------------------------------------
String readString = String(30); // string para buscar dados de endereço
//boolean statusA0 = false; // Variável para o status do led 
//boolean statusA1 = false; // Variável para o status do led 
//boolean statusA2 = false; // Variável para o status do led 
//boolean statusA3 = false; // Variável para o status do led 
//boolean statusA4 = false; // Variável para o status do led 
//boolean statusD2 = false; // Variável para o status do led 
boolean statusT1 = false; // Variável para o status do led 
boolean statusT2 = false; // Variável para o status do led 
boolean statusT3 = false; // Variável para o status do led 
boolean statusT4 = false; // Variável para o status do led 
//----Configuracoes do CLiente NTP----------------------------------------------------------------
unsigned int localPort = 8888;      // local port to listen for UDP packets

//IPAddress timeServer(132, 163, 4, 101); // time-a.timefreq.bldrdoc.gov NTP server
// IPAddress timeServer(132, 163, 4, 102); // time-b.timefreq.bldrdoc.gov NTP server
// IPAddress timeServer(132, 163, 4, 103); // time-c.timefreq.bldrdoc.gov NTP server
IPAddress timeServer(200, 192, 232, 8);

const int NTP_PACKET_SIZE= 48; // NTP time stamp is in the first 48 bytes of the message

byte packetBuffer[ NTP_PACKET_SIZE]; //buffer to hold incoming and outgoing packets 

// A UDP instance to let us send and receive packets over UDP
EthernetUDP Udp;


//--------------------------------------------------------------------
void setup(){
  // Inicia o Ethernet
  //Ethernet.begin(mac, ip);
  Ethernet.begin(mac, ip, gateway, subnet);
  server.begin();
//-----------------------Define pino como saída-----------------------
  //pinMode(AA0, OUTPUT);
  //pinMode(AA1, OUTPUT);
  //pinMode(AA2, OUTPUT);
  //pinMode(AA3, OUTPUT);
  //pinMode(AA4, OUTPUT);
 // pinMode(D2, OUTPUT);
  pinMode(tomada1, OUTPUT);
  pinMode(tomada2, OUTPUT);
  pinMode(tomada3, OUTPUT);
  pinMode(tomada4, OUTPUT);
//---------------------------------------------------------------------
  // Inicia a comunicação Serial
  Serial.begin(9600); 
}

void loop(){
  // Criar uma conexão de cliente
  EthernetClient client = server.available();
  
  if (client) {
    while (client.connected())
    {
      if (client.available())
      {
        char c = client.read();
        // ler caractere por caractere vindo do HTTP
        if (readString.length() < 30)
        {
          // armazena os caracteres para string
          readString += (c);
        }
       //Serial.print(readString);
        //se o pedido HTTP terminou
        if (c == '\n')
        {
            Serial.println("Entrou");
//------------------------------------------------------------------        
          if(readString.indexOf("t1high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada1, HIGH);//Arduino porta digital D5=5V;
            statusT1 = true;
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t1low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada1, LOW);//Arduino porta digital D5=0V;
            statusT1 = false;
          }
//------------------------------------------------------------------   
          
          if(readString.indexOf("t2high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada2, HIGH);//Arduino porta digital D6=5V;
            statusT2 = true;
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t2low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada2, LOW);//Arduino porta digital D6=0V;
            statusT2 = false;
          }
//------------------------------------------------------------------        
          if(readString.indexOf("t3high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada3, HIGH);//Arduino porta digital D7=5V;
            statusT3 = true;
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t3low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada3, LOW);//Arduino porta digital D7=0V;
            statusT3 = false;
          }
//------------------------------------------------------------------        
          if(readString.indexOf("t4high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada4, HIGH);//Arduino porta digital D8=5V;
            statusT4 = true;
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t4low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada4, LOW);//Arduino porta digital D8=0V;
            statusT4 = false;
          } 
//------------------------------------------------------------------         
        // dados HTML de saída começando com cabeçalho padrão
        client.println("HTTP/1.1 200 OK");
        client.println("Content-Type: text/html");
        client.println();      
        client.print("<font size='20'>");
//------------------------------------------------------------------ 
        if (statusT1) {
          client.print("te1on");//Ethernet envia para Android;
          //String apenas letras;
        } else {
          client.print("te1off");//Ethernet envia string para Android;
          //String apenas letras;
        }
//------------------------------------------------------------------ 
        if (statusT2) {
          client.print("te2on");//Ethernet envia para Android;
          //String apenas letras;
        } else {
          client.print("te2off");//Ethernet envia string para Android;
          //String apenas letras;
        }
//------------------------------------------------------------------ 
        if (statusT3) {
          client.print("te3on");//Ethernet envia para Android;
          //String apenas letras;
        } else {
          client.print("te3off");//Ethernet envia string para Android;
          //String apenas letras;
        }
//------------------------------------------------------------------ 
        if (statusT4) {
          client.print("te4on");//Ethernet envia para Android;
          //String apenas letras;
        } else {
          client.print("te4off");//Ethernet envia string para Android;
          //String apenas letras;
        }
//------------------------------------------------------------------ 
        //limpa string para a próxima leitura
        readString="";
        
        // parar cliente
        client.stop();
        }
      }
    }
  }
}
//--------------------------------------------------------------------------------------------------------//

void lerHoraNTP(){
  
  sendNTPpacket(timeServer); // send an NTP packet to a time server

    // wait to see if a reply is available
  delay(1000);  
  if ( Udp.parsePacket() ) {  
    // We've received a packet, read the data from it
    Udp.read(packetBuffer,NTP_PACKET_SIZE);  // read the packet into the buffer

    //the timestamp starts at byte 40 of the received packet and is four bytes,
    // or two words, long. First, esxtract the two words:

    unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
    unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);  
    // combine the four bytes (two words) into a long integer
    // this is NTP time (seconds since Jan 1 1900):
    unsigned long secsSince1900 = highWord << 16 | lowWord;  
    Serial.print("Seconds since Jan 1 1900 = " );
    Serial.println(secsSince1900);               

    // now convert NTP time into everyday time:
    Serial.print("Unix time = ");
    // Unix time starts on Jan 1 1970. In seconds, that's 2208988800:
    const unsigned long seventyYears = 2208988800UL;     
    // subtract seventy years:
    unsigned long epoch = secsSince1900 - seventyYears;  
    // print Unix time:
    Serial.println(epoch);                               


    // print the hour, minute and second:
    Serial.print("The UTC time is ");       // UTC is the time at Greenwich Meridian (GMT)
    Serial.print((epoch  % 86400L) / 3600); // print the hour (86400 equals secs per day)
    Serial.print(':');  
    if ( ((epoch % 3600) / 60) < 10 ) {
      // In the first 10 minutes of each hour, we'll want a leading '0'
      Serial.print('0');
    }
    Serial.print((epoch  % 3600) / 60); // print the minute (3600 equals secs per minute)
    Serial.print(':'); 
    if ( (epoch % 60) < 10 ) {
      // In the first 10 seconds of each minute, we'll want a leading '0'
      Serial.print('0');
    }
    Serial.println(epoch %60); // print the second
  }
  // wait ten seconds before asking for the time again
  delay(10000);  
}

//---------------------------------------------------------------------

// send an NTP request to the time server at the given address 
unsigned long sendNTPpacket(IPAddress& address)
{
  // set all bytes in the buffer to 0
  memset(packetBuffer, 0, NTP_PACKET_SIZE); 
  // Initialize values needed to form NTP request
  // (see URL above for details on the packets)
  packetBuffer[0] = 0b11100011;   // LI, Version, Mode
  packetBuffer[1] = 0;     // Stratum, or type of clock
  packetBuffer[2] = 6;     // Polling Interval
  packetBuffer[3] = 0xEC;  // Peer Clock Precision
  // 8 bytes of zero for Root Delay & Root Dispersion
  packetBuffer[12]  = 49; 
  packetBuffer[13]  = 0x4E;
  packetBuffer[14]  = 49;
  packetBuffer[15]  = 52;

  // all NTP fields have been given values, now
  // you can send a packet requesting a timestamp: 		   
  Udp.beginPacket(address, 123); //NTP requests are to port 123
  Udp.write(packetBuffer,NTP_PACKET_SIZE);
  Udp.endPacket(); 
}



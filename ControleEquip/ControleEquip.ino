  //----------------------------------------------------------------------------------------------------------//
//Creditos
/*
alterado por Fabiano A. Arndt,
baseados nos créditos abaixo.
 
fabianoallex@gmail.com
www.youtube.com/fabianoallex
*/
//Fim Creditos 
#include <SPI.h>

//#include <String.h>

#include <Ethernet.h>
#include <SD.h>
#include "Dns.h"

//---------------Configuracao sensor temperatura LM35-----

const int LM35 = A0;

//-------------------Configuracao SdCard----------------------------


#define PIN_SD_CARD 4
File myFile;

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
//host para a primeira tentativa
const char* host = "ntp02.oal.ul.pt";  // servidor da NTP.br - ver lista acima para todos os servidores da NTP.br
//const char* host = "192.168.200.254";  // servidor interno 01 - caso tenha um servidor de hora interno, pode ser configurado o nome ou ip na variavel host
//const char* host = "192.168.200.253";  // servidor interno 02
 

DNSClient Dns;
IPAddress rem_add;
//

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
  
  // Seta porta SdCard
  pinMode(PIN_SD_CARD, OUTPUT);
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
#define LEAP_YEAR(_year) ((_year%4)==0)
static  byte monthDays[] = {31, 28, 31, 30 , 31, 30, 31, 31, 30, 31, 30, 31};
 
void localTime(unsigned long *timep, byte *psec, byte *pmin, byte *phour, byte *pday, byte *pwday, byte *pmonth, byte *pyear) {
  unsigned long long epoch =* timep;
  byte year;
  byte month, monthLength;
  unsigned long days;
   
  *psec  =  epoch % 60;
  epoch  /= 60; // now it is minutes
  *pmin  =  epoch % 60;
  epoch  /= 60; // now it is hours
  *phour =  epoch % 24;
  epoch  /= 24; // now it is days
  *pwday =  (epoch+4) % 7;
   
  year = 70;  
  days = 0;
  while((unsigned)(days += (LEAP_YEAR(year) ? 366 : 365)) <= epoch) { year++; }
  *pyear=year; // *pyear is returned as years from 1900
   
  days  -= LEAP_YEAR(year) ? 366 : 365;
  epoch -= days; // now it is days in this year, starting at 0
   
  for (month=0; month<12; month++) {
    monthLength = ( (month==1) && LEAP_YEAR(year) ) ? 29 : monthDays[month];  // month==1 -> february
    if (epoch >= monthLength) { epoch -= monthLength; } else { break; }
  }
   
  *pmonth = month;  // jan is month 0
  *pday   = epoch+1;  // day of month
}

/**********************************************************************************
**************************************** FUNÇÕES FORMATAR DATA/HORA ***************
**********************************************************************************/
String zero(int a){ if(a>=10) {return (String)a+"";} else { return "0"+(String)a;} }
 
String diaSemana(byte dia){
  String str[] = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sabado"};
  return str[dia];
}

void lerHoraNTP(){
  
  
  //Chamar essa funcao aqui
    if(Dns.getHostByName(host, rem_add) == 1 ){
    Serial.println("DNS resolve...");  
    Serial.print(host);
    Serial.print(" = ");
    Serial.println(rem_add);
    sendNTPpacket(rem_add);
  } else {
    Serial.println("DNS fail...");
    Serial.print("time.nist.gov = ");
    Serial.println(timeServer); // caso a primeira tentativa não retorne um host válido
    sendNTPpacket(timeServer);  // send an NTP packet to a time server
  }
   
  delay(1000); //aguarda um segundo, para receber os dados enviados.
   
  if ( Udp.parsePacket() ) {  
    // We've received a packet, read the data from it
    Udp.read(packetBuffer, NTP_PACKET_SIZE);  // read the packet into the buffer
   
    // the timestamp starts at byte 40 of the received packet and is four bytes,
    // or two words, long. First, esxtract the two words:
    unsigned long highWord = word(packetBuffer[40], packetBuffer[41]);
    unsigned long lowWord = word(packetBuffer[42], packetBuffer[43]);  
     
    // combine the four bytes (two words) into a long integer
    // this is NTP time (seconds since Jan 1 1900):
    unsigned long secsSince1900 = highWord << 16 | lowWord;  
    Serial.print("Segundos desde 1 de Jan. de 1900 = " );
    Serial.println(secsSince1900);     
   
    Serial.print("Unix time = ");
    const unsigned long seventyYears = 2208988800UL;      // Unix time starts on Jan 1 1970. In seconds, that's 2208988800:
    unsigned long epoch = secsSince1900 - seventyYears;  //desconta 70 anos
    // print Unix time:
    Serial.println(epoch);         
 
    byte ano, mes, dia, dia_semana, hora, minuto, segundo;            
    localTime(&epoch, &segundo, &minuto, &hora, &dia, &dia_semana, &mes, &ano); //extrai data e hora do unix time
     
    Serial.print("Ano: ");
    Serial.println(ano+1900);
    Serial.print("Mes: ");
    Serial.println(mes+1);
    Serial.print("Dia da semana: ");
    Serial.println(dia_semana);
    Serial.print("Dia: ");
    Serial.println(dia);
    Serial.print("Hora: ");
    Serial.println(hora);
    Serial.print("minunto: ");
    Serial.println(minuto);
    Serial.print("segundo: ");
    Serial.println(segundo);
     
    String s = diaSemana(dia_semana) + ", " + zero(dia) + "/" + zero(mes+1) + "/" + (ano+1900) + " " + zero(hora) + ":" + zero(minuto) + ":" + zero(segundo);
     
    Serial.println(s);
    Serial.println(" ");
  }
   
  delay(10000); //atualiza novamente em 10 segundos
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

int lerTemperatura()
{
  int temperatura = ((int)analogRead(LM35)*5/(1023))/0.01;
  Serial.print("Temperatura: ");
  Serial.println(temperatura);
  return temperatura; 
}

void escreveArffSd(){
  if (!SD.begin(4)) {
    Serial.println("initialization failed!");
    return;
  }
  Serial.println("initialization done.");
  
  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  myFile = SD.open("test.txt", FILE_WRITE);
  
  // if the file opened okay, write to it:
  if (myFile) {
    Serial.print("Writing to test.txt...");
    myFile.println("testing 1, 2, 3.");
	// close the file:
    myFile.close();
    Serial.println("done.");
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening test.txt");
  }
 
}

String lerArffSd(){
  
  // re-open the file for reading:
  myFile = SD.open("test.txt");
  if (myFile) {
    Serial.println("test.txt:");
    
    // read from the file until there's nothing else in it:
    while (myFile.available()) {
    	Serial.write(myFile.read());
    }
    // close the file:
    myFile.close();
  } else {
  	// if the file didn't open, print an error:
    Serial.println("error opening test.txt");
  } 
}

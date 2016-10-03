#include <TimeLib.h>
#include <Ethernet.h>
#include <EthernetUdp.h>
#include <SPI.h>
#include <SD.h>
#include <stdlib.h>
//----------array para programacao de eventos-----------

struct programar{
  int  id_tomada; 
  int hora;
  int minutos;
  boolean comando; //True - liga, false - desliga
  boolean ja_executado;
};

#define array_programar_size 8

programar array_programar[array_programar_size];

int cont_array_programar = 0;

const int LM35 = A0;
const int LDR = A1;
//TODO verificar isso aqui, A4 acho que esta sendo usado SD
const int S1 = A2;
const int S2 = A3;
const int S3 = A5;
const int S4 = A6;

#define PIN_SD_CARD 4
File myFile;

IPAddress ip(10, 10, 0, 50);

IPAddress gateway(10, 10, 0, 1); 

IPAddress subnet(255, 255, 255, 0);

EthernetServer server(80); 

int tomada1 = 5;//Arduino digital D5;
int tomada2 = 6;//Arduino digital D6;
int tomada3 = 7;//Arduino digital D7;
int tomada4 = 8;//Arduino digital D8;
//-------------------------------------------------------------------
String readString = String(30); // string para buscar dados de endereço

boolean statusT1 = false; // Variável para o status do led 
boolean statusT2 = false; // Variável para o status do led 
boolean statusT3 = false; // Variável para o status do led 
boolean statusT4 = false; // Variável para o status do led 



byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; 

// NTP Servers:
IPAddress timeServer(132, 163, 4, 101); // time-a.timefreq.bldrdoc.gov

const int timeZone = -3; //acredito que seja essa notacao para o Brasil


EthernetUDP Udp;
unsigned int localPort = 8888; 

void setup() 
{
  
  for(int i = 0; i < array_programar_size; ++i){
    array_programar[i].id_tomada = -1;
    array_programar[i].hora = -1;
    array_programar[i].minutos = -1;
    array_programar[i].comando = -1;
    array_programar[i].ja_executado = -1;
  }
  
  pinMode(tomada1, OUTPUT);
  pinMode(tomada2, OUTPUT);
  pinMode(tomada3, OUTPUT);
  pinMode(tomada4, OUTPUT);
    
  // Seta porta SdCard
  pinMode(PIN_SD_CARD, OUTPUT);
  
  Serial.begin(9600);
  //while (!Serial) ; // Needed for Leonardo only
  delay(250);
//  Serial.println("Hora NTP");
  
  Ethernet.begin(mac, ip);
  //Serial.print("IP Arduino ");
  //Serial.println(Ethernet.localIP());
  Udp.begin(localPort);
  //Serial.println("Esperando Sincronizar NTP");
  setSyncProvider(getNtpTime);
  define_programacao_diaria();
  
}

time_t prevDisplay = 0; // when the digital clock was displayed


//EthernetClient client;

void loop()
{  
  if (timeStatus() != timeNotSet) {
    if (now() != prevDisplay) { //update the display only if time has changed
      prevDisplay = now();
      //digitalClockDisplay();  
    }
  }
  
//  client = server.available();

  controleTomadas();
  //TODO verificar depois se a opço de programacao sera mantida
  //executa_programacao();
  //lista_programacao();
  
  //delay(2000);

}

EthernetClient client;

void controleTomadas(){
  // Criar uma conexão de cliente
client = server.available();
  
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
            Serial.println("Entrou1");
            // O Led vai ser ligado
            digitalWrite(tomada1, HIGH);//Arduino porta digital D5=5V;
            statusT1 = true;
            registraEventoArffSd();
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t1low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada1, LOW);//Arduino porta digital D5=0V;
            statusT1 = false;
            registraEventoArffSd();
          }
//------------------------------------------------------------------   
          
          if(readString.indexOf("t2high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada2, HIGH);//Arduino porta digital D6=5V;
            statusT2 = true;
            registraEventoArffSd();
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t2low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada2, LOW);//Arduino porta digital D6=0V;
            statusT2 = false;
            registraEventoArffSd();
          }
//------------------------------------------------------------------        
          if(readString.indexOf("t3high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada3, HIGH);//Arduino porta digital D7=5V;
            statusT3 = true;
            registraEventoArffSd();
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t3low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada3, LOW);//Arduino porta digital D7=0V;
            statusT3 = false;
            registraEventoArffSd();
          }
//------------------------------------------------------------------        
          if(readString.indexOf("t4high")>=0)//Recebido do Android;
          {
            // O Led vai ser ligado
            digitalWrite(tomada4, HIGH);//Arduino porta digital D8=5V;
            statusT4 = true;
            registraEventoArffSd();
          }
          // Se a string possui o texto L=Desligar
          if(readString.indexOf("t4low")>=0)//Recebido do Android;
          {
            // O Led vai ser desligado
            digitalWrite(tomada4, LOW);//Arduino porta digital D8=0V;
            statusT4 = false;
            registraEventoArffSd();
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

//void digitalClockDisplay(){
  // digital clock display of the time
  //Serial.print(hour());
  //printDigits(minute());
  //printDigits(second());
  /*Serial.print(" ");
  Serial.print(day());
  Serial.print(" ");
  Serial.print(month());
  Serial.print(" ");
  Serial.print(year()); 
  Serial.println();*/ 
//}

//void printDigits(int digits){
  // utility for digital clock display: prints preceding colon and leading 0
  //Serial.print(":");
  //if(digits < 10)
    //Serial.print('0');
  //Serial.print(digits);
//}

/*-------- NTP code ----------*/

const int NTP_PACKET_SIZE = 48; // NTP time is in the first 48 bytes of message
byte packetBuffer[NTP_PACKET_SIZE]; //buffer to hold incoming & outgoing packets

time_t getNtpTime()
{
  //delay(20000);
  while (Udp.parsePacket() > 0) ; // discard any previously received packets
  Serial.println("Transmit NTP Request");
  sendNTPpacket(timeServer);
  uint32_t beginWait = millis();
  while (millis() - beginWait < 1500) {
    int size = Udp.parsePacket();
    if (size >= NTP_PACKET_SIZE) {
      Serial.println("Receive NTP Response");
      Udp.read(packetBuffer, NTP_PACKET_SIZE);  // read packet into the buffer
      unsigned long secsSince1900;
      // convert four bytes starting at location 40 to a long integer
      secsSince1900 =  (unsigned long)packetBuffer[40] << 24;
      secsSince1900 |= (unsigned long)packetBuffer[41] << 16;
      secsSince1900 |= (unsigned long)packetBuffer[42] << 8;
      secsSince1900 |= (unsigned long)packetBuffer[43];
      return secsSince1900 - 2208988800UL + timeZone * SECS_PER_HOUR;
    }
  }
  Serial.println("No NTP Response :-(");
  return 0; // return 0 if unable to get the time
}

// send an NTP request to the time server at the given address
void sendNTPpacket(IPAddress &address)
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
  Udp.write(packetBuffer, NTP_PACKET_SIZE);
  Udp.endPacket();
}

float lerQtdLuz()
{
  float luz = (float)analogRead(LDR);
  return luz;  
}

float lerTemperatura()
{
  float temperatura = ((float)analogRead(LM35)*5/(1023))/0.01;
  Serial.print("Temperatura: ");
  Serial.println(temperatura);
  return temperatura; 
}
//DOING
float lerConsumoT1(){
    int sensor = analogRead(S1);
    float output =   map(sensor, 0, 1023, -30, 30);
    return output;
}

float lerConsumoT2(){
    int sensor = analogRead(S2);
    float output =   map(sensor, 0, 1023, -30, 30);
    return output;
}

float lerConsumoT3(){
    int sensor = analogRead(S3);
    float output =   map(sensor, 0, 1023, -30, 30);
    return output;
}

float lerConsumoT4(){
    int sensor = analogRead(S4);
    float output =   map(sensor, 0, 1023, -30, 30);
    return output;
}

//DOING
// estado tomadas, temp, luz, data, hora, consumo 
boolean registraEventoArffSd()
{
  boolean error;
  char buff[20];  
  //status das tomadas
  String t1, t2, t3, t4;
  float c1, c2, c3, c4;
  t1 = (statusT1)?"on":"off";
  t2 = (statusT2)?"on":"off";
  t3 = (statusT3)?"on":"off";
  t4 = (statusT4)?"on":"off";
  
  c1 = lerConsumoT1();
  c2 = lerConsumoT2();
  c3 = lerConsumoT3();
  c4 = lerConsumoT4();
  
  //status temperatura
  float t = lerTemperatura();
  
  //Sensor de Luz
  float l = lerQtdLuz();
  
  if (!SD.begin(4)) {
    Serial.println("initialization failed!");
    //return;
    error = true;
  }
  Serial.println("initialization done.");
  
  // open the file. note that only one file can be open at a time,
  // so you have to close this one before opening another.
  myFile = SD.open("base.ARFF", FILE_WRITE);
  
  //Weekday sunday = 1
  // if the file opened okay, write to it:
  if (myFile) {
    Serial.print("Writing to test.txt...");
    myFile.println(t1+","+t2+","+t3+","+t4+","+dtostrf(t, 0, 2, buff) 
                    +","+ dtostrf(l, 0, 2, buff) +","+ dtostrf(weekday(), 0, 2, buff) +","
                    + dtostrf(month(), 0, 2, buff) +","+ dtostrf(year(), 0, 2, buff) +","
                    + dtostrf(hour(), 0, 2, buff) +","+ dtostrf(minute(), 0, 2, buff)
                    + "," + dtostrf(c1, 0, 2, buff) + "," + dtostrf(c2, 0, 2, buff)
                    + "," + dtostrf(c3, 0, 2, buff) + "," + dtostrf(c4, 0, 2, buff));
                    
    // close the file:
    myFile.close();
    Serial.println("done.");
    error = false;
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening test.txt");
    error = true;
  }  
  return !error;
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

void disparaComandoTomada(int id_tomada, boolean comando){

    Serial.print("Id da tomada chamada  ");  
    Serial.println(id_tomada);
  switch(id_tomada){

    //Serial.println(id_tomada);
    case 0:
    
          Serial.println("Entrou na tomada1 sim");
    if (comando != statusT1) {
      if(comando){
          digitalWrite(tomada1, HIGH);//Arduino porta digital D8=5V;
          statusT1 = true;
          //client.print("te1off");//Ethernet envia string para Android;
        } else {
          digitalWrite(tomada1, LOW);//Arduino porta digital D8=5V;
          //client.print("te1off");//Ethernet envia string para Android;
        }
    }
    break; 
    
    case 1:
    if (comando != statusT2) {
      if(comando){
          //digitalWrite(tomada2, HIGH);//Arduino porta digital D8=5V;
          client.print("te2on");//Ethernet envia para Android;
        } else {
          digitalWrite(tomada2, LOW);//Arduino porta digital D8=5V;
          //client.print("te2off");//Ethernet envia string para Android;
        }
    }
    break; 
    
    case 2:
    if (comando != statusT3) {
      if(comando){
          digitalWrite(tomada3, HIGH);//Arduino porta digital D8=5V;
          //client.print("te3on");//Ethernet envia para Android;
        } else {
          digitalWrite(tomada3, LOW);//Arduino porta digital D8=5V;
          //client.print("te3off");//Ethernet envia string para Android;
        }
    }
    break; 
    
    case 3:
    if (comando != statusT4) {
      if(comando){
          digitalWrite(tomada4, HIGH);//Arduino porta digital D8=5V;
          //client.print("te4on");//Ethernet envia para Android;
        } else {
          digitalWrite(tomada4, LOW);//Arduino porta digital D8=5V;
          //client.print("te4off");//Ethernet envia string para Android;
        }
    }
    break; 
    
    default:
        Serial.print("disparaComandoTomada, opcao invalida ");
        Serial.println(comando);
    break;
  }
    //client.stop();
}

void lista_programacao(){
  Serial.println("Listando programacao");
  for(int i = 0; i < cont_array_programar; ++i){
    Serial.println(""); 
    Serial.println(array_programar[i].id_tomada);
    Serial.println(array_programar[i].comando);
    Serial.println(array_programar[i].hora);
    Serial.println(array_programar[i].minutos);
  }
}

boolean executa_programacao(){
  
  boolean status;
  int hora = hour();
  int minutos = minute();
  /*Serial.print("hora atual ");
  Serial.print(hora);
  Serial.print("hora funcao ");
  Serial.println( hour());
  Serial.print("minutos atual ");
  Serial.print(minutos);
  Serial.print(" funcao ");
  Serial.println(minute());
  
  Serial.print("Tamanho do cont_array ");*/
  //Serial.println(cont_array_programar);
  for(int i = 0; i <= cont_array_programar; ++i){
    if(array_programar[i].ja_executado == false)
      if(array_programar[i].hora == hora){
        //Serial.println("Entrou hora hora hora");
        if(array_programar[i].minutos <= minutos){
          //Serial.println("Entrou em minutos tambem");
          disparaComandoTomada(array_programar[i].id_tomada, array_programar[i].comando);
          //Serial.println("Disparando Comando");
          //Serial.print("Entrou para id ");
          //Serial.println(array_programar[i].id_tomada);
        
          array_programar[i].ja_executado = true;
          status = true;      
      }
    }
    else{
      status = false;
      //Serial.println("Nenhum Agendamento para Disparar"); 
    }
  }
 return status; 
}

//Programa para desligar, True ou false
boolean programarTomada(int id_tomada, boolean comando, int hora, int minutos, boolean ja_executado)
{
  
  struct programar p;
  p.id_tomada = id_tomada;
  p.comando = comando;
  p.hora = hora;
  p.minutos = minutos;
  p.ja_executado = ja_executado;
  //se ainda houver vaga
  
    if((cont_array_programar < array_programar_size) && (verificaSeJaAgendado(p.id_tomada, p.comando)))
    {
    /*Serial.println("Inserida Programacao");
    Serial.print(p.id_tomada);
        Serial.print(" ");
            Serial.print(p.comando);
                Serial.print(" ");
                    Serial.print(p.hora);
                        Serial.print(" ");
                            Serial.print(p.minutos);
                                Serial.println(" ");*/
      array_programar[cont_array_programar] = p;
      ++cont_array_programar;
      return true;   
  }
   return false;
}

boolean define_programacao_diaria(){
  
  
  boolean retorno_programar;
  boolean estado;

  retorno_programar = programarTomada(0, true, 0, 0, false); //Tomada 1, desligar, 2 horas e 20 minutos, se ja agendado(true, falses)
  if(retorno_programar){
    //Serial.println("Programacao efetuada com sucesso.");
    estado = true;
  }
  else{
    //Serial.println("Falha ao programar");
    estado = false; 
  }
    
  retorno_programar = programarTomada(1, true, 2, 20, false); //Tomada 2, desligar, 2 horas e 20 minutos
  if(retorno_programar){
    //Serial.println("Programacao efetuada com sucesso.");
    estado = true;
  }
  else{
    //Serial.println("Falha ao programar");
    estado = false;
  }
    
  retorno_programar = programarTomada(2, true, 5, 50, false); //Tomada 3, desligar, 6 horas e 0 minutos
  if(retorno_programar){
    //Serial.println("Programacao efetuada com sucesso.");
    estado = true;
  }
  else{
    //Serial.println("Falha ao programar");
    estado = false;
  }
    
  retorno_programar = programarTomada(3, true, 10, 0, false); //Tomada 1, desligar, 0 horas 0 minutos
  if(retorno_programar){
    //Serial.println("Programacao efetuada com sucesso.");
    estado = true;
  }
  else{
    //Serial.println("Falha ao programar");
    estado = false;
  }
    
    return estado;
}

boolean verificaSeJaAgendado(int id_tomada, boolean comando){
  
  for(int i = 0; i <= cont_array_programar; ++i){
     if(array_programar[i].id_tomada == id_tomada)
       if(array_programar[i].comando == comando)
         return false;
  }
  return true; // Pode ja esta agendado, porem com outro comando
}





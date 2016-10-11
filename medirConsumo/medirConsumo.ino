//Programa: Medidor de corrente com sensor ACS712
//Autor: Arduino e Cia

#include <Wire.h>
#include <SD.h>
#include <TimeLib.h>
#include <stdlib.h>
#include <SPI.h>
#include <Ethernet.h>
#include <EthernetUdp.h>

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; 

// NTP Servers:
IPAddress timeServer(132, 163, 4, 101); // time-a.timefreq.bldrdoc.gov

const int timeZone = -3; //acredito que seja essa notacao para o Brasil


EthernetUDP Udp;

unsigned int localPort = 8888; 

IPAddress ip(10, 10, 0, 50);

IPAddress gateway(10, 10, 0, 1); 

IPAddress subnet(255, 255, 255, 0);

#define PIN_SD_CARD 4
File myFile;

const int analogIn0 = A0;
const int analogIn1 = A1;
const int analogIn2 = A2;
const int analogIn3 = A3;

int mVperAmp = 66; 
int RawValue0 = 0;
int RawValue1 = 0;
int RawValue2 = 0;
int RawValue3 = 0;

int ACSoffset = 2500;

double Voltage0 = 0;
double Voltage1 = 0;
double Voltage2 = 0;
double Voltage3 = 0;

double tensao = 220;

double consumoS1 = 0;
double consumoS2 = 0;
double consumoS3 = 0;
double consumoS4 = 0;


double voltage_mcz = 220;

//sensor0
double watt0 = 0;
double avgamps0 = 0;
double amphr0;
double totamps0 = 0;

//sensor1
double watt1 = 0;
double avgamps1 = 0;
double amphr1;
double totamps1 = 0;

//sensor2
double watt2 = 0;
double avgamps2 = 0;
double amphr2;
double totamps2 = 0;

//sensor3
double watt3 = 0;
double avgamps3 = 0;
double amphr3;
double totamps3 = 0;




void setup(void)
{
  Serial.begin(9600);
  Serial.println("Sensor de Corrente ACS712"); Serial.println("");
  Serial.println("");
  
  Ethernet.begin(mac, ip);
  //Serial.print("IP Arduino ");
  //Serial.println(Ethernet.localIP());
  Udp.begin(localPort);
  //Serial.println("Esperando Sincronizar NTP");
  setSyncProvider(getNtpTime);
 
}

time_t prevDisplay = 0; // when the digital clock was displayed

void loop(void)
{
  calculaS0();
  
    if (timeStatus() != timeNotSet) {
    if (now() != prevDisplay) { //update the display only if time has changed
      prevDisplay = now();
      //digitalClockDisplay();  
    }
  }
  
  //Chama a rotina de desenho na tela
 /* u8g.firstPage();
  do
  {
    draw();
  }
  while ( u8g.nextPage() );*/
  delay(150);
}

/*double calcula_corrente0()
{
  RawValue0 = analogRead(analogIn0);
  Voltage0 = (RawValue0 / 1024.0) * 5000; // Gets you mV
  Amps0 = ((Voltage0 - ACSoffset) / mVperAmp);
  
//  double potencia = tensao*Amps;
  
  Serial.print("Amps: ");
  Serial.println(Amps);
  
  Serial.print("Potencia: ");
  Serial.println(potencia);
  
  consumoS1 += potencia
  delay(2000);
  
  return Amps0;
}*/



float calculaS0(){
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds


  RawValue0 = analogRead(analogIn0);
  Voltage0 = (RawValue0 / 1024.0) * 5000; // Gets you mV
  double amps = ((Voltage0 - ACSoffset) / mVperAmp);
  
  //double amps = calcula_corrente0();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  totamps0 = totamps0 + amps; // calculate total amps
  avgamps0 = totamps0 / time; // average amps
  amphr0 = (avgamps0 * time)/3600; // amp-hour
  watt0 = voltage_mcz * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy = (watt0 * time)/(1000 * 3600); //kwh
  
  Serial.print("Consp KWH S0: ");
  Serial.println(energy);
}


float calculaS1(){
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds


  RawValue1 = analogRead(analogIn1);
  Voltage1 = (RawValue1 / 1024.0) * 5000; // Gets you mV
  double amps = ((Voltage1 - ACSoffset) / mVperAmp);
  
  //double amps = calcula_corrente0();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  totamps1 = totamps1 + amps; // calculate total amps
  avgamps1 = totamps1 / time; // average amps
  amphr1 = (avgamps1 * time)/3600; // amp-hour
  watt1 = voltage_mcz * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy = (watt1 * time)/(1000 * 3600); //kwh
  
  Serial.print("Consp KWH S1: ");
  Serial.println(energy);
  return energy;
}

float calculaS2(){
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds


  RawValue2 = analogRead(analogIn2);
  Voltage2 = (RawValue2 / 1024.0) * 5000; // Gets you mV
  double amps = ((Voltage2 - ACSoffset) / mVperAmp);
  
  //double amps = calcula_corrente0();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  totamps2 = totamps2 + amps; // calculate total amps
  avgamps2 = totamps2 / time; // average amps
  amphr2 = (avgamps2 * time)/3600; // amp-hour
  watt2 = voltage_mcz * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy = (watt2 * time)/(1000 * 3600); //kwh
  
  Serial.print("Consp KWH S2: ");
  Serial.println(energy);
  return energy;
}

float calculaS3(){
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds


  RawValue3 = analogRead(analogIn3);
  Voltage3 = (RawValue3 / 1024.0) * 5000; // Gets you mV
  double amps = ((Voltage3 - ACSoffset) / mVperAmp);
  
  //double amps = calcula_corrente0();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  totamps3 = totamps3 + amps; // calculate total amps
  avgamps3 = totamps3 / time; // average amps
  amphr3 = (avgamps3 * time)/3600; // amp-hour
  watt3 = voltage_mcz * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy = (watt3 * time)/(1000 * 3600); //kwh
  
  Serial.print("Consp KWH S3: ");
  Serial.println(energy);
  return energy;
}

void gravarSD(){
  
  char buff[20]; 
  float c0 = calculaS0();
  float c1 = calculaS1();
  float c2 = calculaS2();
  float c3 = calculaS3();
  String consumo = "Consumo" + hour() + minute() + month() + year();
  
  // + "," + dtostrf(c0, 0, 2, buff) + "," + dtostrf(c1, 0, 2, buff) + "," + dtostrf(c2, 0, 2, buff) + "," + dtostrf(c3, 0, 2, buff);
  
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
    Serial.print("Escrevendo consumo.txt:");
    Serial.println(consumo + dtostrf(c0, 0, 2, buff) + dtostrf(c1, 0, 2, buff));
    myFile.println(consumo + dtostrf(c0, 0, 2, buff) + dtostrf(c1, 0, 2, buff));
	// close the file:
    myFile.close();
    Serial.println("done.");
  } else {
    // if the file didn't open, print an error:
    Serial.println("error opening test.txt");
  }
 
}

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


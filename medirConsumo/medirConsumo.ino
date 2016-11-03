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

int tomada1 = 5;
//tempo
uint32_t lastTime=0;

const int analogIn0 = A2;
const int analogIn1 = A3;
const int analogIn2 = A4;
const int analogIn3 = A5;

int mVperAmp = 66; 
int RawValue0 = 0;
int RawValue1 = 0;
int RawValue2 = 0;
int RawValue3 = 0;

int ACSoffset = 2488;

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
  pinMode(tomada1, OUTPUT);
  digitalWrite(tomada1, HIGH);
   
}


void loop(void)
{
  calculaS0();
  
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
  //lastTime=millis();
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds

  RawValue1 = analogRead(analogIn0);
  Voltage1 = (RawValue1 / 1024.0) * 5000; // Gets you mV
  double amps = ((Voltage1 - ACSoffset) / mVperAmp);
  
//  amps /= 10;
  //tratar ruido
  //if(amps < 0.350)
    //amps = 0;
  //double amps = calcula_corrente0();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  //totamps0 = totamps0 + amps; // calculate total amps
  //avgamps0 = totamps0 / time; // average amps
  //amphr0 = (avgamps0 * time)/3600; // amp-hour
  watt0 = voltage_mcz * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  //long time1 = (millis()-lastTime)/1000;
  energy = (watt0 * time )/(1000 * 3600); //kwh
  
  Serial.print("amps instantaneo: ");
  Serial.println(amps);
  Serial.print("watts instantaneo: ");
  Serial.println(watt0);
  //Serial.print("Consp KWH S0: ");
  //Serial.println(energy);
  
  delay(1000);
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
  Serial.println(amps);
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
  
//  Serial.print("Consp KWH S2: ");
 // Serial.println(energy);
 // return energy;
 delay(250);
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


//Créditos
//Programa: Medidor de corrente com sensor ACS712
//Autor: Arduino e Cia
//Alterado por: mariomenezes

#include <Wire.h>

//int valor = 0;

const int sensor_corrente1 = A1;
const int sensor_corrente2 = A2;
const int sensor_corrente3 = A3;

const int rele1 = 8;
const int rele2 = 7;
const int rele3 = 6;

//int mVperAmp = 66;
int mVperAmp = 185;
int RawValue = 0;
int ACSoffset = 2500;
double Voltage = 0;
double Amps = 0;



void setup(void)
{
  Serial.begin(9600);
  Serial.println("Sensor de Corrente ACS712"); Serial.println("");
  Serial.println("");

  pinMode(rele1, OUTPUT);
  pinMode(rele2, OUTPUT);
  pinMode(rele3, OUTPUT);

  digitalWrite(rele1,HIGH);
}

void loop(void)
{
  Calcula_corrente();
  //Chama a rotina de desenho na tela
  Serial.print("Amperagem: ");
  Serial.println(Amps);
  delay(150);
}

void Calcula_corrente()
{
  RawValue = analogRead(sensor_corrente1);
  Voltage = (RawValue / 1024.0) * 5000; // Gets you mV
  Amps = ((Voltage - ACSoffset) / mVperAmp);
  delay(2000);
}

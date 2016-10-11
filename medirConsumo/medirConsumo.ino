//Programa: Medidor de corrente com sensor ACS712
//Autor: Arduino e Cia

#include <Wire.h>
//#include <U8glib.h>

//Definicoes do display Oled
//U8GLIB_SSD1306_128X64 u8g(U8G_I2C_OPT_NONE);

//int valor = 0;
String str;
int tamanho;

const int analogIn = A2;
int mVperAmp = 66; 
int RawValue = 0;
int ACSoffset = 2500;
double Voltage = 0;
double Amps = 0;

double tensao = 220;

double consumoS1 = 0;

void setup(void)
{
  Serial.begin(9600);
  Serial.println("Sensor de Corrente ACS712"); Serial.println("");
  Serial.println("");
 
}

void loop(void)
{
  calcula_corrente();
  //Chama a rotina de desenho na tela
 /* u8g.firstPage();
  do
  {
    draw();
  }
  while ( u8g.nextPage() );*/
  delay(150);
}

double calcula_corrente()
{
  RawValue = analogRead(analogIn);
  Voltage = (RawValue / 1024.0) * 5000; // Gets you mV
  Amps = ((Voltage - ACSoffset) / mVperAmp);
  
//  double potencia = tensao*Amps;
  
  /*Serial.print("Amps: ");
  Serial.println(Amps);
  
  Serial.print("Potencia: ");
  Serial.println(potencia);
  
  consumoS1 += potencia
  delay(2000);*/
  
  return Amps;
}

double watt = 0;
double voltage = 220;
double avgamps = 0;
double amphr;
double totamps = 0;

void foo(){
  double energy;
  long milisec = millis(); // calculate time in milliseconds
  long time=milisec/1000; // convert milliseconds to seconds

  double amps = calcula_corrente();
//original
  /*totamps=totamps+amps; // calculate total amps
  avgamps=totamps/time; // average amps
  amphr=(avgamps*time)/3600; // amp-hour
  watt =voltage*amps; // power=voltage*current
  energy=(watt*time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy=(watt*time)/(1000*3600); //for reading in kWh
  */
  totamps = totamps + amps; // calculate total amps
  avgamps = totamps / time; // average amps
  amphr = (avgamps * time)/3600; // amp-hour
  watt = voltage * amps; // power=voltage*current
  //energy = (watt * time)/3600; //Watt-sec is again convert to Watt-Hr by dividing 1hr(3600sec)
  energy = (watt * time)/(1000 * 3600); //kwh
  
  Serial.print("Consp KWH: ");
  Serial.println(energy);
}

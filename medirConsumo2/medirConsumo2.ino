/*-importacao Necessaria*/
#include <Wire.h>


int pinoSensor1 = A2; // Tomada 1
int pinoSensor2 = A3; // pIn de calibracao  

int valorCalibracao;
int ValorSensorCali = 0;


int sensorValue_aux = 0;
float valorSensor = 0;
float valorCorrente = 0;
float voltsporUnidade = 0.004887586;// 5%1023
// Para ACS712 de  5 Amperes use 0.185
// Para ACS712 de 10 Amperes use 0.100
//  Para ACS712 de 5 Amperes use 0.066
float sensibilidade = 0.066;

//Tensao da rede AC 110 Volts e na verdade (127 volts)
int tensao = 220;

void setup() {


  //Incia a Serial
  Serial.begin(9600); 
  
  pinMode(pinoSensor1, INPUT);
  pinMode(pinoSensor2, INPUT);//usado apenas para calibracao
  //pinMode(pinoSensor3, INPUT);
  //pinMode(pinoSensor4, INPUT);
 
  
  delay(1000);

}

int calibraLeitura(int sensor){
  
 // Serial.println("Iniciando Calibracao ACS712");
  
  int valCalibInterno = 490;
  
  while(valorCorrente != 0){
    //Serial.print("Valor: ");
    //Serial.println(valCalibInterno);
    for(int i = 5000; i > 0; -i--){
    // le o sensor na pino analogico A0 e ajusta o valor lido ja que a saída do sensor é (1023)vcc/2 para corrente =0
      sensorValue_aux = (analogRead(sensor) - valCalibInterno); 
    // somam os quadrados das leituras.
      valorSensor += pow(sensorValue_aux,2); 
      delay(1);
    }
  
    valorSensor = (sqrt(valorSensor/ 5000)) * voltsporUnidade; 
    // calcula a corrente considerando a sensibilidade do sernsor (185 mV por amper)
    valorCorrente = (valorSensor/sensibilidade); 
  
    //tratamento para possivel ruido
    //O ACS712 para 30 Amperes é projetado para fazer leitura
    // de valores alto acima de 0.25 Amperes até 30.
    // por isso é normal ocorrer ruidos de até 0.20A
    //por isso deve ser tratado
    //Serial.println(valorCorrente);
    
    if(valorCorrente <= 0.250){ // 0.095
      valorCorrente = 0; 
    }
  
    valorSensor = 0;
    
    ++valCalibInterno;
    
    if(valCalibInterno  > 515)
      valCalibInterno  = 500;
      
    }
    //Serial.print("Sensor ");
    //Serial.print(sensor);
    //Serial.print(" Recalibrado = ");
    //Serial.println(valCalibInterno);
    
    //return valorCalibracao;
    valorCalibracao = valCalibInterno;
    
}

boolean verificaSensorCalibracao(int sensor){

  for(int i = 5000; i > 0; i--){
    // le o sensor na pino analogico A0 e ajusta o valor lido ja que a saída do sensor é (1023)vcc/2 para corrente =0
    sensorValue_aux = (analogRead(sensor) - valorCalibracao); 
    // somam os quadrados das leituras.
    valorSensor += pow(sensorValue_aux,2); 
    delay(1);
  }

  // finaliza o calculo da média quadratica e ajusta o valor lido para volts
  valorSensor = (sqrt(valorSensor/ 5000)) * voltsporUnidade; 
  // calcula a corrente considerando a sensibilidade do sernsor (185 mV por amper)
  valorCorrente = (valorSensor/sensibilidade); 

  //tratamento para possivel ruido
  //O ACS712 para 30 Amperes é projetado para fazer leitura
  // de valores alto acima de 0.25 Amperes até 30.
  // por isso é normal ocorrer ruidos de até 0.20A
  //por isso deve ser tratado
  if(valorCorrente <= 0.250){
//    valorCorrente = 0; 
  return true;

  }
  
  return false;
}
int cont_calibra = 0;
boolean calibrado = false;

void loop() {
  
  ++cont_calibra;
  
  if(cont_calibra == 100){
    cont_calibra = 0;
    calibraLeitura(A3);
  }
  //delay(1000);
  //while(cont_calibracao < 3){
   // calibraLeitura(A2);
    //++cont_calibracao;
    //delay(10000);
 // }
  
  delay(1000);
  if(!verificaSensorCalibracao(A3))
  if(!calibrado){
    calibraLeitura(A3);
    calibrado = true;
  }
  //if(!verificaSensorCalibracao(A3))
    //calibraLeitura(A3);
  lerSensor(A2);
  delay(200);
  lerSensor(A3);

}
void lerSensor(int pinoSensor){
  
for(int i = 1000; i > 0; i--){
    // le o sensor na pino analogico A0 e ajusta o valor lido ja que a saída do sensor é (1023)vcc/2 para corrente =0
    sensorValue_aux = (analogRead(pinoSensor) - valorCalibracao); 
    // somam os quadrados das leituras.
    valorSensor += pow(sensorValue_aux,2); 
    delay(1);
  }

  // finaliza o calculo da média quadratica e ajusta o valor lido para volts
  valorSensor = (sqrt(valorSensor/ 1000)) * voltsporUnidade; 
  // calcula a corrente considerando a sensibilidade do sernsor (185 mV por amper)
  valorCorrente = (valorSensor/sensibilidade); 

  //tratamento para possivel ruido
  //O ACS712 para 30 Amperes é projetado para fazer leitura
  // de valores alto acima de 0.25 Amperes até 30.
  // por isso é normal ocorrer ruidos de até 0.20A
  //por isso deve ser tratado
  if(valorCorrente <= 0.250){
    valorCorrente = 0; 
  }

  valorSensor = 0;
  //Serial.print("P");
  //Serial.print(pinoSensor);
  if(pinoSensor == 17 && (valorCorrente * tensao) != 0)
    calibraLeitura(A3);
 
   if(pinoSensor == 16){   
    //Serial.print(pinoSensor);
    
    Serial.println(valorCorrente * tensao);
    //Serial.println(" Watts");
  }
  //Serial.print(" ");
  
  
  

}

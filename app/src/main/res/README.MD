Este programa irá utilizar um conjuntos de elementos(uso das tomadas, uso de sensores)

para montar uma base de dados a cada evento, ou seja.. a cada ligação de uma tomada dispara um evento
Um evento é disparado quando o estado de uma tomada é alterado
Então adiciona-se um itemSet a nossa base

usando formato arff
tomada 1 (ligada, desligada)
tomada 2 (ligada, desligada)
tomada 3 (ligada, desligada)
tomada 4 (ligada, desligada)
//sensor_consumo 1 (<20,>20<50,>50)
//sensor_consumo 2 (<20,>20<50,>50)
//sensor_consumo 3 (<20,>20<50,>50)
//sensor_consumo 4 (<20,>20<50,>50)
sensor_temperatura (<20,>20,>20<30,>30)
sensor_luz (claro, medio, escuro)
sensor_presenca (sim, nao)
hora(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23)

itemSet1 = {ligado,ligado,desligado, desligado, >30, escuro, nao, 2}



# -*- coding: iso-8859-1 -*-

import serial
from datetime import *
import time

# Iniciando conexao serial
comport = serial.Serial("/dev/ttyACM0", 9600)
#comport = serial.Serial('/dev/ttyUSB0', 9600, timeout=1) # Setando timeout 1s para a conexao

PARAM_CARACTER='t'
PARAM_ASCII=str(chr(116))       # Equivalente 116 = t
 
# Time entre a conexao serial e o tempo para escrever (enviar algo)
#time.sleep(1.8) # Entre 1.5s a 2s
 
#comport.write(PARAM_CARACTER)
#comport.write(PARAM_ASCII)

while 1:
    a = datetime.now()
#    VALUE_SERIAL=comport.read()
    arq = open("consumoAsemAssistente", "a")
    #novo
    date = a.strftime('%H:%M:%S')
    #novo
#    all = "%s %s" % (date, VALUE_SERIAL)
    value = (comport.readline().strip())
    #value_formated = '{0:0.2f}'.format(value)
    value_formated = value.decode().strip('\r\n')
    all = "%s %s" % (date, value_formated)
    #antigo
    #arq.write(a.strftime('%H:%M:%S') +" " +  VALUE_SERIAL)
    arq.write(all)
    #para inserir quebra de linha
    arq.write("\n")
    print (value_formated)
    arq.close()
    time.sleep(1.8)
	
 
# Fechando conexao serial
##comport.close()
# Autor: Fernando Krein Pinheiro
# Data: 09/09/2011
# Linguagem: Python
 
# ========= IMPORTANTE ===========
# O codigo esta livre para usar,
# citar e compartilhar desde que
# mantida sua fonte e seu autor.
# Obrigado.
 
#!/usr/bin/python
# -*- coding: cp1252 -*-
 
from datetime import datetime
import serial
import time
 
#Define a porta e o baudrate usado
#No Windows a porta e alguma das COMs
porta = '/dev/ttyACM0'
baud_rate = 9600
 
opcao = raw_input("Verificar Temperatura? s / n")
 
if opcao == 's':
 
    #Tempo de intervalo entre cada leitura
    ##tempo = int (raw_input("Informe o intervalo entre cada leitura: "))
 
    #Cria o objeto_porta, limpa o buffer da serial,
    objeto_porta = serial.Serial(porta, baud_rate)
    objeto_porta.flushInput();
 
    #Abre o arquivo para escrever os dados
    arquivo = open("LOG.txt", "a+")
 
    #Fica em LOOP Infinito, para sair pressione
    #Ctrl + c
    while True:
 
        #Cria o objeto_hoje, pega os valores de
        #dia, mes, ano e horario, converte para string
        objeto_hoje = datetime.today()
        dia = str(objeto_hoje.day)
        mes = str(objeto_hoje.month)
        ano = str(objeto_hoje.year)
        horario = str(objeto_hoje.strftime("%X"))
 
        # Le os valores passados pelo arduino
        temperatura = objeto_porta.readline()
 
        #Escreve os dados de HORA, DATA e TEMPERATURA no arquivo
        #arquivo.write("Leitura de sensor de temperatura\n")
        #arquivo.write("DATA: ")
        #arquivo.write(dia)
        #arquivo.write(" / ")
        #arquivo.write(mes)
        #arquivo.write(" / ")
        #arquivo.write(ano)
        #arquivo.write("\n")
        #arquivo.write("HORA: ")
        #arquivo.write(horario)
        #arquivo.write("\nTEMPERATURA: ")
        arquivo.write(temperatura)
        arquivo.write("\n")
 
        # Imprime os dados na tela (Terminal)
        #print "DATA :",  dia , "/" , mes , "/" , ano , "/"
        #print "HORA : ", horario
        #print "TEMPERATURA :", temperatura
 
        #Faz uma pausa de X (tempo informado) segundos para fazer o loop novamente
        #time.sleep(tempo)
 
    #Fecha o arquivo e a porta serial
    arquivo.close()
    objeto_porta.close()
 
else:
 
        print "Saindo..."

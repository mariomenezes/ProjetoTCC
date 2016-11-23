package com.mmc.nanopower.communication;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mario on 03/11/16.
 */
public class SaveState {

    private double temperatura;
    private double luz;
    private double diaSemana;
    private double hora;
    private double minuto;

    //True liga, False desliga tomada
    private boolean tomada1;
    private boolean tomada2;
    private boolean tomada3;

    private double resTomada1;
    private double resTomada2;
    private double resTomada3;

    private boolean tomada1FuzzyActive;
    private boolean tomada2FuzzyActive;
    private boolean tomada3FuzzyActive;

    //correto ja
    public static SaveState instancia;

    protected SaveState(){

    }

    public boolean getTomada1(){
        return tomada1;
    }

    public boolean getTomada2(){
        return tomada2;
    }

    public boolean getTomada3(){
        return tomada3;
    }

    public static SaveState getInstancia(){
        if(instancia == null){
            instancia = new SaveState();
        }
        return instancia;
    }

    public void setTomada(String name, boolean state, double res){
        switch (name){
            case "tomada1":
                //Caso tomada ja estiver ligada e o comando for para ligar, entao sera ignorado
                if(getTomada1() != state) {
                    Log.d("SET TOMADA 1", " estado diferente, agendamento inserido");
                    tomada1 = state;
                    resTomada1 = res;
                    setFuzzyActiveT1(true);
                }
                break;
            case "tomada2":
                if(getTomada2() != state) {
                    Log.d("SET TOMADA 2", " estado diferente, agendamento inserido");
                    tomada2 = state;
                    resTomada2 = res;
                    setFuzzyActiveT2(true);
                }
                break;
            case "tomada3":
                if(getTomada3() != state) {
                    Log.d("SET TOMADA 3", " estado diferente, agendamento inserido");
                    tomada3 = state;
                    resTomada3 = res;
                    setFuzzyActiveT3(true);
                }
                break;
        }

        //Informa que logica fuzzy agiu e tem algo pra tratar
        //TODO lembrar de desativar depois de executar a acao agendada
        //setFuzzyActive(true);

    }


    public void setTemperatura(double t){
        Log.d("SAVESTATE TEMPRATURA", String.valueOf(t));
        temperatura = t;
    }

    public void setLuz(double l){
        Log.d("SAVESTATE LUZ", String.valueOf(l));
        luz = l;
    }

    public void setDiaSemana(double d){
        Log.d("SAVESTATE DIA", String.valueOf(d));
        diaSemana = d;
    }

    public void setHora(double h){
        Log.d("SAVESTATE HORA", String.valueOf(h));
        hora = h;
    }

    public void setMinuto(double h){
        Log.d("SAVESTATE MINUTO", String.valueOf(h));
        minuto = h;
    }

    public double getTemperatura(){
        return temperatura;
    }

    public double getLuz(){ return luz;}

    public double getDiaSemana(){
        return diaSemana;
    }

    public double getHora(){
        return hora;
    }

    public double getMinuto() {return minuto; }

    public boolean getFuzzyActiveT1(){
        return tomada1FuzzyActive;
    }

    public void setFuzzyActiveT1(boolean state){
        tomada1FuzzyActive = state;
    }

    public boolean getFuzzyActiveT2(){
        return tomada2FuzzyActive;
    }

    public void setFuzzyActiveT2(boolean state){
        tomada2FuzzyActive = state;
    }

    public boolean getFuzzyActiveT3(){
        return tomada3FuzzyActive;
    }

    public void setFuzzyActiveT3(boolean state){
        tomada3FuzzyActive = state;
    }

    public void setLinha(){

    }
}

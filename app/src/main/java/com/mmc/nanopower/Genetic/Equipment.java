package com.mmc.nanopower.Genetic;


public class Equipment {
    private int power_cons_on; // power consuption on in watts
    private int duration; // in minutes of use
    private String equip_name; 
    private double kwattsday;
    
    // construtor de city, adiciona as coordenadas x,y e o nome da cidade
    public Equipment(int consumo_ligado, int duration, String name){
        this.power_cons_on = consumo_ligado;
        this.duration = duration;
        this.equip_name = name;
    }
    
    // return power_consuption_on
    public int getPowerConsuption_on(){
        return this.power_cons_on;
    }
    
    // return duration
    public int getDuration(){
        return this.duration;
    }
    public String getEquipeName(){
    	return equip_name;
    }
    
    //TODO implement method to transform minuts in hours
    
    
    private double wattsToKwatts(double watts)
    {
    	return watts/1000;
    }
    
 // retorno consumo em KWh/dia
    public double calcKwattsDay(Equipment equipment){
        /*int xDistance = Math.abs(this.getPowerConsuption_on() - equipment.getPowerConsuption_on()); //adicionado this
        int yDistance = Math.abs(this.getDuration() - equipment.getDuration()); //adicionado o this
        double distance = Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );
        
        return distance;]}*/
    	kwattsday = wattsToKwatts(this.getPowerConsuption_on())* this.getDuration(); 
    	return kwattsday;
        
    }
    
    //@Override
    public String toString(){
        return getPowerConsuption_on()+"Watts, "+getDuration()+"hours, "+getEquipeName() + ",consuption " + kwattsday + "KWh" ;
    }
}
package com.mmc.nanopower.Genetic;


import java.util.ArrayList;
import java.util.Collections;

public class Tour{

    private ArrayList tour = new ArrayList<Equipment>();
    private double fitness = 0;
    private int consuption = 0;
    
    // cria um tour vazio
    public Tour(){
        for (int i = 0; i < EquipmentManager.numberOfEquipments(); i++) {
            tour.add(null);
        }
    }
    
    public Tour(ArrayList tour){
        this.tour = tour;
    }

    //TODO, olha aqui para gerar uma quantidade de uso aleatoria
    // cria um individuo randomico
    public void generateIndividual() {
    	// loop por todas as cidades destino e adiciona no nosso tour
        for (int equipamentIndex = 0; equipamentIndex < EquipmentManager.numberOfEquipments(); equipamentIndex++) {
          setEquipment(equipamentIndex, EquipmentManager.getEquipment(equipamentIndex));
          
        }
        // sorteia uma ordem para o nosso tour criado
        Collections.shuffle(tour);
    }

    // retorna uma cidade do tour
    public Equipment getEquipment(int tourPosition) {
        return (Equipment)tour.get(tourPosition);
    }

    // adiciona uma cidade em uma posição especifica do tour
    public void setEquipment(int tourPosition, Equipment equipment) {
        tour.set(tourPosition, equipment);
        // quando o tour é alterado é preciso redefinir fitness e distance
        fitness = 0;
        consuption = 0;
    }
    
    // retorna o fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
            //TODO adicionar ao cálculo do fitness informações sobre congestionamento na regiao
        }
        //System.out.println("valor do nosso fitness " + fitness);
        return fitness;
    }
    
    // retorna a distancia total do tour
   /* public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                // recebe a cidade de onde estamos saindos
                Equipment fromCity = getCity(cityIndex);
                // cidade para onde vamos
                Equipment destinationCity;
                // verifica se estamos na ultima cidade do out, se estiver
                // setamos nosso destino final para nossa cidade inicial
                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                // recebe a distancia entre duas cidades
                tourDistance += fromCity.distanceTo(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }*/

 // retorna a distancia total do tour
    // retorna o consumo total de energia
    public int getDistance(){
        if (consuption == 0) {
            int tourConsuption = 0;
            // Loop through our tour's cities
            for (int equipmentIndex=0; equipmentIndex < numberOfEquip(); equipmentIndex++) {
                // recebe a cidade de onde estamos saindos
                Equipment fromCity = getEquipment(equipmentIndex);
                // cidade para onde vamos
                Equipment destinationCity;
                // verifica se estamos na ultima cidade do out, se estiver
                // setamos nosso destino final para nossa cidade inicial
                if(equipmentIndex+1 < numberOfEquip()){
                    destinationCity = getEquipment(equipmentIndex+1);
                }
                else{
                    destinationCity = getEquipment(0);
                }
                // recebe a distancia entre duas cidades
                tourConsuption += fromCity.calcKwattsDay(destinationCity);
            }
            consuption = tourConsuption;
        }
        
        //System.out.println("Saida getDistance = " + consuption);
        
        return consuption;
    }
    
    // numero de equipamentos
    public int numberOfEquip() {
        return tour.size();
    }
    
    // verofoca se o tour possui uma dada cidade
    // verifica se contém o equipamento
    public boolean containsEquip(Equipment city){
        return tour.contains(city);
    }
    
    //@Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < numberOfEquip(); i++) {
            geneString += getEquipment(i)+"|\n";
        }
        return geneString;
    }
}
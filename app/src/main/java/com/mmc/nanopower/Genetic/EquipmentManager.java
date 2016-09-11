package com.mmc.nanopower.Genetic;

import com.mmc.nanopower.Genetic.Equipment;

import java.util.ArrayList;



import java.util.ArrayList;

/*Just Store all equipments*/
public class EquipmentManager {

    private static ArrayList equipments = new ArrayList<Equipment>();

    // Adiciona uma cidade destino
    public static void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }
    
    // return an equipment
    public static Equipment getEquipment(int index){
        return (Equipment)equipments.get(index);
    }
    
    // retorna a quantidade de cidades destino
    // return the number of equipments
    public static int numberOfEquipments(){
        return equipments.size();
    }
}
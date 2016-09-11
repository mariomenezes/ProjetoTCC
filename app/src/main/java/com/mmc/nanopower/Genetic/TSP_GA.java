package com.mmc.nanopower.Genetic;

import android.util.Log;

public class TSP_GA {

    public TSP_GA(int size_population, int size_evolutions) {

        // cria e adiociona os equipamentos
    	//consumo, duracao em minutos, nome do equipamento
        Equipment equipament1 = new Equipment(60, 200, "tomada1");
        EquipmentManager.addEquipment(equipament1);
        Equipment equipament2 = new Equipment(180, 200,"Tomada2");
        EquipmentManager.addEquipment(equipament2);
        /*Equipament city3 = new Equipament(80, 180,"satuba");
        TourManager.addCity(city3);
        Equipament city4 = new Equipament(140, 180,"rio largo");
        TourManager.addCity(city4);
        Equipament city5 = new Equipament(20, 160,"coite do noia");
        TourManager.addCity(city5);
        Equipament city6 = new Equipament(100, 160,"cacimbinhas");
        TourManager.addCity(city6);
        Equipament city7 = new Equipament(200, 160,"agua branca");
        TourManager.addCity(city7);
        Equipament city8 = new Equipament(140, 140,"olho dagua");
        TourManager.addCity(city8);
        Equipament city9 = new Equipament(40, 120,"coruripe");
        TourManager.addCity(city9);
        Equipament city10 = new Equipament(100, 120,"mata do rolo");
        TourManager.addCity(city10);
        Equipament city11 = new Equipament(180, 100,"joao sampaio");
        TourManager.addCity(city11);
        Equipament city12 = new Equipament(60, 80,"jaqueira");
        TourManager.addCity(city12);
        Equipament city13 = new Equipament(120, 80,"campestre");
        TourManager.addCity(city13);
        Equipament city14 = new Equipament(180, 60,"arapiraca");
        TourManager.addCity(city14);
        Equipament city15 = new Equipament(20, 40,"bananal");
        TourManager.addCity(city15);
        Equipament city16 = new Equipament(100, 40,"palmeira");
        TourManager.addCity(city16);
        Equipament city17 = new Equipament(200, 40,"cruzeiro do sul");
        TourManager.addCity(city17);
        Equipament city18 = new Equipament(20, 20,"garanhuns");
        TourManager.addCity(city18);
        Equipament city19 = new Equipament(60, 20,"recife");
        TourManager.addCity(city19);
        Equipament city20 = new Equipament(160, 20,"penedo");
        TourManager.addCity(city20);*/

        // inicializa a população
        Population pop = new Population(size_population, true);
        System.out.println("Initial consuption: " + pop.getFittest().getDistance() + "KWh");

        // Evolui a população paara 100 gerações
        pop = GA.evolvePopulation(pop);
        for (int i = 0; i < size_evolutions; i++) {
            pop = GA.evolvePopulation(pop);
        }

        // resultado final
        Log.d("GENETICO Finished","");
        Log.d("GENETICO Final: ", + pop.getFittest().getDistance() + "KWh");
        Log.d("GENETICO Solution:","");
        Log.d("GENETICO", "" + pop.getFittest());
    }
}
package com.mmc.nanopower.Genetic;;

public class Population {

    // Holds population of tours
    Tour[] tours;

    // constrói uma população
    //Adicionar aqui a geracao aleatória de horas de consumo
    public Population(int populationSize, boolean initialise) {
        tours = new Tour[populationSize];
        // se precisar inilizar uma população de tours
        if (initialise) {
        	// loop e cria inidividuos
            for (int i = 0; i < populationSize(); i++) {
                Tour newTour = new Tour();
                newTour.generateIndividual();
                saveTour(i, newTour);
            }
        }
    }
    
    // salva um tour
    public void saveTour(int index, Tour tour) {
        tours[index] = tour;
    }
    
    // retorna um tour da populacao
    public Tour getTour(int index) {
        return tours[index];
    }

    // retorna o melhor tour da populacao
    public Tour getFittest() {
        Tour fittest = tours[0];
        // loop pelos individuos para encontrar o fittest
        //TODO unico lugar onde o getFitness é utilizado
        for (int i = 1; i < populationSize(); i++) {
            //if (fittest.getFitness() <= getTour(i).getFitness()) {
            if (fittest.getFitness() <= getTour(i).getFitness() && this.getTour(i).getEquipment(0).getEquipeName() == "tomada1") {
                fittest = getTour(i);
                //System.out.println("resultado esperado = "+ this.getTour(i).getCity(0).getName());
                //System.out.println("Deu certo");
            }
        }
        return fittest;
    }

    // retorna o tamanho da populacao
    public int populationSize() {
        return tours.length;
    }
}
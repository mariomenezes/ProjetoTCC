package com.mmc.nanopower.Genetic;;

public class GA {

    /* GA parameters */
    private static final double mutationRate = 0.015; // apenas utilizado nessa mesma classe
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    // Evolui uma população de mais de uma geração
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.populationSize(), false);

        // mantem nosso melhor individuo se elitism estiver setado como true
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveTour(0, pop.getFittest());
            elitismOffset = 1;
        }

        // faz um crossover na populacao
        // faz um loop sobre o tamanho da nova população e cria indivíduos da população atual
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            // seleciona os pais
            Tour parent1 = tournamentSelection(pop);
            Tour parent2 = tournamentSelection(pop);
            // Crossover dos pais
            Tour child = crossover(parent1, parent2);
            // adiciona o filho na nova populacao
            newPopulation.saveTour(i, child);
        }

        // faz mutação na nova população para adicionar algum novo material genético
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            mutate(newPopulation.getTour(i));
        }

        return newPopulation;
    }

    // Aplica crossover a um conjunto de pais e cria filhos
    public static Tour crossover(Tour parent1, Tour parent2) {
        // Create new child tour
    	// cria um novo tour para os filhos
        Tour child = new Tour();

        // recebe posição inicial e final do "sub tour" para o tour de parent1
        int startPos = (int) (Math.random() * parent1.numberOfEquip());
        int endPos = (int) (Math.random() * parent1.numberOfEquip());

        // loop para adicionar o "sub tour" de parent1 para nosso filho
        for (int i = 0; i < child.numberOfEquip(); i++) {
        	// se nossa posição inicial for menos que a posição final
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setEquipment(i, parent1.getEquipment(i));
            } 
            // se nossa posição inicial for maior
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setEquipment(i, parent1.getEquipment(i));
                }
            }
        }

        // loop atraves do tour de cidades de parents2
        for (int i = 0; i < parent2.numberOfEquip(); i++) {
        	// se o filho nao possui a cidade, adiciona
            if (!child.containsEquip(parent2.getEquipment(i))) {
            	// loop para encntrar a posição spare no caminho dos filhos
                for (int ii = 0; ii < child.numberOfEquip(); ii++) {
                    // posição spare encontrada, adiciona cidade
                    if (child.getEquipment(ii) == null) {
                        child.setEquipment(ii, parent2.getEquipment(i));
                        break;
                    }
                }
            }
        }
        return child;
    }

    // faz mutação no tour usando mutação swap
    private static void mutate(Tour tour) {
        // loop atravez do tour das cidades
        for(int tourPos1=0; tourPos1 < tour.numberOfEquip(); tourPos1++){
            // aplica mutation rate
            if(Math.random() < mutationRate){
                // obtem uma segunda posição aleatória no tour
                int tourPos2 = (int) (tour.numberOfEquip() * Math.random());

                // recebe as cidades na posição alvo do tour
                Equipment city1 = tour.getEquipment(tourPos1);
                Equipment city2 = tour.getEquipment(tourPos2);

                // troca elas
                tour.setEquipment(tourPos2, city1);
                tour.setEquipment(tourPos1, city2);
            }
        }
    }

    // seleciona tours candidatos para crossover
    private static Tour tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // para cada lugar no tournament, recebe um "tour candidato" randomico e adiciona ele
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.saveTour(i, pop.getTour(randomId));
        }
        // recebe o tour mais apto
        Tour fittest = tournament.getFittest();
        return fittest;
    }
}
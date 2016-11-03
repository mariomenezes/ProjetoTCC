package com.mmc.nanopower.Genetic2;

/**
 * Created by mario on 07/09/16.
 */
public class HelloWorldAG {

    public static void main(String[] args) {

        //Define a solução
        Algoritmo.setSolucao("Olá Mundo");
        //Define os caracteres existentes
        Algoritmo.setCaracteres("!,.:;?áÁãÃâÂõÕôÔóÓéêÉÊíQWERTYUIOPASDFGHJKLÇZXCVBNMqwertyuiopasdfghjklçzxcvbnm1234567890 ");
        //taxa de crossover de 60%
        Algoritmo.setTaxaDeCrossover(0.6);
        //taxa de mutação de 3%
        Algoritmo.setTaxaDeMutacao(0.3);
        //elitismo
        boolean eltismo = true;
        //tamanho da população
        int tamPop = 100;
        //numero máximo de gerações
        int numMaxGeracoes = 10000;

        //define o número de genes do indivíduo baseado na solução
        int numGenes = Algoritmo.getSolucao().length();

        //cria a primeira população aleatérioa
        Populacao populacao = new Populacao(numGenes, tamPop);

        boolean temSolucao = false;
        int geracao = 0;

        System.out.println("Iniciando... Aptidão da solução: "+ Algoritmo.getSolucao().length());

        //loop até o critério de parada
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            populacao = Algoritmo.novaGeracao(populacao, eltismo);

            System.out.println("Geração " + geracao + " | Aptidão: " + populacao.getIndivduo(0).getAptidao() + " | Melhor: " + populacao.getIndivduo(0).getGenes());

            //verifica se tem a solucao
            temSolucao = populacao.temSolocao(Algoritmo.getSolucao());
        }

        if (geracao == numMaxGeracoes) {
            System.out.println("Número Maximo de Gerações | " + populacao.getIndivduo(0).getGenes() + " " + populacao.getIndivduo(0).getAptidao());
        }

        if (temSolucao) {
            System.out.println("Encontrado resultado na geração " + geracao + " | " + populacao.getIndivduo(0).getGenes() + " (Aptidão: " + populacao.getIndivduo(0).getAptidao() + ")");
        }
    }
}

package com.mmc.nanopower.Fuzzy;
import android.os.AsyncTask;
import android.util.Log;


//import com.mmc.nanopower.communication.ArduinoSensorState;
import com.mmc.nanopower.communication.SaveState;
import com.mmc.nanopower.communication.Tomadas;

import net.sourceforge.jFuzzyLogic.FIS;

import java.util.Timer;
import java.util.TimerTask;
//import net.sourceforge.jFuzzyLogic.FunctionBlock;

/**
 * Created by mario on 03/11/16.
 * Assistente para tomada de decisão visando ligar ou desligar equipamentos conectados ao arduino
 */


public class DecisionAssist extends AsyncTask<String, Void, Boolean> {


    //private SaveState saveState = null;
    private SaveState saveState = SaveState.getInstancia();
    int delay = 60000;   // delay de 1 min.
    int interval = 300000;  // intervalo de 5 min.
    Timer timer = new Timer();
    private static final String TAG = "DecisionAssist";


    @Override
    protected Boolean doInBackground(String... params) {

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
//                saveState = SaveState.getInstancia();

                fuzzyTomadas(Tomadas.tomada1.name());
                fuzzyTomadas(Tomadas.tomada2.name());
                fuzzyTomadas(Tomadas.tomada3.name());

            }

        }, delay, interval);

//        FIS fis = null;
//
//        fis = openFile(params[0]);
//        //apenas para teste
//        if(params[0] == "TCC") {
//
//            //fis = openFile(params[0]);
//
////           String filename = "/storage/emulated/0/regrasTCC.fcl";
////
////
////           FIS fis = FIS.load(filename, true); // Load from 'FCL' file
////           if (fis == null) {//erro ao carregar
////               Log.d("DECISAO", " erro carregar arquivo");
////               return false;
////           }
//
//           //testando plotagem
//           //mostra as regras
//           //FunctionBlock functionBlock = fis.getFunctionBlock(null);
//           //JFuzzyChart.get().chart(functionBlock);
//
//           double posicaoX = 20;
//           double anguloVeiculo = 30;
//           fis.setVariable("Posicao_x", posicaoX);
//           fis.setVariable("Angulo_veiculo", anguloVeiculo);
//           fis.evaluate();
//           double anguloRoda = fis.getVariable("Angulo_Roda").getLatestDefuzzifiedValue();
//           Log.d("DECISAO ", "anguloRoda");
//           System.out.println("Saida da logica Fuzzy: " + anguloRoda);
//           return true;
//       }
//
//
//        else if(params[0].contains("tomada")){
//
//
//            //TODO, aqui vai apenas verificar os dados dos sensores
//
//
//            fis.setVariable("TEMPERATURA",saveState.getTemperatura());
//            fis.setVariable("LUZ",saveState.getLuz());
//            fis.setVariable("DIA_SEMANA",saveState.getDiaSemana());
//            fis.setVariable("HORA",saveState.getHora());
//            fis.evaluate();
//            double resul = fis.getVariable("Angulo_Roda").getLatestDefuzzifiedValue();
//            Log.d("DECISAO ", "anguloRoda");
//            //System.out.println("Saida da logica Fuzzy: " + anguloRoda);
//
//            double res = fis.getVariable(params[0].toUpperCase()).getLatestDefuzzifiedValue();
//            Log.d("FUZZY ",String.valueOf(res));
//
//            //Retorna possibilidade >= a 70/100, entao liga tomada
////            if(res >= 0.7 && res <= 1){
////                acionaTomada = true;
////            }
////            else
////                acionaTomada = false;
//
//            //TODO params[0] contém o nome da tomada.
//            saveState.setTomada(params[0],(res >= 0.7 && res <= 1),res);

        //}



        return true;
    }

    private void fuzzyTomadas(String tomada){

        double h = saveState.getHora();
        Log.d(TAG, "HORA CALCULO " + String.valueOf(h));
        double minuto_temp = saveState.getMinuto();
        Log.d(TAG, "MINUTO CALCULO" + String.valueOf(minuto_temp));
        double m = converteMinutosFracao(minuto_temp);
        Log.d(TAG, "MIN_CONVER CALCULO" + String.valueOf(m));

        Log.d(TAG, "minutos convertidos = "+ String.valueOf(m));
        double r = h + m;

        Log.d(TAG, "Fuzzy " + tomada + "valores:");
        Log.d(TAG, "TEMP " + String.valueOf(saveState.getTemperatura()));
        Log.d(TAG, "LUZ " + String.valueOf(saveState.getLuz()));
        Log.d(TAG, "DIA " + String.valueOf(saveState.getDiaSemana()));
        Log.d(TAG, "HORA " + String.valueOf(r));
        FIS fis = openFile(tomada);
        fis.setVariable("temperatura",saveState.getTemperatura());
        fis.setVariable("luz",saveState.getLuz());
        double dia_semana = saveState.getDiaSemana();

        //corrige os dias da semana, exemplo
        if(dia_semana == 1)
            dia_semana = 7;
        else
            dia_semana -= 1;

        //fis.setVariable("dia",saveState.getDiaSemana());
        fis.setVariable("dia", dia_semana);
        fis.setVariable("hora", r);

        //variavel TOMADA1,TOMADA2,TOMADA3
        fis.evaluate();
        double res = fis.getVariable(tomada.toLowerCase()).getLatestDefuzzifiedValue();
        Log.d(TAG, "FUZZY " + tomada +" " + String.valueOf(res));

        //Intervalo entre nenhuma possibilidade, 0 e total possibilidade 1.0
        //Serao considerados resultados com pelo menos 0.7 de possibilidade
        if(res >= 0.7 && res <= 1.0){
                Log.d(TAG, tomada + " Possibilidade de ligar");
            //if(res >= 0.7){
                saveState.setTomada(tomada, true, res);
                Log.d(TAG, " TOMADAS ligacao pendente");
            //}
        }
        //Intervalo entre nenhuma possibilidade, 2.0 e total possibilidade 3.0
        //Serao considerados resultados com pelo menos 2.7 de possibilidade
        else if(res >= 0 && res <= 0.3){
            Log.d(TAG, tomada + " Possibilidade de desligar" + res);
           // if(res >= 2.7){
                Log.d(TAG, " TOMADAS desligamento pendente");
                saveState.setTomada(tomada, false, res);
            //}

        }
        else if(res == -1){
            Log.d(TAG, " nao encontrou nenhuma regra "+ tomada);
        }

        else{
            Log.d(TAG, " valor desconhecido" + String.valueOf(res));
        }
    }


    public double converteMinutosFracao(double minutos){

        double ret = minutos/60.0;
        Log.d(TAG, " conversao minutos " + String.valueOf(minutos) + " em " + String.valueOf(ret));
        return ret;
    }
    /*
    * params String: tomada1, tomada2, tomada3
    * */

    private FIS openFile(String filename){

        String filepath = "/storage/emulated/0/" + filename + ".fcl";


        FIS fis = FIS.load(filepath, true); // Load from 'FCL' file
        if (fis == null) {//erro ao carregar
            Log.d(TAG, " DECISAO erro carregar arquivo " + filepath);
        }
        Log.d(TAG, " DECISAO carregou com sucesso arquivo");
        return fis;
    }
}

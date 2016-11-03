package com.mmc.nanopower.Fuzzy;
import android.os.AsyncTask;
import android.util.Log;


import com.mmc.nanopower.communication.ArduinoSensorState;
import com.mmc.nanopower.communication.SaveState;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;

/**
 * Created by mario on 03/11/16.
 * Assistente para tomada de decisão visando ligar ou desligar equipamentos conectados ao arduino
 */


public class DecisionAssist extends AsyncTask<String, Void, Boolean> {


    private SaveState saveState = null;
    private boolean acionaTomada;

    @Override
    protected Boolean doInBackground(String... params) {


        saveState =  SaveState.getInstancia();

        FIS fis = null;

        fis = openFile(params[0]);
        //apenas para teste
        if(params[0] == "TCC") {

            //fis = openFile(params[0]);

//           String filename = "/storage/emulated/0/regrasTCC.fcl";
//
//
//           FIS fis = FIS.load(filename, true); // Load from 'FCL' file
//           if (fis == null) {//erro ao carregar
//               Log.d("DECISAO", " erro carregar arquivo");
//               return false;
//           }

           //testando plotagem
           //mostra as regras
           //FunctionBlock functionBlock = fis.getFunctionBlock(null);
           //JFuzzyChart.get().chart(functionBlock);

           double posicaoX = 20;
           double anguloVeiculo = 30;
           fis.setVariable("Posicao_x", posicaoX);
           fis.setVariable("Angulo_veiculo", anguloVeiculo);
           fis.evaluate();
           double anguloRoda = fis.getVariable("Angulo_Roda").getLatestDefuzzifiedValue();
           Log.d("DECISAO ", "anguloRoda");
           System.out.println("Saida da logica Fuzzy: " + anguloRoda);
           return null;
       }


        else if(params[0].contains("tomada")){


            //TODO, aqui vai apenas verificar os dados dos sensores

            fis.setVariable("TEMPERATURA",saveState.getTemperatura());
            fis.setVariable("LUZ",saveState.getLuz());
            fis.setVariable("DIA_SEMANA",saveState.getDiaSemana());
            fis.setVariable("HORA",saveState.getHora());
            fis.evaluate();

            double res = fis.getVariable(params[0].toUpperCase()).getLatestDefuzzifiedValue();
            Log.d("FUZZY ",String.valueOf(res));

            //Retorna possibilidade >= a 70/100, entao liga tomada
//            if(res >= 0.7 && res <= 1){
//                acionaTomada = true;
//            }
//            else
//                acionaTomada = false;

            //TODO params[0] contém o nome da tomada.
            saveState.setTomada(params[0],(res >= 0.7 && res <= 1),res);

        }


        return false;
    }

    /*
    * params String: tomada1, tomada2, tomada3
    * */

    private FIS openFile(String filepath){

        String filename = "/storage/emulated/0/regras" + filepath + ".fcl";


        FIS fis = FIS.load(filename, true); // Load from 'FCL' file
        if (fis == null) {//erro ao carregar
            Log.d("DECISAO", " erro carregar arquivo");
        }
        return fis;
    }
}

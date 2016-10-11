package com.mmc.nanopower.Storage;

/*Scanner in = new Scanner(new FileReader("dados.txt"));
while (in.hasNextLine()) {
    String line = scanner.nextLine();
    System.out.println(line);
}*/

import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class ReadarffSD{

    Scanner in = null;

    public void readFile() {

        try {
            in = new Scanner(new FileReader("/storage/emulated/0/database.arff"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Log.d("SAIDA TEXTO",line);
        }
    }

}
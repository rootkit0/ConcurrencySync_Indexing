package eps.scp;

import com.google.common.collect.HashMultimap;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SaveIndexThread extends Thread implements Runnable {

    private final String DIndexFilePrefix = "/IndexFile";   // Prefijo de los ficheros de Índice Invertido.

    private String outputDirectory;
    private int first_file;
    private int last_file;
    private long remainingKeys;
    private int remainingFiles;
    private Iterator KeyIterator;
    private HashMultimap<String, Long> Hash;
    private CyclicBarrier barrier;
    private Estadisticas GlobalStatistics;
    private Estadisticas ThreadStatistics;

    public SaveIndexThread(String outputDirectory, int first_file, int last_file, long remainingKeys, int remainingFiles, Iterator KeyIterator, HashMultimap<String, Long> Hash, CyclicBarrier barrier, Estadisticas GlobalStatistics, Estadisticas ThreadStatistics) {
        this.outputDirectory = outputDirectory;
        this.first_file = first_file;
        this.last_file = last_file;
        this.remainingKeys = remainingKeys;
        this.remainingFiles = remainingFiles;
        this.KeyIterator = KeyIterator;
        this.Hash = Hash;
        this.barrier = barrier;
        this.GlobalStatistics = GlobalStatistics;
        this.ThreadStatistics = ThreadStatistics;
    }

    @Override
    public void run() {
        String key = "";
        long keysByFile = 0;
        for(int i=first_file; i<=last_file; ++i) {
            try {
                File KeyFile = new File(outputDirectory + DIndexFilePrefix + String.format("%03d", i));
                FileWriter fw = new FileWriter(KeyFile);
                BufferedWriter bw = new BufferedWriter(fw);
                keysByFile = remainingKeys/remainingFiles;
                remainingKeys -= keysByFile;
                while (KeyIterator.hasNext() && keysByFile>0) {
                    key = (String) KeyIterator.next();
                    GlobalStatistics.AddBytesEscritos(key.length());
                    ThreadStatistics.AddBytesEscritos(key.length());
                    SaveIndexKey(key,bw);  // Salvamos la clave al fichero.
                    keysByFile--;
                }
                bw.close(); // Cerramos el fichero.
                --remainingFiles;
            } catch (IOException e) {
                System.err.println("Error opening Index file " + outputDirectory + "/IndexFile" + i);
                e.printStackTrace();
                System.exit(-1);
            }
        }
        try{
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void SaveIndexKey(String key, BufferedWriter bw)
    {
        try {
            Collection<Long> values = Hash.get(key);
            ArrayList<Long> offList = new ArrayList<Long>(values);
            // Creamos un string con todos los offsets separados por una coma.
            String joined = StringUtils.join(offList, ",");
            bw.write(key+"\t");
            bw.write(joined+"\n");
        } catch (IOException e) {
            System.err.println("Error writing Index file");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

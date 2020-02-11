package eps.scp;

import com.google.common.collect.HashMultimap;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class BuildIndexThread extends Thread implements Runnable {

    private int init_pos;
    private int workPerThread;
    private int KeySize;
    private String InputFilePath;
    private HashMultimap<String, Long> Hash;
    private CyclicBarrier barrier;
    private Estadisticas GlobalStatistics;
    private Estadisticas ThreadStatistics;
    private int[] PercentageValues;

    private Semaphore sem = new Semaphore(1, true);

    public BuildIndexThread(int init_pos, int workPerThread, int KeySize, String InputFilePath, HashMultimap<String, Long> Hash, CyclicBarrier barrier, Estadisticas GlobalStatistics, Estadisticas ThreadStatistics, int[] PercentageValues) {
        this.init_pos = init_pos;
        this.workPerThread = workPerThread;
        this.KeySize = KeySize;
        this.InputFilePath = InputFilePath;
        this.Hash = Hash;
        this.barrier = barrier;
        this.GlobalStatistics = GlobalStatistics;
        this.ThreadStatistics = ThreadStatistics;
        this.PercentageValues = PercentageValues;
    }

    @Override
    public void run() {
        FileInputStream is;
        try {
            long offset = init_pos - 1;
            int car;
            String key="";
            int num_keys = 0;
            int bytes_leidos = 0;

            File file = new File(InputFilePath);
            is = new FileInputStream(file);
            is.skip(init_pos);

            while((car = is.read()) != -1 && num_keys != workPerThread) {
                GlobalStatistics.AddBytesLeidos();
                ThreadStatistics.AddBytesLeidos();
                ++bytes_leidos;
                if(ArrayUtils.contains(PercentageValues, bytes_leidos)) {
                    GlobalStatistics.PrintGlobalStatistics();
                }
                offset++;
                if(car=='\n' || car=='\r' || car=='\t') {
                    // Sustituimos los carácteres de \n,\r,\t en la clave por un espacio en blanco.
                    if (key.length()==KeySize && key.charAt(KeySize-1)!=' ')
                        key = key.substring(1, KeySize) + ' ';
                    continue;
                }
                if(key.length()<KeySize) {
                    // Si la clave es menor de K, entonces le concatenamos el nuevo carácter leído.
                    key = key + (char) car;
                }
                else {
                    // Si la clave es igual a K, entonces eliminaos su primier carácter y le concatenamos el nuevo carácter leído (implementamos una slidding window sobre el fichero a indexar).
                    key = key.substring(1, KeySize) + (char) car;
                }
                if(key.length()==KeySize) {
                    //Incrementamos el numero de claves tratadas por el thread
                    ++num_keys;
                    GlobalStatistics.AddKeysGeneradas();
                    ThreadStatistics.AddKeysGeneradas();
                    GlobalStatistics.AddOffsetsGenerados();
                    ThreadStatistics.AddOffsetsGenerados();
                    // Si tenemos una clave completa, la añadimos al Hash, junto a su desplazamiento dentro del fichero.
                    AddKey(key, offset - KeySize + 1);
                }
            }
        } catch (FileNotFoundException fnfE) {
            System.err.println("Error opening Input file.");
        }  catch (IOException ioE) {
            System.err.println("Error read Input file.");
        }
        try{
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void AddKey(String key, long offset) {
        try {
            sem.release();
            Hash.put(key, offset);
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

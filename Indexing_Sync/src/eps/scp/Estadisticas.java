package eps.scp;

public class Estadisticas {
    private int keys_generadas = 0;
    private int keys_procesadas = 0;
    private int offsets_generados = 0;
    private int offsets_procesados = 0;
    private int bytes_leidos = 0;
    private int bytes_escritos = 0;
    private int bytes_procesados = 0;

    public synchronized void AddKeysGeneradas() {
        keys_generadas += 1;
    }

    public synchronized void AddKeysProcesadas() {
        keys_procesadas += 1;
    }

    public synchronized void AddOffsetsGenerados() {
        offsets_generados += 1;
    }

    public synchronized void AddOffsetsProcesados() {
        offsets_procesados += 1;
    }

    public synchronized void AddBytesLeidos() {
        bytes_leidos += 1;
    }

    public synchronized void AddBytesEscritos(int num_bytes) {
        bytes_escritos += num_bytes;
    }

    public synchronized void AddBytesProcesados(int num_bytes) {
        bytes_procesados += num_bytes;
    }

    public synchronized void PrintGlobalStatistics() {
        System.out.println("\n++++++++++++++++++ Estadisticas Globales ++++++++++++++++++");
        System.out.println("Numero de keys diferentes generados: " + keys_generadas);
        System.out.println("Numero de keys diferentes procesados: " + keys_procesadas);
        System.out.println("Numero de offsets generados: " + offsets_generados);
        System.out.println("Numero de offsets procesados: " + offsets_procesados);
        System.out.println("Numero de bytes del fichero leídos: " + bytes_leidos);
        System.out.println("Numero de bytes del fichero escritos: " + bytes_escritos);
        System.out.println("Numero de bytes del fichero procesados: " + bytes_procesados);
        System.out.println("Porcentaje de progreso: ");
    }

    public synchronized void PrintThreadStatistics(int id) {
        System.out.println("\n++++++++++++++++++ Estadisticas Thread: " + id + " ++++++++++++++++++");
        System.out.println("Numero de keys diferentes generados: " + keys_generadas);
        System.out.println("Numero de keys diferentes procesados: " + keys_procesadas);
        System.out.println("Numero de offsets generados: " + offsets_generados);
        System.out.println("Numero de offsets procesados: " + offsets_procesados);
        System.out.println("Numero de bytes del fichero leídos: " + bytes_leidos);
        System.out.println("Numero de bytes del fichero escritos: " + bytes_escritos);
        System.out.println("Numero de bytes del fichero procesados: " + bytes_procesados);
        System.out.println("Porcentaje de progreso: ");
    }
}

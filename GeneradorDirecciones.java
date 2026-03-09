import java.io.*;
import java.util.*;


public class GeneradorDirecciones {

    static final int INT_SIZE = 4; 

    public static void main(String[] args) throws IOException {
        if (args.length != 6) {
            System.err.println("Uso: java GeneradorDirecciones NF1 NC1 NF2 NC2 PAGE_SIZE archivo_salida");
            System.err.println("Nota: NF2 debe ser igual a NC1.");
            System.exit(1);
        }

        int nf1 = Integer.parseInt(args[0]);
        int nc1 = Integer.parseInt(args[1]);
        int nf2 = Integer.parseInt(args[2]);
        int nc2 = Integer.parseInt(args[3]);
        int pageSize  = Integer.parseInt(args[4]);
        String outFile = args[5];

        if (nf2 != nc1) {
            System.err.println("Error: NF2 debe ser igual a NC1 para poder multiplicar las matrices.");
            System.exit(1);
        }

        long baseM1 = 0L;
        long baseM2 = baseM1 + (long) nf1 * nc1 * INT_SIZE;
        long baseM3 = baseM2 + (long) nf2 * nc2 * INT_SIZE;
        long totalBytes = baseM3 + (long) nf1 * nc2 * INT_SIZE;
        long numPages = (totalBytes + pageSize - 1) / pageSize;


        List<Long> references = new ArrayList<>();

        for (int i = 0; i < nf1; i++) {
            for (int j = 0; j < nc2; j++) {
                for (int k = 0; k < nc1; k++) {
                    
                    references.add(baseM1 + (long)(i * nc1 + k) * INT_SIZE);
                    references.add(baseM2 + (long)(k * nc2 + j) * INT_SIZE);
                }
                references.add(baseM3 + (long)(i * nc2 + j) * INT_SIZE);
            }
        }

        long numRefs = references.size();


        //SE ESCRIBE EL ARCHIVO DE SALIDA 
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
            pw.printf("TP:%d, NF1:%d, NC1:%d, NF2:%d, NC2:%d, NR:%d, NP:%d%n",
                      pageSize, nf1, nc1, nf2, nc2, numRefs, numPages);

            for (long ref : references) {
                pw.println(ref);
            }
        }

        System.out.printf("Archivo '%s' generado exitosamente.%n", outFile);
        System.out.printf("  Matrices: mat1[%d][%d], mat2[%d][%d], mat3[%d][%d]%n",
                          nf1, nc1, nf2, nc2, nf1, nc2);
        System.out.printf("  Tamaño de página: %d bytes%n", pageSize);
        System.out.printf("  Total de referencias: %d%n", numRefs);
        System.out.printf("  Páginas virtuales usadas: %d%n", numPages);
        System.out.printf("  Bytes totales de las matrices: %d%n", totalBytes);
    }
}
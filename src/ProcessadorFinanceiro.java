import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

class ProcessadorFinanceiro implements Runnable {
    private final String tipo;
    private final String arquivo;
    private final Map<String, Double> agrupados;
    private final CyclicBarrier barrier;

    public ProcessadorFinanceiro(String tipo, String arquivo, Map<String, Double> agrupados, CyclicBarrier barrier) {
        this.tipo = tipo;
        this.arquivo = arquivo;
        this.agrupados = agrupados;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            lerArquivo();
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lerArquivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                String[] partes = linha.split(",");
                if (partes.length == 3) {
                    String data = partes[0];
                    double valor = Double.parseDouble(partes[1].replace("\"", "").trim());
                    agrupados.merge(data, valor, Double::sum);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao converter valor na linha: " + e.getMessage());
        }
    }

}
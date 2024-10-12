import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;

public class ControleFinanceiro {
    private static final int NUM_THREADS = 3;

    public static void main(String[] args) throws Exception {
        Map<String, Double> receitas = new HashMap<>();
        Map<String, Double> despesas = new HashMap<>();
        Map<String, Double> provisoes = new HashMap<>();

        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
            calcularTotais(receitas, despesas, provisoes);
        });

        Thread receitaThread = new Thread(new ProcessadorFinanceiro("RECEITA", "receitas.csv", receitas, barrier));
        Thread despesaThread = new Thread(new ProcessadorFinanceiro("DESPESA", "despesas.csv", despesas, barrier));
        Thread provisaoThread = new Thread(new ProcessadorFinanceiro("PROVISÃO", "provisao.csv", provisoes, barrier));

        receitaThread.start();
        despesaThread.start();
        provisaoThread.start();

        receitaThread.join();
        despesaThread.join();
        provisaoThread.join();
    }

    private static void calcularTotais(Map<String, Double> receitas, Map<String, Double> despesas, Map<String, Double> provisoes) {
        double totalReceitas = receitas.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalDespesas = despesas.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalProvisoes = provisoes.values().stream().mapToDouble(Double::doubleValue).sum();

        System.out.printf("Total Receitas: %.4f\n", totalReceitas);
        System.out.printf("Total Despesas: %.4f\n", totalDespesas);
        System.out.printf("Total Provisões: %.4f\n", totalProvisoes);
    }
}
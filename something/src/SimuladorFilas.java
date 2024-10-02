import java.util.Random;

class Cliente {
    double tempoChegada;

    public Cliente(double tempoChegada) {
        this.tempoChegada = tempoChegada;
    }
}

class Fila {
    int maxClientes;
    double tempoServicoMin;
    double tempoServicoMax;
    int numServidores;
    double probabilidadeDesvio;
    double clientesPerdidos;
    double tempoAcumulado;
    int clientesAtendidos;
    Random random;

    public Fila(int numServidores, int maxClientes, double tempoServicoMin, double tempoServicoMax) {
        this.numServidores = numServidores;
        this.maxClientes = maxClientes;
        this.tempoServicoMin = tempoServicoMin;
        this.tempoServicoMax = tempoServicoMax;
        this.random = new Random();
    }

    public double gerarTempoServico() {
        return tempoServicoMin + (tempoServicoMax - tempoServicoMin) * random.nextDouble();
    }

    public boolean processarCliente(Cliente cliente, double tempoAtual) {
        if (numServidores > 0 && random.nextDouble() < 0.8) { // probabilidade de ser atendido nesta fila
            double tempoServico = gerarTempoServico();
            tempoAcumulado += tempoServico;
            clientesAtendidos++;
            return true;
        } else {
            clientesPerdidos++;
            return false;
        }
    }
}

public class SimuladorFilas {
    public static void main(String[] args) {
        double tempoSimulacao = 0;
        double tempoGlobal = 100000; // Simular até o 100.000° evento
        Random random = new Random();

        // Fila 1: G/G/1 com tempo de chegada entre 2..4 min e atendimento entre 1..2 min
        Fila fila1 = new Fila(1, Integer.MAX_VALUE, 1, 2);

        // Fila 2: G/G/2/5 com atendimento entre 4..8 min
        Fila fila2 = new Fila(2, 5, 4, 8);

        // Fila 3: G/G/2/10 com atendimento entre 5..15 min
        Fila fila3 = new Fila(2, 10, 5, 15);

        double tempoChegada = 2.0; // Primeiro cliente chega em 2 minutos

        while (tempoSimulacao < tempoGlobal) {
            // Simular o tempo de chegada do próximo cliente
            tempoChegada += 2 + random.nextDouble() * 2; // Próximo cliente entre 2..4 minutos
            Cliente cliente = new Cliente(tempoChegada);

            // Cliente entra na Fila 1
            if (fila1.processarCliente(cliente, tempoSimulacao)) {
                // 80% vão para a Fila 2, 20% vão para a Fila 3
                if (random.nextDouble() < 0.8) {
                    fila2.processarCliente(cliente, tempoSimulacao);
                } else {
                    fila3.processarCliente(cliente, tempoSimulacao);
                }
            }

            // Atualizar o tempo da simulação
            tempoSimulacao = cliente.tempoChegada;
        }

        // Relatório de resultados
        System.out.println("Resultado da Simulação:");
        System.out.println("Fila 1: Clientes Atendidos = " + fila1.clientesAtendidos + ", Clientes Perdidos = " + fila1.clientesPerdidos);
        System.out.println("Fila 2: Clientes Atendidos = " + fila2.clientesAtendidos + ", Clientes Perdidos = " + fila2.clientesPerdidos);
        System.out.println("Fila 3: Clientes Atendidos = " + fila3.clientesAtendidos + ", Clientes Perdidos = " + fila3.clientesPerdidos);
        System.out.println("Tempo total de simulação: " + tempoSimulacao);
    }
}

# Projeto: Simulação de Monitoramento de Incêndios Florestais 🌲🔥

Este projeto simula o monitoramento de incêndios florestais usando threads para sensores, gerador de incêndio e centro de controle, com gerenciamento de uma matriz que representa a floresta.

## Estrutura Principal

### `Main.java`
**Ponto de entrada do programa:**
- **`main(String[] args)`**: Inicializa a floresta, cria e inicia threads para sensores, gerador de incêndio e centro de controle.
  - **Inicialização da Floresta**: Chama `initializeForest()` para configurar a matriz.
  - **Threads de Sensores**: Cria e inicia uma thread para cada nó sensor.
  - **Thread do Gerador de Incêndios**: Inicia a thread `FireGenerator`.
  - **Thread do Centro de Controle**: Inicia a thread `ControlCenter`.

### Funções principais:
- **`initializeForest()`**: Configura a matriz florestal, com posições aleatórias para sensores.
- **`printForest()`**: Garante a impressão segura da floresta com bloqueios para evitar conflitos de threads.

---

## Componentes do Sistema

### `SensorNode.java`
**SensorNode**:
- **`SensorNode(int x, int y)`**: Constrói um sensor com suas coordenadas iniciais.
- **`run()`**: Thread principal que monitora a posição, detecta incêndios e notifica vizinhos.
  - **Detecção de Incêndio**: Se um incêndio (`'@'`) for detectado, a célula é atualizada para `'/'` e os vizinhos são notificados.
  - **Notificação de Vizinhos**: O método `notifyNeighbors(int x, int y, String path)` calcula e notifica os sensores adjacentes.
  - **Validação**: `isValid(int x, int y)` verifica se as coordenadas estão dentro dos limites da floresta.

### `FireGenerator.java`
- **`FireGenerator()`**: Agenda e executa a geração de incêndios em intervalos fixos usando `ScheduledExecutorService`.
- **`run()`**: Gera um incêndio aleatório na floresta e sinaliza as threads de sensores correspondentes.

### `ControlCenter.java`
- **`notifyControlCenter(int x, int y)`**: Notifica sobre incêndios e chama `combatFire(x, y)` para extinguir o fogo.
- **`combatFire(int x, int y)`**: Extingue o fogo e atualiza o estado da floresta.

---

## Principais Funcionalidades
- **Monitoramento em Tempo Real**: Sensores monitoram a floresta continuamente e se comunicam em caso de incêndio.
- **Extinção Automática**: O centro de controle combate incêndios detectados nas bordas da floresta.

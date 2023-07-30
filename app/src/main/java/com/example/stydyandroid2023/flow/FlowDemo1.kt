package com.example.stydyandroid2023.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlin.system.measureTimeMillis

/*

FLOW é essencialmente um fluxo reativo de dados que pode ser emitido de um produtor
e coletado por um consumidor. Em outras palavras, Flow é uma forma de emitir de forma
assíncrona uma sequência de valores ao longo do tempo.

Com o Flow, os dados podem ser emitidos sem bloqueio, o que o torna ideal para lidar com
operações de longa duração ou vinculadas a E/S.
Ao contrário do LiveData, projetado para lidar com eventos relacionados à interface do usuário,
 o Flow foi projetado para lidar com fluxos de dados arbitrários.
 No Coroutine Flow, um Flow é um fluxo assíncrono frio que pode emitir vários valores ao longo
  do tempo. É definido pela interface Flow, que possui um único método chamado collect.
  O método collect usa uma interface FlowCollector que possui um único método chamado emit.
  O método emit é usado para emitir um valor para o FlowCollector, que é então propagado a jusante.
 */
fun interface Flow<T> {
    suspend fun collect(collector: FlowCollector<T>)
}

fun interface FlowCollector<T> {
    suspend fun emit(value: T)
}

/*
A interface Flow representa o fluxo de dados que emite itens de dados e a interface FlowCollector
representa o receptor de dados que consome itens de dados emitidos pelo Flow.
 */
val flow1 = object : Flow<String> {
    override suspend fun collect(collector: FlowCollector<String>) {
        collector.emit("Hello")
        delay(100)
        collector.emit("World")
    }
}

//COM LAMBDA
val flow1_0 = Flow<String> { collector ->
    collector.emit("Hello")
    delay(1000)
    collector.emit("World")
}

//PARA COLETAR:
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
suspend fun main() {
    flow1.collect(object : FlowCollector<String> {
        override suspend fun emit(value: String) {
            println(value)
        }
    })
    //COM LAMBDA
    flow1.collect { value ->
        println(value)
    }


    /*
Upstream e DownStream
No contexto do fluxo de co-rotina, o termo upstream refere-se à origem dos itens de dados que são
 emitidos no fluxo. O receptor downstream, por outro lado, é o consumidor desses itens de dados que
  são coletados da fonte upstream. É importante observar que o receptor downstream também pode ser
  um fluxo em si, criando uma
 cadeia de estágios de processamento de dados onde cada estágio coleta dados do estágio anterior.


 Construtores de Fluxo ( Flow Builders )
Coroutine Flow fornece vários construtores para criar fluxos.
Aqui estão alguns construtores comuns:

flowOf
A função flowOf() é usada para criar um Flow que emite um conjunto fixo de valores.
 */
    val flow = flowOf("Hello", "World")

    /*
    A função asFlow() é usada para converter qualquer iterável ou sequência em um Flow.
    */
    val flowAsF = listOf(1, 2, 3).asFlow()

    /*
    A função do flow{}  é usada para criar um fluxo personalizado. Ele pega um lambda de
    suspensão que fornece um FlowCollector como parâmetro e emite valores para ele usando a função emit().
    */

    val flowFlow = kotlinx.coroutines.flow.flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    /*
    Operadores de Transformação - Transformation Operators
    Coroutine Flow fornece uma variedade de operadores para transformar fluxos.
    Esses operadores permitem criar novos fluxos transformando os dados emitidos por um fluxo de origem.
     */

//map
    val numbers = (1..5).asFlow()
    numbers.map { it * 2 }.collect { println(it) }
//filter
    val numbersFilter = (1..5).asFlow()
    numbers.filter { it % 2 == 0 }.collect { println(it) }
//transform
    val numberTrans = (1..5).asFlow()
    numbers.transform { value ->
        emit("A$value")
        emit("B$value")
    }.collect { println(it) }

//zip
    val a = (1..5).asFlow()
    val b = (6..10).asFlow()
    a.zip(b) { x, y -> x + y }.collect { println(it) }


    /*
    Operadores de Terminal  Terminal Operators
    Um operador de terminal é uma função que aciona o processo de coleta do Flow, que faz com que o
    Flow emita valores e termine.



    toList() - Coleta todos os valores emitidos e os retorna como uma lista. Este operador é uma função
    de suspensão e deve ser chamado de dentro de uma co-rotina.

    toSet() - Coleta todos os valores emitidos e os retorna como um conjunto. Este operador é uma função
     de suspensão e deve ser chamado de dentro de uma co-rotina.

    count() - Conta o número de valores emitidos e retorna o resultado como um inteiro. Este operador é
    uma função de suspensão e deve ser chamado de dentro de uma co-rotina.

    reduce() - Aplica uma operação binária aos valores emitidos e retorna um único resultado.
     Este operador é uma função de suspensão e deve ser chamado de dentro de uma co-rotina.

    fold() - Aplica uma operação binária aos valores emitidos, com um valor acumulador inicial,
     e retorna um único resultado. Este operador é uma função de suspensão e deve ser chamado de dentro
      de uma co-rotina.

    collect() - Coleta os valores emitidos e aplica a ação especificada a cada um. Este operador é uma
     função de suspensão e deve ser chamado de dentro de uma co-rotina.


    Fluxos achatados - Flattening Flows
    Flattening Flows é o processo de pegar um fluxo de fluxos e transformá-lo em um único fluxo plano que
     emite itens de todos os fluxos aninhados.

    O operador flatMapConcat é usado quando você deseja mesclar vários fluxos em um único fluxo,
    concatenando-os sequencialmente um após o outro. É necessária uma função que transforma cada valor
    emitido do fluxo de origem em outro fluxo e, em seguida, nivela os fluxos resultantes. Isso significa
    que o coletor  recebe os valores do fluxo resultante na mesma ordem em que foram emitidos
    pelo fluxo fonte, com os valores de cada fluxo transformado emitidos na ordem em que foram emitidos.
     */

    val flow1 = flowOf("A", "B", "C")
    val flow2 = flowOf("D", "E", "F")
    val flow3 = flowOf("G", "H", "I")

    flowOf(flow1, flow2, flow3)
        .flatMapConcat { it }
        .collect {
            println(it)
        }

// Output: A B C D E F G H I

    /*
    O operador flatMapLatest é usado quando você deseja transformar os valores emitidos por um fluxo
    em outro fluxo, mas deseja apenas coletar valores do fluxo transformado mais recente
    e ignorar quaisquer fluxos transformados anteriormente. É necessária uma função que transforma
    cada valor emitido do fluxo de origem em outro fluxo e, em seguida, nivela os fluxos resultantes.
    Quando um novo valor é emitido pelo fluxo de origem, a transformação anterior é cancelada e a
    nova transformação é utilizada para coletar valores do fluxo resultante.
     */
    val flow11 = flowOf("A", "B", "C")
    val flow21 = flowOf("D", "E", "F").onEach { delay(100) }
    val flow31 = flowOf("G", "H", "I")

    flowOf(flow1, flow2, flow3)
        .flatMapLatest { it }
        .collect {
            println(it)
            delay(250)
        }

// Output: A, B, C, G, H, I

    /*
    Exception Transparency
    Transparência de exceção refere-se à capacidade de um programa de lidar corretamente com exceções
     de maneira transparente e previsível, sem comprometer a correção ou a segurança do programa.
      Em outras palavras, as exceções devem ser capazes de se propagar pelo código de maneira que deixe
       claro qual é o erro e onde ele ocorreu.
     */
    // What’s wrong with this code?
    try {
        flow.collect { println(it) }
    } catch (e: RuntimeException) {
        println("Error")
    }
    /*
    Ao agrupar a função collect dentro de um bloco try-catch, você está capturando quaisquer exceções
     que possam ocorrer dentro da função collect. No entanto, isso pode dificultar o rastreamento da
      origem da exceção, pois ela pode ter sido lançada por uma chamada de função dentro da função
      collect. Isso pode causar confusão e dificultar a depuração.

    Em vez disso, a abordagem recomendada é permitir que as exceções se propaguem de forma transparente,
     não capturando-as dentro da função collect. Dessa forma, se uma exceção for lançada,
      ela se propagará pela pilha de chamadas e será capturada pelo código de chamada,
       que pode tratá-la adequadamente.
     */
    val flowThrow = kotlinx.coroutines.flow.flow {
        emit(1)
        throw RuntimeException("Oops!")
    }.catch { e ->
        emit(-1)
    }

    flow.collect { value ->
        println(value)
    }

    //Execution context
    //flowOn
    flow
        .flowOn(Dispatchers.IO)
        .collect { println(it) }
    /*
    Ao usar o operador flowOn para alterar o despachante do emissor upstream em um fluxo,
    um ChannelCoroutine é adicionado no meio do coletor e do fluxo.

    Contrapressão e Buffer -- Backpressure and Buffering
    Ao lidar com grandes quantidades de dados, a contrapressão torna-se uma preocupação importante.
     Refere-se à capacidade de um consumidor sinalizar a um produtor para diminuir a taxa de produção
     quando o consumidor não consegue acompanhar a taxa de consumo. Em Kotlin Flows, a contrapressão é
      controlada por meio da suspensão do produtor quando o downstream não está pronto para receber mais dados.

    Suponha que você tenha um fluxo que emita um grande número de itens e cada item exija algum
     processamento demorado antes de poder ser coletado. O coletor aguardará o processamento
     de cada item antes de solicitar o próximo. Isso pode resultar em um atraso significativo
     no tempo de execução geral.
         */

    fun slowFlow(): kotlinx.coroutines.flow.Flow<Int> = kotlinx.coroutines.flow.flow {
        repeat(10) {
            delay(100)
            emit(it)
        }
    }
    measureTimeMillis {
        slowFlow().collect {
            delay(100)
            println("Received $it")
        }
        println("Done")
    }.let {
        println("Collected in $it ms")
    }
    /*
    Neste exemplo, a função slowFlow() emite números inteiros um a um com um atraso de 100 milissegundos
     entre cada emissão. A função de coleta também possui um atraso de 100 milissegundos entre cada consumo.
      Portanto, levará um total de 2 segundos para concluir a coleta de todos os inteiros emitidos.

    Buffer
      No entanto, se adicionarmos um buffer ao fluxo, podemos reduzir o tempo total gasto para concluir a coleta.
     */
    fun slowFlow2(): kotlinx.coroutines.flow.Flow<Int> = kotlinx.coroutines.flow.flow {
        repeat(10) {
            delay(100)
            emit(it)
        }
    }.buffer()


    measureTimeMillis {
        slowFlow2().collect {
            delay(100)
            println("Received $it")
        }
        println("Done")
    }.let {
        println("Collected in $it ms")
    }

/*
A função buffer recebe 2 parâmetros; capacidade e onBufferOverflow.

O parâmetro de capacidade especifica o número máximo de valores que podem ser armazenados no buffer.
 Se forem emitidos mais valores do que o buffer pode conter, o parâmetro onBufferOverflow especifica
  qual ação deve ser executada;

SUSPEND é o valor padrão e significa que, se o buffer estiver cheio, qualquer tentativa de enviar
um novo elemento para o buffer suspenderá o remetente até que haja espaço disponível no buffer.

DROP_OLDEST significa que se o buffer estiver cheio, qualquer tentativa de enviar um novo elemento
 para o buffer descartará o elemento mais antigo atualmente no buffer e adicionará o novo elemento.

DROP_LATEST significa que se o buffer estiver cheio, qualquer tentativa de enviar um novo elemento
 para o buffer descartará o novo elemento e manterá o conteúdo do buffer atual inalterado.
 */

}
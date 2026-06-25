package com.mailson.pereira.laboratory.exercises

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

data class EventDTO(
    val orderId: String,
    val orderDeliveryPersonId: String,
    val eventId: String,
    val eventTimestamp: Long,
    val eventStatus: EventStatus,
    val rewardValue: Double? = null
)

enum class EventStatus {
    ENTRADA, INTERMEDIARIO, FINALIZACAO
}

data class OrderRewardDTO(
    val orderDeliveryPersonId: String,
    val orderId: String,
    val rewardValue: Double
)

fun calcularTaxasFlow(eventos: Flow<EventDTO>): Flow<OrderRewardDTO> = flow {
    // Flow.groupBy retorna Flow<GroupedFlow<K,V>> onde cada grupo é um sub-Flow,
    // o que torna inviável consultas like minByOrNull sem coletar.
    // A abordagem correta é coletar todos os eventos e agrupar via coleção.
    val agrupados = eventos.toList()

    val resultado = agrupados
        .groupBy { it.orderDeliveryPersonId to it.orderId }
        .mapNotNull { (key, eventosPedido) ->
            val entrada = eventosPedido.filter { it.eventStatus == EventStatus.ENTRADA }.minByOrNull { it.eventTimestamp }
            val finalizacao = eventosPedido.filter { it.eventStatus == EventStatus.FINALIZACAO }.maxByOrNull { it.eventTimestamp }

            if (entrada != null && finalizacao != null && finalizacao.rewardValue != null) {
                val tempoHoras = (finalizacao.eventTimestamp - entrada.eventTimestamp) / 3600000.0
                if (tempoHoras > 0) {
                    OrderRewardDTO(key.first, key.second, finalizacao.rewardValue / tempoHoras)
                } else null
            } else null
        }

    resultado.forEach { emit(it) }
}

fun main() {
    runBlocking {
        val eventos = flowOf(
            EventDTO("order1", "personA", "evt1", 1_700_000_000_000, EventStatus.ENTRADA),
            EventDTO("order1", "personA", "evt2", 1_700_003_600_000, EventStatus.FINALIZACAO, rewardValue = 50.0),
            EventDTO("order2", "personB", "evt3", 1_700_010_000_000, EventStatus.ENTRADA),
            EventDTO("order2", "personB", "evt4", 1_700_040_000_000, EventStatus.FINALIZACAO, rewardValue = 90.0)
        )

        calcularTaxasFlow(eventos).collect { reward ->
            println("Entrega ${reward.orderId} por ${reward.orderDeliveryPersonId} → Taxa: R$ %.2f/hora".format(reward.rewardValue))
        }
    }
}
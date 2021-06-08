package br.com.zup.ranyell.keymanager.pix.lista

import br.com.zup.ranyell.keymanager.ChavePixResponse
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ChavePixInfoCliente(
    val pixId: String,
    val clienteId: String,
    val chave: String,
    val tipo: String,
    val registradaEm: String
) {

    companion object {
        fun of(chave: ChavePixResponse): ChavePixInfoCliente {
            return ChavePixInfoCliente(
                pixId = chave.pixId,
                clienteId = chave.clienteId,
                chave = chave.chave,
                tipo = chave.tipoDeChave.name,
                registradaEm = chave.registradaEm.let {
                    LocalDateTime.ofEpochSecond(it.seconds, it.nanos, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_DATE_TIME)
                }
            )
        }
    }
}
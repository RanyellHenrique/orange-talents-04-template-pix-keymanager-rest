package br.com.zup.ranyell.keymanager.pix.lista

import br.com.zup.ranyell.keymanager.ChavePixResponse
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ChavePixInfoCliente(
    val pixId: String,
    val clienteId: String,
    val chave: String,
    val tipoDeConta: String,
    val tipoDeChave: String,
    val registradaEm: LocalDateTime

) {

    companion object {
        fun of(chave: ChavePixResponse): ChavePixInfoCliente {
            return ChavePixInfoCliente(
                pixId = chave.pixId,
                clienteId = chave.clienteId,
                chave = chave.chave,
                tipoDeConta = chave.tipoDeConta.name,
                tipoDeChave = chave.tipoDeChave.name,
                registradaEm = chave.registradaEm.let {
                    LocalDateTime.ofEpochSecond(
                        it.seconds,
                        it.nanos,
                        ZoneOffset.UTC
                    )
                }
            )
        }
    }
}
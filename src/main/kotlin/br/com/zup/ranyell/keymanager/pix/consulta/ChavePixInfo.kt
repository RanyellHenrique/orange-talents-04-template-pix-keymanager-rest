package br.com.zup.ranyell.keymanager.pix.consulta

import br.com.zup.ranyell.keymanager.ConsultaChavePixResponse
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ChavePixInfo(
    val pixId: String,
    val clienteId: String,
    val chave: String,
    val titularNome: String,
    val titularCpf: String,
    val tipo: String,
    val conta: ContaAssociada,
    val registradaEm: LocalDateTime
) {

    companion object {
        fun of(chave: ConsultaChavePixResponse): ChavePixInfo {
            return ChavePixInfo(
                pixId = chave.pixId,
                clienteId = chave.clientId,
                chave = chave.chave,
                titularNome = chave.titularNome,
                titularCpf = chave.titularCpf,
                tipo = chave.tipoDeChave.name,
                registradaEm = chave.registradaEm.let {
                    LocalDateTime.ofEpochSecond(it.seconds, it.nanos, ZoneOffset.UTC)
                },
                conta = ContaAssociada(
                    nome = chave.conta.nome,
                    agencia = chave.conta.agencia,
                    numero = chave.conta.numero,
                    tipoDeConta = chave.conta.tipoDeconta.name
                )
            )
        }
    }


}

data class ContaAssociada(
    val nome: String,
    val agencia: String,
    val numero: String,
    val tipoDeConta: String
) {

}
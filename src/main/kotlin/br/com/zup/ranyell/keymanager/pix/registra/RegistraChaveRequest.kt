package br.com.zup.ranyell.keymanager.pix.registra

import br.com.zup.ranyell.keymanager.TipoDeChave as TipoDeChaveGRPC
import br.com.zup.ranyell.keymanager.TipoDeConta as TipoDeContaGRPC
import br.com.zup.ranyell.keymanager.RegistraChavePixRequest
import br.com.zup.ranyell.keymanager.compartilhado.validacao.ValidPixKey
import br.com.zup.ranyell.keymanager.pix.TipoDeConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@ValidPixKey
@Introspected
data class RegistraChaveRequest(
    @field:NotNull
    val tipoDeChave: TipoDeChave?,
    @Size(max = 77)
    val chave: String?,
    @field:NotNull
    val tipoDeConta: TipoDeConta?
) {
    fun toGrpcRequest(clienteId: String): RegistraChavePixRequest {
        return  RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId)
            .setTipoDeChave(TipoDeChaveGRPC.valueOf(tipoDeChave?.name ?: TipoDeChaveGRPC.UNKNOWN_TIPO_CHAVE.name))
            .setChave(chave?: "")
            .setTipoDeConta(TipoDeContaGRPC.valueOf(tipoDeConta?.name ?: TipoDeContaGRPC.UNKNOWN_TIPO_CONTA.name))
            .build()
    }

}

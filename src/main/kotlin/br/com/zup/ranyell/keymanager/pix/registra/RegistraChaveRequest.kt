package br.com.zup.ranyell.keymanager.pix.registra

import br.com.zup.ranyell.keymanager.RegistraChavePixRequest
import br.com.zup.ranyell.keymanager.compartilhado.validacao.ValidPixKey
import br.com.zup.ranyell.keymanager.pix.TipoDeConta
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class RegistraChaveRequest(
    @field:NotNull
    val tipoDeChave: TipoDeChave,
    @field:NotBlank @Size(max = 77)
    val chave: String,
    @field:NotNull
    val tipoDeConta: TipoDeConta
) {
    fun toGrpcRequest(clienteId: String): RegistraChavePixRequest {
        return  RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId)
            .setTipoDeChave(br.com.zup.ranyell.keymanager.TipoDeChave.valueOf(tipoDeChave.name))
            .setChave(chave?: "")
            .setTipoDeConta(br.com.zup.ranyell.keymanager.TipoDeConta.valueOf(tipoDeConta.name))
            .build()
    }

}

package br.com.zup.ranyell.keymanager.pix.remove

import br.com.zup.ranyell.keymanager.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.RemoveChavePixRequest
import br.com.zup.ranyell.keymanager.compartilhado.validacao.UUIDValid
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
@Controller("/api/v1/clientes/")
class RemoveChavePixController(
    private val grpcClient: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub
) {

    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    @Delete("{clienteId}/pix/{pixId}")
    fun remove(
        @Valid @PathVariable @NotBlank @UUIDValid pixId: String,
        @PathVariable @NotBlank @UUIDValid clienteId: String
    ): HttpResponse<Any> {
        grpcClient.remove(RemoveChavePixRequest.newBuilder()
                .setClienteId(clienteId)
                .setPixId(pixId)
                .build()
        ).also { LOOGER.info("Chave pix de id: $pixId do cliente: $clienteId removida com sucesso") }
        return HttpResponse.ok()
    }
}
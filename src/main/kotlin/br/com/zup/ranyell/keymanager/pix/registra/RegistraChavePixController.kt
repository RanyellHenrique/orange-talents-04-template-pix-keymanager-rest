package br.com.zup.ranyell.keymanager.pix.registra

import br.com.zup.ranyell.keymanager.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.compartilhado.validacao.UUIDValid
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller("/api/v1/clientes/")
@Validated
class RegistraChavePixController(
    @Inject private val grpcClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub
) {
    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    @Post("{clienteId}/pix")
    fun registra(
        @Valid @Body request: RegistraChaveRequest,
        @UUIDValid @NotBlank @PathVariable clienteId: String
    ): HttpResponse<Any>? {
        return grpcClient.registra(request.toGrpcRequest(clienteId))
            .also { LOOGER.info("Nova chave pix do cliente de id = $clienteId cadastrada com sucesso") }
            .let { HttpResponse.created(location(it.clienteId, it.pixId)) }
    }

    private fun location(clienteId: String, pixId: String) = HttpResponse.uri("/api/v1/clientes/${clienteId}/pix/${pixId}")
}
package br.com.zup.ranyell.keymanager.pix.consulta

import br.com.zup.ranyell.keymanager.ConsultaChavePixRequest
import br.com.zup.ranyell.keymanager.KeyManagerConsultaGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.compartilhado.validacao.UUIDValid
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/")
class ConsultaChavePixController(
    private val grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub
) {
    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    @Get("{clienteId}/pix/{pixId}")
    fun consulta(
        @Valid @PathVariable @UUIDValid clienteId: String,
        @PathVariable @UUIDValid pixId: String
    ): HttpResponse<ChavePixInfo> {
        return grpcClient.consulta(toRequestGrpc(pixId, clienteId))
            .also { LOOGER.info("Consulta da chave $pixId do cliente de id $clienteId realizada com sucesso") }
            .let { HttpResponse.ok(ChavePixInfo.of(it)) }
    }

    private fun toRequestGrpc(pixId: String, clienteId: String) = ConsultaChavePixRequest.newBuilder()
        .setPixId(
            ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                .setPixId(pixId)
                .setClienteId(clienteId)
                .build()
        ).build()
}
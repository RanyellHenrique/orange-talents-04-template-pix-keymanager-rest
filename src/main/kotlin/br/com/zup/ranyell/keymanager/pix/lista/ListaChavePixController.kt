package br.com.zup.ranyell.keymanager.pix.lista

import br.com.zup.ranyell.keymanager.KeyManagerListaChavePixPorClienteGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.ListaChavePixPorClienteRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import javax.validation.Valid


@Validated
@Controller("/api/v1/clientes/")
class ListaChavePixController(
    private val grpcClient: KeyManagerListaChavePixPorClienteGrpcServiceGrpc.KeyManagerListaChavePixPorClienteGrpcServiceBlockingStub
) {
    private val LOOGER = LoggerFactory.getLogger(this::class.java)

    @Get("{clienteId}/pix")
    fun lista(@Valid @PathVariable  clienteId: String): HttpResponse<List<ChavePixInfoCliente>> {
        return grpcClient.lista(ListaChavePixPorClienteRequest.newBuilder().setClientId(clienteId).build())
            .also { LOOGER.info("Consulta das chaves do cliente de id: $clienteId realizada com sucesso") }
            .run { chavesList.map (ChavePixInfoCliente::of) }
            .let { HttpResponse.ok(it) }
    }
}
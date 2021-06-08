package br.com.zup.ranyell.keymanager.pix.lista

import br.com.zup.ranyell.keymanager.*
import br.com.zup.ranyell.keymanager.pix.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavePixControllerTest {

    @field:Client("/")
    @field:Inject
    lateinit var client: HttpClient

    @field:Inject
    lateinit var grpcClient: KeyManagerListaChavePixPorClienteGrpcServiceGrpc.KeyManagerListaChavePixPorClienteGrpcServiceBlockingStub

    val PIX_ID_EMAIL = "036dde44-c87f-11eb-b8bc-0242ac130003"
    val PIX_ID_CPF = "93a0308f-80bb-4d85-a6d7-6192c721fb8e"
    val CLIENTE_ID = "ae93a61c-0642-43b3-bb8e-a17072295958"
    val CHAVE_EMAIL = "email@email.com"
    val CHAVE_CPF = "05324522199"
    val TIPO_DE_CHAVE_EMAIL = TipoDeChave.EMAIL
    val TIPO_DE_CONTA = TipoDeConta.CONTA_POUPANCA


    @Test
    internal fun `deve retornar a lista de chaves do cliente`() {
        //cenário
        `when`(grpcClient.lista(any())).thenReturn(
            ListaChavePixPorClienteResponse
                .newBuilder()
                .addAllChaves(
                    listOf(
                        chavePixGrpc(CHAVE_EMAIL, TIPO_DE_CHAVE_EMAIL, PIX_ID_EMAIL),
                        chavePixGrpc(CHAVE_CPF, TipoDeChave.CPF, PIX_ID_CPF)
                    )
                ).build()
        )
        //ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/pix")
        val response = client.toBlocking().exchange(request, List::class.java)
        //avaliação
        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(2, body().size)

        }

    }

    fun chavePixGrpc(chave: String, tipo: TipoDeChave, id: String) =
        ChavePixResponse.newBuilder()
            .setPixId(id)
            .setClienteId(CLIENTE_ID)
            .setChave(chave)
            .setTipoDeChave(TIPO_DE_CHAVE_EMAIL)
            .setTipoDeConta(TIPO_DE_CONTA)
            .setRegistradaEm(
                Timestamp.newBuilder()
                    .setNanos(12334556)
                    .setSeconds(1235566)
            )
            .build()

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class Clients {
        @Singleton
        fun stubMock() =
            mock(KeyManagerListaChavePixPorClienteGrpcServiceGrpc.KeyManagerListaChavePixPorClienteGrpcServiceBlockingStub::class.java)
    }
}

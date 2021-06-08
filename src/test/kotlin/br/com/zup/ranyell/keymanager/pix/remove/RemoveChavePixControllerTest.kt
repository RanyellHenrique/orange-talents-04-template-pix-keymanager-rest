package br.com.zup.ranyell.keymanager.pix.remove

import br.com.zup.ranyell.keymanager.KeyManagerRemoveGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.RemoveChavePixResponse
import br.com.zup.ranyell.keymanager.pix.KeyManagerGrpcFactory
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var clientGrpc: KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve remover a chave pix quando tudo estiver correto`() {
        //cenário
        val pixId = "ae93a61c-0642-43b3-bb8e-a17072295955"
        val clienteId = "ae93a61c-0642-43b3-bb8e-a17072295958"
        `when`(clientGrpc.remove(any())).thenReturn(
            RemoveChavePixResponse.newBuilder().setClienteId(clienteId).setPixId(pixId).build()
        )
        //ação
        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)
        //validação
        with(response) {
            assertEquals(HttpStatus.OK, status)
        }
    }

    @Test
    internal fun `nao deve remover a chave pix quando os argumentos forem invalidos`() {
        //cenário
        val pixId = "pixidinvlido"
        val clienteId = "clienteidinvalido"
        //ação
        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }
        //validação
        with(response) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = mock(KeyManagerRemoveGrpcServiceGrpc.KeyManagerRemoveGrpcServiceBlockingStub::class.java)
    }
}
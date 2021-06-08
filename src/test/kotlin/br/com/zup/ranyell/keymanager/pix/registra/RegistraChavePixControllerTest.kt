package br.com.zup.ranyell.keymanager.pix.registra

import br.com.zup.ranyell.keymanager.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.RegistraChavePixResponse
import br.com.zup.ranyell.keymanager.pix.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zup.ranyell.keymanager.pix.TipoDeConta as TipoDeContaModel
import br.com.zup.ranyell.keymanager.pix.registra.TipoDeChave as TipoDeChaveModel


@MicronautTest
internal class RegistraChavePixControllerTest {

    @field:Inject
    lateinit var grpcClient: KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    internal fun `deve registar uma nova chave quando tudo estiver correto`() {
        //cenário
        `when`(grpcClient.registra(any())).thenReturn(registraChavePixResponseGrpc())
        val novaChave = RegistraChaveRequest(TipoDeChaveModel.EMAIL, "email@email.com", TipoDeContaModel.CONTA_POUPANCA)
        //ação
        val request = HttpRequest.POST("/api/v1/clientes/ae93a61c-0642-43b3-bb8e-a17072295958/pix", novaChave)
        val response = client.toBlocking().exchange(request, RegistraChaveRequest::class.java)
        //validacão
        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(registraChavePixResponseGrpc().pixId))
    }

    @Test
    internal fun `nao deve registrar uma nova chave quando houver argumentos invalidos`() {
        //cenário
        `when`(grpcClient.registra(any())).thenReturn(registraChavePixResponseGrpc())
        val novaChave = RegistraChaveRequest(TipoDeChaveModel.EMAIL,"chave invalida" , TipoDeContaModel.CONTA_POUPANCA)
        val request = HttpRequest.POST("/api/v1/clientes/ae93a61c-0642-43b3-bb8e-a17072295958/pix", novaChave)
        //ação
        val response  = assertThrows<HttpClientResponseException> {
            client.toBlocking().exchange(request, Any::class.java)
        }
        //validacão
        with(response) {
            assertEquals(HttpStatus.BAD_REQUEST, status)
            assertEquals("request.null: Chave pix inválida para o tipo EMAIL", message)
        }

    }

    fun registraChavePixResponseGrpc(): RegistraChavePixResponse = RegistraChavePixResponse.newBuilder()
        .setClienteId("ae93a61c-0642-43b3-bb8e-a17072295958")
        .setPixId("ae93a61c-0642-43b3-bb8e-a17072295955")
        .build()

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            mock(KeyManagerRegistraGrpcServiceGrpc.KeyManagerRegistraGrpcServiceBlockingStub::class.java)
    }
}
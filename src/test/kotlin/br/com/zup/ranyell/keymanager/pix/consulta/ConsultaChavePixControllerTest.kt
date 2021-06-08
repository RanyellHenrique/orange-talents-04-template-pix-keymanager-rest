package br.com.zup.ranyell.keymanager.pix.consulta

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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaChavePixControllerTest {

    @field:Inject
    lateinit var grpcClient: KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub

    @field:Client("/")
    @field:Inject
    lateinit var client: HttpClient

    val PIX_ID = "036dde44-c87f-11eb-b8bc-0242ac130003"
    val CLIENTE_ID = "ae93a61c-0642-43b3-bb8e-a17072295958"
    val CHAVE = "email@email.com"
    val TIPO_DE_CHAVE = TipoDeChave.EMAIL
    val TITULAR_NOME = "Nome do titular"
    val TITULAR_CPF = "05324522199"
    val CONTA_NOME = "TAÚ UNIBANCO S.A."
    val CONTA_TIPO = TipoDeConta.CONTA_POUPANCA
    val CONTA_AGENCIA = "0001"
    val CONTA_NUMERO = "125987"

    @Test
    internal fun `deve retornar os dados da chave consultada`() {
        //cenário
        `when`(grpcClient.consulta(any())).thenReturn(grpcResponse())
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$CLIENTE_ID/pix/$PIX_ID")
        //ação
        val response = client.toBlocking().exchange(request, ChavePixInfo::class.java)
        //validação
        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(PIX_ID, body()!!.pixId)
            assertEquals(CHAVE, body()!!.chave)
            assertNotNull(body()!!.registradaEm)
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = mock(KeyManagerConsultaGrpcServiceGrpc.KeyManagerConsultaGrpcServiceBlockingStub::class.java)
    }

    fun grpcResponse() = ConsultaChavePixResponse.newBuilder()
        .setPixId(PIX_ID)
        .setClientId(CLIENTE_ID)
        .setChave(CHAVE)
        .setTitularNome(TITULAR_NOME)
        .setTitularCpf(TITULAR_CPF)
        .setTipoDeChave(TIPO_DE_CHAVE)
        .setConta(
            Conta.newBuilder()
                .setTipoDeconta(CONTA_TIPO)
                .setNome(CONTA_NOME)
                .setNumero(CONTA_NUMERO)
                .setAgencia(CONTA_AGENCIA)
                .build()
        )
        .setRegistradaEm(
            Timestamp.newBuilder()
                .setNanos(12334556)
                .setSeconds(1235566)
                .build()
        )
        .build()
}
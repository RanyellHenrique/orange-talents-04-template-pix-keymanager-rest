package br.com.zup.ranyell.keymanager.compartilhado.excecao

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    internal fun `deve retornar 404 quando statusException for not found`() {
        //cenário
        val mensagem = "não encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))
        //ação
        val response = GlobalExceptionHandler().handle(requestGenerica, notFoundException)
        //validação
        with(response) {
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertNotNull(body())
            assertEquals(mensagem, body()!!.message)
            assertNotNull(body()!!.instante)
            assertNotNull(body()!!.path)
        }
    }

    @Test
    internal fun `deve retornar 400 quando statusException for invalid argument`() {
        //cenário
        val mensagem = "argumentos inválidos"
        val invalidArgumentException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))
        //ação
        val response = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)
        //validação
        with(response) {
            assertNotNull(body())
            assertEquals(HttpStatus.BAD_REQUEST, status)
            assertEquals(mensagem, body()!!.message)
            assertNotNull(body()!!.instante)
            assertNotNull(body()!!.path)
        }
    }

    @Test
    internal fun `deve retornar 422 quando statusException for already exists`() {
        //cenário
        val mensagem = "chave existente"
        val invalidArgumentException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))
        //ação
        val response = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)
        //validação
        with(response) {
            assertNotNull(body())
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
            assertEquals(mensagem, body()!!.message)
            assertNotNull(body()!!.instante)
            assertNotNull(body()!!.path)
        }
    }

    @Test
    internal fun `deve retornar 500 quando statusException for diferente`() {
        //cenário
        val mensagem = "Erro interno"
        val invalidArgumentException = StatusRuntimeException(Status.INTERNAL.withDescription(mensagem))
        //ação
        val response = GlobalExceptionHandler().handle(requestGenerica, invalidArgumentException)
        //validação
        with(response) {
            assertNotNull(body())
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
            assertEquals(mensagem, body()!!.message)
            assertNotNull(body()!!.instante)
            assertNotNull(body()!!.path)
        }
    }
}
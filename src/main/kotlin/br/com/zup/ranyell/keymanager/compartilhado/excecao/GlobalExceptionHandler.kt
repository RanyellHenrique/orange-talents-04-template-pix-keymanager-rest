package br.com.zup.ranyell.keymanager.compartilhado.excecao

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import java.time.Instant
import javax.inject.Singleton

@Produces
@Singleton
@Requirements(Requires(classes = [StatusRuntimeException::class, ExceptionHandler::class]))
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<ErrorPadrao>> {

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException): HttpResponse<ErrorPadrao> {
        val erro = ErrorPadrao(
            status = when (exception.status.code) {
                Status.ALREADY_EXISTS.code -> HttpStatus.UNPROCESSABLE_ENTITY
                Status.INVALID_ARGUMENT.code -> HttpStatus.BAD_REQUEST
                Status.NOT_FOUND.code -> HttpStatus.NOT_FOUND
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            },
            message = exception.status.description!!,
            instante = Instant.now(),
            path = request!!.path
        )
        return HttpResponse.status<ErrorPadrao>(erro.status).body(erro)
    }

}
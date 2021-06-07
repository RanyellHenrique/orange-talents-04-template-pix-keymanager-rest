package br.com.zup.ranyell.keymanager.compartilhado.excecao

import io.micronaut.http.HttpStatus
import java.time.Instant

data class ErrorPadrao(
    val status: HttpStatus,
    val message: String,
    val instante: Instant,
    val path: String
) {
}
package br.com.zup.ranyell.keymanager.pix

import br.com.zup.ranyell.keymanager.KeyManagerConsultaGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.KeyManagerListaChavePixPorClienteGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.KeyManagerRegistraGrpcServiceGrpc
import br.com.zup.ranyell.keymanager.KeyManagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keymanager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = KeyManagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultaChave() = KeyManagerConsultaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeyManagerListaChavePixPorClienteGrpcServiceGrpc.newBlockingStub(channel)
}
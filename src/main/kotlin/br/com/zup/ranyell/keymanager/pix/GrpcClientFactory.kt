package br.com.zup.ranyell.keymanager.pix

import br.com.zup.ranyell.keymanager.KeyManagerRegistraGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory(@GrpcChannel("keymanager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeyManagerRegistraGrpcServiceGrpc.newBlockingStub(channel)
}
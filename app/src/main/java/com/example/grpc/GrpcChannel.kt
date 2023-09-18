package com.example.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

object GrpcChannel

{
    fun getChannel():ManagedChannel{
        return ManagedChannelBuilder.forAddress("192.168.28.54",50040).usePlaintext().build()
    }
}
package com.example.grpc


import android.util.Log
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GrpcClient {
    private  val  managedChannel :ManagedChannel =
        ManagedChannelBuilder
            .forAddress("192.168.10.23",50040)
            .usePlaintext()
            .build()

    private val greeterStub: GreeterGrpc.GreeterStub = GreeterGrpc.newStub(managedChannel)
    private val loginStub=LoginGrpc.newStub(managedChannel)
    private val chatStub=ChatGrpc.newStub(managedChannel)

    private val chatBlockingStub = ChatGrpc.newBlockingStub(managedChannel)



    fun login(request: LoginRequest):Flow<LoginResponse> =
        callbackFlow {
//            managedChannel.shutdown()
//            managedChannel.awaitTermination()
//            managedChannel.enterIdle()
//            managedChannel.resetConnectBackoff()
//            managedChannel.shutdownNow()

            val responseObserver=object:StreamObserver<LoginResponse>{
                override fun onNext(value: LoginResponse?) {
                   value?.let {
                       trySend(value)
                   }
                }

                override fun onError(t: Throwable?) {
                    close(t)
                }

                override fun onCompleted() {
                    close()
                }

            }
            loginStub.loginWithUserNameAndPassword(request, responseObserver)
            awaitClose { responseObserver.onCompleted() }
        }

    fun shutDown(){
        managedChannel.shutdown()
    }

    val TAG="TAG"
    fun createBidirectionalFlow(request: MutableSharedFlow<HelloRequest>):Flow<HelloResponse> =
        callbackFlow {
            val responseobserver = object : StreamObserver<HelloResponse> {
                override fun onNext(value: HelloResponse?) {
                    Log.d(TAG, "onNext: $value")
                    value?.let {
                        trySend(it)
                    }
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "onError: ")
                    close(t)
                }

                override fun onCompleted() {
                    Log.d(TAG, "onCompleted: ")
                    close()
                }

            }
            val requestObserver=greeterStub.bidirectionalHello(responseobserver)
            launch {
                request.collect{
                    withContext(Dispatchers.IO){
                        requestObserver.onNext(it)
                    }


                }
            }
            awaitClose {
                requestObserver.onCompleted()
            }

        }

    fun startChat(chatMessageFlow: MutableSharedFlow<ChatRequest>): Flow<ChatResponse> =
        callbackFlow {
            Log.d("Chat", "startChat")
            val responseObserver=object :StreamObserver<ChatResponse>{
                override fun onNext(value: ChatResponse?) {
                    value?.let {
                        trySend(value)
                    }
                }

                override fun onError(t: Throwable?) {
                    close(t)
                }

                override fun onCompleted() {
                   close()
                }

            }
            val requestObserver=chatStub.joinChat(responseObserver)
            launch {
                chatMessageFlow.collect(){
                    requestObserver.onNext(it)
                }
            }

            awaitClose {
                responseObserver.onCompleted()
            }
        }


}
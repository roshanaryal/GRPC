package com.example.grpc

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class MainActivityViewModel : ViewModel() {
    val grpcClient = GrpcClient()

    fun shutDown() {
        grpcClient.shutDown()
    }





    fun login(username:String,password:String):Flow<LoginResponse>{
        val loginRequest=LoginRequest.newBuilder().setUsername(username).setPassword(password).build()
        return grpcClient.login(loginRequest)
    }




//    fun sendMessage(m:String) {
//       val message: HelloRequest=HelloRequest.newBuilder().setName(m).build()
//        CoroutineScope(Dispatchers.IO).launch {
//            userMessageFlow.emit(message)
//        }
//
//    }


//    fun startBidirectionalStreaming(): SharedFlow<HelloResponse> {
//        return userMessageFlow.flatMapLatest {
//            grpcClient.createBidirectionalFlow(flowOf(it))
//        }
//    }

//    fun startBidirectionalStreaming(): Flow<HelloRequest> = flow {
//        Log.d("TAG", "startBidirectionalStreaming: ")
//        for (i in 0..5) {
//            emit(HelloRequest.newBuilder().setName("Name $i").build())
//        }
//    }

    fun observeBidirectionalStreaming() =
        grpcClient.createBidirectionalFlow(userMessageFlow).flowOn(Dispatchers.IO)

    suspend fun ObserveData() {
        Log.d("TAG", "ObserveData: ")
//        withContext(Dispatchers.IO){
//            observeBidirectionalStreaming().collect(){
//                Log.d("TAG", "ObserveData: ${it.message}")
//            }
    }


}
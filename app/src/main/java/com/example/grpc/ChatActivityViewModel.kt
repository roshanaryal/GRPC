package com.example.grpc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatActivityViewModel :ViewModel() {



    private val chatMessageFlow = MutableSharedFlow<ChatRequest>()
    private val id= "${java.util.Random().nextInt(1000)}"

    fun getId()=id

    fun sendMessage(message:String){
        val message = ChatRequest.newBuilder().setMessage(message).setId(id).build()
        CoroutineScope(Dispatchers.IO).launch {
            chatMessageFlow.emit(message)
        }
    }

    fun startChat() = grpcClient.startChat(chatMessageFlow).map {value: ChatResponse ->
        MessageModel(value.id,value.message)
    }

    val grpcClient = GrpcClient()

    fun shutDown() {
        grpcClient.shutDown()
    }
}
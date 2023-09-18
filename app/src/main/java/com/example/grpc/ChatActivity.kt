package com.example.grpc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {
    private lateinit var viewModel: ChatActivityViewModel
    private lateinit var messageTv: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewModel = ViewModelProvider(this)[ChatActivityViewModel::class.java]
        messageTv = findViewById(R.id.messageTv)
        messageEt = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.button)

        sendButton.setOnClickListener {
            if (!messageEt.text.isNullOrEmpty()){
                viewModel.sendMessage(messageEt.text.toString())
                messageEt.setText( "")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.startChat().catch {
                Log.d("TAG", "Erroe : ${it.message} ")
            }.collect {

                val message = if (it.id == viewModel.getId()){
                    " Me : ${it.message}"
                }else {
                    "From(${it.id}): ${it.message} "
                }
                CoroutineScope(Dispatchers.Main).launch {
                    messageTv.append("\n $message")
                }
            }
        }



    }
}
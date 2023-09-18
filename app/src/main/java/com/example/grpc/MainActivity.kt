package com.example.grpc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity/TAG"


    private lateinit var viewModel: MainActivityViewModel
    private lateinit var textView: TextView
    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        textView = findViewById(R.id.textview)
        usernameEt = findViewById(R.id.usernameEt)
        passwordEt = findViewById(R.id.passwordEt)
        button = findViewById(R.id.sendButton)

        button.setOnClickListener {
            if (usernameEt.text.isNullOrEmpty() || passwordEt.text.isNullOrEmpty()){
                Toast.makeText(this,"Enter username or password",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val username=usernameEt.text.toString()
            val password=passwordEt.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.login(username,password).catch {  }.collect(){
                    var result=""
                    if (it.success){
                         result="Login succesful for $username \n Name : ${it.fullname} \n gender : ${it.gender.toString()} \n "
                    }else{
                         result="Login failed for $username try again "
                    }
                    withContext(Dispatchers.Main){
                        textView.text=result
                    }

                }
            }

        }



//        button.setOnClickListener {
//            var message="Empty message"
//           if (!edittext.text.isNullOrEmpty()){
//               message=edittext.text.toString()
//           }
//            viewModel.sendMessage(message)
////            edittext.setText("")
//        }

//        CoroutineScope(Dispatchers.IO).launch {
////            viewModel.ObserveData()
//            viewModel.observeBidirectionalStreaming().catch {
//                Log.d(TAG, "onCreate: Excecption ${it.message}")
//            }
//                    .collect() {
//                    Log.d(TAG, "onCreate: ${it.message}")
//                    withContext(Dispatchers.Main) {
//                        textView.append("\n ${it.message}")
//                    }
////                Log.d(TAG, "onCreate: ${it.message}")
////            }
//                }
//        }


    }
}
        

//        managedChannel = ManagedChannelBuilder.forAddress("address",111).usePlaintext().build()
//
//       val greeterStub = GreeterGrpc.newBlockingStub(managedChannel)
//
//    //urinary call
//    val request=HelloRequest.newBuilder().setName("Roshan").build()
//    val ss = greeterStub.sayHello(request)
//    val response=greeterStub.sayHello(request,object :StreamObserver<HelloResponse>{
//        override fun onNext(value: HelloResponse?) {
//            val message=value?.message
//        }
//
//        override fun onError(t: Throwable?) {
//
//        }
//
//        override fun onCompleted() {
//
//        }
//
//    })
//
//
//    //server streaming lots of replies
//    val lotsofreplies = greeterStub.lotsOfReplies(request,object :StreamObserver<HelloResponse>{
//        override fun onNext(value: HelloResponse?) {
//            value?.let {
//                Log.d(TAG, "onNext: ${value.message}")
//            }
//        }
//
//        override fun onError(t: Throwable?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onCompleted() {
//            TODO("Not yet implemented")
//        }
//
//    })
//
//    //client streaming lots of request
//    val requestObserver=greeterStub.lotsOfRequests(object :StreamObserver<HelloResponse>{
//        override fun onNext(value: HelloResponse?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onError(t: Throwable?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onCompleted() {
//            TODO("Not yet implemented")
//        }
//
//    })
//    requestObserver.onNext(HelloRequest.newBuilder().setName("Test1").build())
//    requestObserver.onNext(HelloRequest.newBuilder().setName("Test2").build())
//    requestObserver.onCompleted()
//


//
//    }
//
//
//
//
//
//
//
//
//}
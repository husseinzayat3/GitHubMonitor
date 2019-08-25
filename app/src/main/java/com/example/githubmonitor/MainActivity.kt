package com.example.githubmonitor;
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {
    val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHandler = DbHelper(this, null)

        val btn_add_project = findViewById(R.id.addProject) as Button
        // set on-click listener
        btn_add_project.setOnClickListener {
            // your code to perform when the user clicks on the button
            var name = findViewById(R.id.ProjectUrl) as EditText
            val lstValues: List<String> = name.text.toString().split("/").map { it -> it.trim() }

//            var url="http://jsonplaceholder.typicode.com/users/"+name.text.toString()
            if (name.text.toString().length < 1 || !name.text.toString().contains("/")) {

                Toast.makeText(this@MainActivity, "Please enter URL/ID", Toast.LENGTH_SHORT).show()
            } else {
                //https://api.github.com/users/NicolaSabino/repos
                //https://api.github.com/repos/NicolaSabino/SaferPlace
                var newUrl = "https://api.github.com/repos/" + name.text.toString()
                val request = Request.Builder()
                    .url(newUrl)
                    .build()


                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {


                        if (response.message().equals("Not Found")) {
//                           do Nothing (dont add the project)

                        } else {
                            dbHandler.addProject(name.text.toString())

                        }

                    }

                })

            }
        }
            val btn_view_all = findViewById(R.id.ViewAll) as Button
            btn_view_all.setOnClickListener {
                val intent = Intent(this, Projects::class.java)
                startActivity(intent)

            }
        }
    }


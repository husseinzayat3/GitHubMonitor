package com.example.githubmonitor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList

class ProjectDetails : AppCompatActivity() {

    var userInfo:Users= Users();

    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHandler = DbHelper(this, null)
        setContentView(R.layout.activity_project_details)
        var projectname = findViewById(R.id.projectName) as TextView
        var username = findViewById(R.id.username) as TextView

//
        val ss: String = intent.getStringExtra("ID")
        val lstValues: List<String> = ss.split("/").map { it -> it.trim() }
        projectname.setText(lstValues[1])

        username.setText(lstValues[0])

        var base_url="https://api.github.com/repos/"+ss
         set_data(base_url,"/commits")
         set_data(base_url,"/contributors")


        var base_url2="https://api.github.com/users/"+lstValues[0]
        set_data2(base_url2,"/starred")
        set_data2(base_url2,"/followers")
        val btn_delete_project = findViewById(R.id.delete) as Button

        btn_delete_project.setOnClickListener {

            dbHandler.deleteProject(ss)
            val intent = Intent(this,Projects::class.java)
            startActivity(intent)

        }


    }
    fun set_data(url:String,data:String){
        var newUrl=url+data
        val request = Request.Builder()
            .url(newUrl)
            .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("===============")
            }

            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body()!!.string()

                //creating json object
                val json_contact: JSONArray = JSONArray(str_response)
                println(json_contact[0])
                //creating json array
                var jsonarray_info: JSONObject = json_contact.getJSONObject(0)
//                var i: Int = 0
                var size= json_contact.length()
                if(data.equals("/commits")){
                    var json_objectdetail:JSONObject=jsonarray_info.getJSONObject("commit").getJSONObject("author")
                    userInfo.date=json_objectdetail.getString("date")
                    userInfo.num_commiters=size.toString()
                }

                runOnUiThread {
                    if (data.equals("/commits")) {
                        var num_commitsTV = findViewById(R.id.num_commits) as TextView
                        var last_commitTV = findViewById(R.id.latest_commit) as TextView

                        num_commitsTV.setText(userInfo.num_commiters)
                        last_commitTV.setText(userInfo.date)
                    } else if (data.equals("/contributors")) {
                        var num_contributorsTV = findViewById(R.id.num_contributors) as TextView
                        num_contributorsTV.setText(size.toString())
                    }
                }
            }
        })
    }

    fun set_data2(url: String,data:String) {
        var newUrl=url+data

        val request = Request.Builder()
            .url(newUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body()!!.string()

                //creating json object
                val json_contact: JSONArray = JSONArray(str_response)

                var size= json_contact.length()

                runOnUiThread {
                    //stuff that updates ui
                    if (data.equals("/starred")) {
                        var num_starsTV = findViewById(R.id.num_stars) as TextView
                        num_starsTV.setText(size.toString())
                    }else if(data.equals("/followers")){
                        var num_followersTV = findViewById(R.id.num_followers) as TextView
                        num_followersTV.setText(size.toString())
                    }

                }
            }
        })
    }
}

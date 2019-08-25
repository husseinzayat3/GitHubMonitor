package com.example.githubmonitor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_projects.*

class Projects : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)

//        tvDisplayName.text = ""
        val dbHandler = DbHelper(this, null)
        val cursor = dbHandler.getAllName()
        cursor!!.moveToFirst()
        val arrayList = ArrayList<String>()//Creating an empty arraylist
//        tvDisplayName.append((cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME))))
        while (cursor.moveToNext()) {
            arrayList.add((cursor.getString(cursor.getColumnIndex(DbHelper.COLUMN_NAME))))


        }
        cursor.close()


        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { adapterView, view, position: Int, id: Long ->

            val intent = Intent(this,ProjectDetails::class.java)
            intent.putExtra("ID", arrayList[position])

            startActivity(intent)

            Toast.makeText(this,arrayList[position], Toast.LENGTH_LONG).show()
            finish()
        }
    }
}

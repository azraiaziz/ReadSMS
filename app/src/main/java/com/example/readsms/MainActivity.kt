package com.example.readsms

import android.app.ListActivity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.cursoradapter.widget.CursorAdapter
import com.example.readsms.databinding.ActivityMainBinding

//for best practice use recycler view
//
//class MainActivity : AppCompatActivity() { replaced with >
class MainActivity : ListActivity() {
    private lateinit var binding:ActivityMainBinding

    //content provider to open sms
    val SMS = Uri.parse("content://sms")
    val PERMISSION_REQUEST_READ_SMS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readSMS()
    }
    //column di dalam table sms yang akan diakses melalui content provider
    object SmsColumns {
        val ID = "_id"
        val ADDRESS = "address"
        val DATE  = "date"
        val BODY = "body"
    }

    private inner class SmsCursorAdapter(context: Context, c: Cursor, autorequery:Boolean) :
            CursorAdapter(context, c, autorequery){
        override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
            return View.inflate(context, R.layout.activity_main, null)
        }

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            view!!.findViewById<TextView>(R.id.sms_origin).text = cursor!!.getString(
                cursor.getColumnIndexOrThrow(SmsColumns.ADDRESS))
            view!!.findViewById<TextView>(R.id.sms_body).text = cursor!!.getString(
                cursor.getColumnIndexOrThrow(SmsColumns.BODY)
            )
            view!!.findViewById<TextView>(R.id.sms_date).text = cursor!!.getString(
                cursor.getColumnIndexOrThrow(SmsColumns.DATE)
            )
            //deprecated not like recycler view. non-deprecated like deprecated
        }
            }

    private fun readSMS(){
        //Baca database dari content provider
        //retrieve the SMS DB, Specify the required column and sort order
        val cursor = contentResolver.query(SMS, arrayOf(SmsColumns.ID, SmsColumns.BODY, SmsColumns.DATE, SmsColumns.ADDRESS),null,null, SmsColumns.DATE + "DESC")
        val adapter = SmsCursorAdapter(this,cursor!!, true)
        listAdapter = adapter

    }
}